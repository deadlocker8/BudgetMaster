package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.charts.ChartService;
import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatch;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatchList;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.icon.Iconizable;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageService;
import de.deadlocker8.budgetmaster.repeating.RepeatingTransactionUpdater;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.TagRepository;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupRepository;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupType;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionBase;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import de.deadlocker8.budgetmaster.utils.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportService.class);

	private final CategoryRepository categoryRepository;
	private final TransactionRepository transactionRepository;
	private final TemplateGroupRepository templateGroupRepository;
	private final TemplateRepository templateRepository;
	private final TagRepository tagRepository;
	private final ChartService chartService;
	private final ImageService imageService;
	private final RepeatingTransactionUpdater repeatingTransactionUpdater;
	private final AccountRepository accountRepository;
	private final IconService iconService;


	private InternalDatabase database;
	private List<String> collectedErrorMessages;

	@Autowired
	public ImportService(CategoryRepository categoryRepository, TransactionRepository transactionRepository, TemplateGroupRepository templateGroupRepository, TemplateRepository templateRepository,
						 TagRepository tagRepository, ChartService chartService, ImageService imageService, RepeatingTransactionUpdater repeatingTransactionUpdater, AccountRepository accountRepository, IconService iconService)
	{
		this.categoryRepository = categoryRepository;
		this.transactionRepository = transactionRepository;
		this.templateGroupRepository = templateGroupRepository;
		this.templateRepository = templateRepository;
		this.tagRepository = tagRepository;
		this.chartService = chartService;
		this.imageService = imageService;
		this.repeatingTransactionUpdater = repeatingTransactionUpdater;
		this.accountRepository = accountRepository;
		this.iconService = iconService;
	}

	public List<ImportResultItem> importDatabase(InternalDatabase database, AccountMatchList accountMatchList, Boolean importTemplateGroups, Boolean importTemplates, Boolean importCharts)
	{
		this.database = database;
		this.collectedErrorMessages = new ArrayList<>();

		final List<ImportResultItem> importResultItems = new ArrayList<>();

		LOGGER.debug("Importing database...");
		importResultItems.add(importImages());
		importIcons();
		importResultItems.add(importCategories());
		importResultItems.add(importAccounts(accountMatchList));
		importResultItems.add(importTransactions());

		if(importTemplateGroups)
		{
			importResultItems.add(importTemplateGroups());
		}
		else
		{
			importResultItems.add(new ImportResultItem(EntityType.TEMPLATE_GROUP, 0, 0));
		}

		if(importTemplates)
		{
			importResultItems.add(importTemplates(importTemplateGroups));
		}
		else
		{
			importResultItems.add(new ImportResultItem(EntityType.TEMPLATE, 0, 0));
		}

		if(importCharts)
		{
			importResultItems.add(importCharts());
		}
		else
		{
			importResultItems.add(new ImportResultItem(EntityType.CHART, 0, 0));
		}

		LOGGER.debug("Updating repeating transactions...");
		repeatingTransactionUpdater.updateRepeatingTransactions(DateHelper.getCurrentDate());

		LOGGER.debug("Importing database DONE");
		return importResultItems;
	}

	public InternalDatabase getDatabase()
	{
		return database;
	}

	public List<String> getCollectedErrorMessages()
	{
		return collectedErrorMessages;
	}

	private String formatErrorMessage(String errorMessage, Exception e)
	{
		return MessageFormat.format("{0}: {1} ({2})", errorMessage, e.getClass().getName(), e.getMessage());
	}

	private ImportResultItem importCategories()
	{
		List<Category> categories = database.getCategories();
		LOGGER.debug(MessageFormat.format("Importing {0} categories...", categories.size()));
		List<TransactionBase> alreadyUpdatedTransactions = new ArrayList<>();
		List<TransactionBase> alreadyUpdatedTemplates = new ArrayList<>();
		int numberOfImportedCategories = 0;

		for(Category category : categories)
		{
			LOGGER.debug(MessageFormat.format("Importing category {0}", category.getName()));

			try
			{
				int oldCategoryID = category.getID();
				int newCategoryID = importCategory(category);

				if(oldCategoryID == newCategoryID)
				{
					numberOfImportedCategories++;
					continue;
				}

				List<TransactionBase> transactions = new ArrayList<>(database.getTransactions());
				transactions.removeAll(alreadyUpdatedTransactions);
				alreadyUpdatedTransactions.addAll(updateCategoriesForItems(transactions, oldCategoryID, newCategoryID));

				List<TransactionBase> templates = new ArrayList<>(database.getTemplates());
				templates.removeAll(alreadyUpdatedTemplates);
				alreadyUpdatedTemplates.addAll(updateCategoriesForItems(templates, oldCategoryID, newCategoryID));

				numberOfImportedCategories++;
			}
			catch(Exception e)
			{
				final String errorMessage = MessageFormat.format("Error while importing category with name \"{0}\"", category.getName());
				LOGGER.error(errorMessage, e);
				collectedErrorMessages.add(formatErrorMessage(errorMessage, e));
			}
		}

		LOGGER.debug(MessageFormat.format("Importing categories DONE ({0}/{1})", numberOfImportedCategories, categories.size()));
		return new ImportResultItem(EntityType.CATEGORY, numberOfImportedCategories, categories.size());
	}

	private int importCategory(Category category)
	{
		Category existingCategory;
		if(category.getType().equals(CategoryType.NONE) || category.getType().equals(CategoryType.REST))
		{
			existingCategory = categoryRepository.findByType(category.getType());
		}
		else
		{
			existingCategory = categoryRepository.findByNameAndColorAndType(category.getName(), category.getColor(), category.getType());
		}

		int newCategoryID;
		if(existingCategory == null)
		{
			//category does not exist --> create it
			Category categoryToCreate = new Category(category.getName(), category.getColor(), category.getType(), category.getIconReference());
			categoryRepository.save(categoryToCreate);

			Category newCategory = categoryRepository.findByNameAndColorAndType(category.getName(), category.getColor(), category.getType());
			newCategoryID = newCategory.getID();
		}
		else
		{
			//category already exists
			newCategoryID = existingCategory.getID();
		}
		return newCategoryID;
	}

	public List<TransactionBase> updateCategoriesForItems(List<TransactionBase> items, int oldCategoryID, int newCategoryID)
	{
		List<TransactionBase> updatedItems = new ArrayList<>();
		for(TransactionBase item : items)
		{
			final Category category = item.getCategory();
			if(category == null)
			{
				continue;
			}

			if(category.getID() == oldCategoryID)
			{
				category.setID(newCategoryID);
				updatedItems.add(item);
			}
		}

		return updatedItems;
	}

	private ImportResultItem importAccounts(AccountMatchList accountMatchList)
	{
		LOGGER.debug(MessageFormat.format("Importing {0} accounts...", accountMatchList.getAccountMatches().size()));
		List<TransactionBase> alreadyUpdatedTransactions = new ArrayList<>();
		List<TransactionBase> alreadyUpdatedTransferTransactions = new ArrayList<>();
		List<TransactionBase> alreadyUpdatedTemplates = new ArrayList<>();
		List<TransactionBase> alreadyUpdatedTransferTemplates = new ArrayList<>();

		int numberOfImportedAccounts = 0;

		for(AccountMatch accountMatch : accountMatchList.getAccountMatches())
		{
			LOGGER.debug(MessageFormat.format("Importing account {0} -> {1}", accountMatch.getAccountSource().getName(), accountMatch.getAccountDestination().getName()));

			try
			{
				Account sourceAccount = database.getAccounts().stream()
						.filter(account -> account.getID().equals(accountMatch.getAccountSource().getID()))
						.findFirst()
						.orElseThrow();

				Account destinationAccount = accountRepository.findById(accountMatch.getAccountDestination().getID()).orElseThrow();

				Icon sourceIcon = sourceAccount.getIconReference();
				if(sourceIcon != null)
				{
					LOGGER.debug("Overwriting destination account icon");
					destinationAccount.setIconReference(sourceIcon);
					accountRepository.save(destinationAccount);
				}

				List<TransactionBase> transactions = new ArrayList<>(database.getTransactions());
				transactions.removeAll(alreadyUpdatedTransactions);
				alreadyUpdatedTransactions.addAll(updateAccountsForItems(transactions, accountMatch.getAccountSource().getID(), accountMatch.getAccountDestination()));

				List<TransactionBase> transferTransactions = new ArrayList<>(database.getTransactions());
				transferTransactions.removeAll(alreadyUpdatedTransferTransactions);
				alreadyUpdatedTransferTransactions.addAll(updateTransferAccountsForItems(transferTransactions, accountMatch.getAccountSource().getID(), accountMatch.getAccountDestination()));

				List<TransactionBase> templates = new ArrayList<>(database.getTemplates());
				templates.removeAll(alreadyUpdatedTemplates);
				alreadyUpdatedTemplates.addAll(updateAccountsForItems(templates, accountMatch.getAccountSource().getID(), accountMatch.getAccountDestination()));

				List<TransactionBase> transferTemplates = new ArrayList<>(database.getTemplates());
				transferTemplates.removeAll(alreadyUpdatedTransferTemplates);
				alreadyUpdatedTransferTemplates.addAll(updateTransferAccountsForItems(transferTemplates, accountMatch.getAccountSource().getID(), accountMatch.getAccountDestination()));

				numberOfImportedAccounts++;
			}
			catch(Exception e)
			{
				final String errorMessage = MessageFormat.format("Error while importing account with name \"{0}\"", accountMatch.getAccountSource().getName());
				LOGGER.error(errorMessage, e);
				collectedErrorMessages.add(formatErrorMessage(errorMessage, e));
			}
		}

		LOGGER.debug(MessageFormat.format("Importing accounts DONE ({0}/{1})", numberOfImportedAccounts, accountMatchList.getAccountMatches().size()));
		return new ImportResultItem(EntityType.ACCOUNT, numberOfImportedAccounts, accountMatchList.getAccountMatches().size());
	}

	public List<TransactionBase> updateAccountsForItems(List<TransactionBase> items, int oldAccountID, Account newAccount)
	{
		List<TransactionBase> updatedTransactions = new ArrayList<>();
		for(TransactionBase item : items)
		{
			// legacy database
			if(oldAccountID == -1)
			{
				item.setAccount(newAccount);
				updatedTransactions.add(item);
				continue;
			}

			if(item.getAccount() == null)
			{
				continue;
			}

			if(item.getAccount().getID() != oldAccountID)
			{
				continue;
			}

			// account needs to be updated
			item.setAccount(newAccount);
			updatedTransactions.add(item);
		}

		return updatedTransactions;
	}

	public List<TransactionBase> updateTransferAccountsForItems(List<TransactionBase> transactions, int oldAccountID, Account newAccount)
	{
		List<TransactionBase> updatedTransactions = new ArrayList<>();
		for(TransactionBase transaction : transactions)
		{
			if(transaction.getTransferAccount() != null && transaction.getTransferAccount().getID() == oldAccountID)
			{
				transaction.setTransferAccount(newAccount);
				updatedTransactions.add(transaction);
			}
		}

		return updatedTransactions;
	}

	private ImportResultItem importTransactions()
	{
		List<Transaction> transactions = database.getTransactions();
		LOGGER.debug(MessageFormat.format("Importing {0} transactions...", transactions.size()));

		int numberOfImportedTransactions = 0;

		for(int i = 0; i < transactions.size(); i++)
		{
			Transaction transaction = transactions.get(i);
			try
			{
				LOGGER.debug(MessageFormat.format("Importing transaction {0}/{1} (name: {2}, date: {3})", i + 1, transactions.size(), transaction.getName(), transaction.getDate()));
				updateTagsForItem(transaction);
				transaction.setID(null);
				transactionRepository.save(transaction);

				numberOfImportedTransactions++;
			}
			catch(Exception e)
			{
				final String errorMessage = MessageFormat.format("Error while importing transaction with name \"{0}\" from {1}", transaction.getName(), transaction.getDate().toString(DateFormatStyle.NORMAL.getKey()));
				LOGGER.error(errorMessage, e);
				collectedErrorMessages.add(formatErrorMessage(errorMessage, e));
			}
		}

		LOGGER.debug(MessageFormat.format("Importing transactions DONE ({0}/{1})", numberOfImportedTransactions, transactions.size()));
		return new ImportResultItem(EntityType.TRANSACTION, numberOfImportedTransactions, transactions.size());
	}

	public void updateTagsForItem(TransactionBase item)
	{
		List<Tag> tags = item.getTags();
		for(int i = 0; i < tags.size(); i++)
		{
			Tag currentTag = tags.get(i);
			Tag existingTag = tagRepository.findByName(currentTag.getName());
			if(existingTag == null)
			{
				final Tag newTag = tagRepository.save(new Tag(currentTag.getName()));
				tags.set(i, newTag);
			}
			else
			{
				tags.set(i, existingTag);
			}
		}
	}

	private ImportResultItem importTemplateGroups()
	{
		List<TemplateGroup> templateGroups = database.getTemplateGroups();
		LOGGER.debug(MessageFormat.format("Importing {0} template groups...", templateGroups.size()));
		List<Template> alreadyUpdatedTemplates = new ArrayList<>();
		int numberOfImportedTemplateGroups = 0;

		for(TemplateGroup templateGroup : templateGroups)
		{
			LOGGER.debug(MessageFormat.format("Importing template group {0}", templateGroup.getName()));

			try
			{
				int oldGroupID = templateGroup.getID();
				int newGroupID = importTemplateGroup(templateGroup);

				if(oldGroupID == newGroupID)
				{
					numberOfImportedTemplateGroups++;
					continue;
				}

				List<Template> templates = new ArrayList<>(database.getTemplates());
				templates.removeAll(alreadyUpdatedTemplates);
				alreadyUpdatedTemplates.addAll(updateTemplateGroupsForTemplates(templates, oldGroupID, newGroupID));

				numberOfImportedTemplateGroups++;
			}
			catch(Exception e)
			{
				final String errorMessage = MessageFormat.format("Error while importing template group with name \"{0}\"", templateGroup.getName());
				LOGGER.error(errorMessage, e);
				collectedErrorMessages.add(formatErrorMessage(errorMessage, e));
			}
		}

		LOGGER.debug(MessageFormat.format("Importing template groups DONE ({0}/{1})", numberOfImportedTemplateGroups, templateGroups.size()));
		return new ImportResultItem(EntityType.TEMPLATE_GROUP, numberOfImportedTemplateGroups, templateGroups.size());
	}

	private int importTemplateGroup(TemplateGroup templateGroup)
	{
		TemplateGroup existingTemplateGroup;
		if(templateGroup.getType().equals(TemplateGroupType.DEFAULT))
		{
			return templateGroupRepository.findFirstByType(templateGroup.getType()).getID();
		}
		else
		{
			existingTemplateGroup = templateGroupRepository.findByNameAndType(templateGroup.getName(), templateGroup.getType());
		}

		int newGroupID;
		if(existingTemplateGroup == null)
		{
			// template group does not exist --> create it
			TemplateGroup templateGroupToCreate = new TemplateGroup(templateGroup.getName(), templateGroup.getType());
			TemplateGroup savedTemplateGroup = templateGroupRepository.save(templateGroupToCreate);

			newGroupID = savedTemplateGroup.getID();
		}
		else
		{
			// template group already exists
			newGroupID = existingTemplateGroup.getID();
		}
		return newGroupID;
	}

	public List<Template> updateTemplateGroupsForTemplates(List<Template> items, int oldGroupID, int newGroupID)
	{
		List<Template> updatedItems = new ArrayList<>();
		for(Template item : items)
		{
			final TemplateGroup templateGroup = item.getTemplateGroup();
			if(templateGroup == null)
			{
				continue;
			}

			if(templateGroup.getID() == oldGroupID)
			{
				templateGroup.setID(newGroupID);
				updatedItems.add(item);
			}
		}

		return updatedItems;
	}

	private ImportResultItem importTemplates(Boolean importTemplateGroups)
	{
		List<Template> templates = database.getTemplates();
		LOGGER.debug(MessageFormat.format("Importing {0} templates...", templates.size()));

		int numberOfImportedTemplates = 0;

		for(int i = 0; i < templates.size(); i++)
		{
			Template template = templates.get(i);
			try
			{
				LOGGER.debug(MessageFormat.format("Importing template {0}/{1} (templateName: {2})", i + 1, templates.size(), template.getTemplateName()));
				updateTagsForItem(template);
				template.setID(null);

				if(!importTemplateGroups)
				{
					template.setTemplateGroup(null);
				}

				templateRepository.save(template);

				numberOfImportedTemplates++;
			}
			catch(Exception e)
			{
				final String errorMessage = MessageFormat.format("Error while importing template with name \"{0}\"", template.getTemplateName());
				LOGGER.error(errorMessage, e);
				collectedErrorMessages.add(formatErrorMessage(errorMessage, e));
			}
		}

		LOGGER.debug(MessageFormat.format("Importing templates DONE ({0}/{1})", numberOfImportedTemplates, templates.size()));
		return new ImportResultItem(EntityType.TEMPLATE, numberOfImportedTemplates, templates.size());
	}

	private ImportResultItem importCharts()
	{
		List<Chart> charts = database.getCharts();
		LOGGER.debug(MessageFormat.format("Importing {0} charts...", charts.size()));

		int numberOfImportedCharts = 0;

		for(int i = 0; i < charts.size(); i++)
		{
			Chart chart = charts.get(i);
			try
			{
				LOGGER.debug(MessageFormat.format("Importing chart {0}/{1} (name: {2})", i + 1, charts.size(), chart.getName()));

				final int highestUsedID = chartService.getHighestUsedID();
				chart.setID(highestUsedID + 1);

				chartService.getRepository().save(chart);

				numberOfImportedCharts++;
			}
			catch(Exception e)
			{
				final String errorMessage = MessageFormat.format("Error while importing chart with name \"{0}\"", chart.getName());
				LOGGER.error(errorMessage, e);
				collectedErrorMessages.add(formatErrorMessage(errorMessage, e));
			}
		}

		LOGGER.debug(MessageFormat.format("Importing charts DONE ({0}/{1})", numberOfImportedCharts, charts.size()));
		return new ImportResultItem(EntityType.CHART, numberOfImportedCharts, charts.size());

	}

	private ImportResultItem importImages()
	{
		List<Image> images = database.getImages();
		LOGGER.debug(MessageFormat.format("Importing {0} images...", images.size()));
		List<Icon> alreadyUpdatedIcons = new ArrayList<>();

		int numberOfImportedImages = 0;

		for(int i = 0; i < images.size(); i++)
		{
			Image image = images.get(i);
			try
			{
				LOGGER.debug(MessageFormat.format("Importing image {0}/{1} (ID: {2})", i + 1, images.size(), image.getID()));

				// always create new image
				int oldImageID = image.getID();
				Image imageToCreate = new Image(image.getImage(), image.getFileName(), image.getFileExtension());

				final Image savedImage = imageService.getRepository().save(imageToCreate);
				int newImageID = savedImage.getID();

				List<Icon> icons = new ArrayList<>(database.getIcons());
				icons.removeAll(alreadyUpdatedIcons);
				alreadyUpdatedIcons.addAll(updateImagesForIcons(icons, oldImageID, newImageID));

				numberOfImportedImages++;
			}
			catch(Exception e)
			{
				final String errorMessage = MessageFormat.format("Error while importing image with ID {0}", image.getID());
				LOGGER.error(errorMessage, e);
				collectedErrorMessages.add(formatErrorMessage(errorMessage, e));
			}
		}

		LOGGER.debug(MessageFormat.format("Importing images DONE ({0}/{1})", numberOfImportedImages, images.size()));
		return new ImportResultItem(EntityType.IMAGE, numberOfImportedImages, images.size());
	}

	public List<Icon> updateImagesForIcons(List<Icon> items, int oldImageId, int newImageID)
	{
		List<Icon> updatedItems = new ArrayList<>();
		for(Icon item : items)
		{
			final Image image = item.getImage();
			if(image == null)
			{
				continue;
			}

			if(image.getID() == oldImageId)
			{
				image.setID(newImageID);
				updatedItems.add(item);
			}
		}

		return updatedItems;
	}

	private void importIcons()
	{
		List<Icon> icons = database.getIcons();
		LOGGER.debug(MessageFormat.format("Importing {0} icons...", icons.size()));
		List<Iconizable> alreadyUpdatedAccounts = new ArrayList<>();
		List<Iconizable> alreadyUpdatedTemplates = new ArrayList<>();
		List<Iconizable> alreadyUpdatedCategories = new ArrayList<>();

		int numberOfImportedIcons = 0;

		for(int i = 0; i < icons.size(); i++)
		{
			Icon icon = icons.get(i);
			try
			{
				LOGGER.debug(MessageFormat.format("Importing icon {0}/{1} (ID: {2})", i + 1, icons.size(), icon.getID()));

				// always create new icon
				int oldIconID = icon.getID();
				Icon iconToCreate = new Icon();
				iconToCreate.setImage(icon.getImage());
				iconToCreate.setBuiltinIdentifier(icon.getBuiltinIdentifier());

				final Icon savedIcon = iconService.getRepository().save(iconToCreate);
				int newIconID = savedIcon.getID();

				List<Iconizable> accounts = new ArrayList<>(database.getAccounts());
				accounts.removeAll(alreadyUpdatedAccounts);
				alreadyUpdatedAccounts.addAll(updateIconsForItems(accounts, oldIconID, newIconID));

				List<Iconizable> templates = new ArrayList<>(database.getTemplates());
				templates.removeAll(alreadyUpdatedTemplates);
				alreadyUpdatedTemplates.addAll(updateIconsForItems(templates, oldIconID, newIconID));

				List<Iconizable> categories = new ArrayList<>(database.getCategories());
				categories.removeAll(alreadyUpdatedCategories);
				alreadyUpdatedCategories.addAll(updateIconsForItems(categories, oldIconID, newIconID));

				numberOfImportedIcons++;
			}
			catch(Exception e)
			{
				final String errorMessage = MessageFormat.format("Error while importing icon with ID {0}", icon.getID());
				LOGGER.error(errorMessage, e);
				collectedErrorMessages.add(formatErrorMessage(errorMessage, e));
			}
		}

		LOGGER.debug(MessageFormat.format("Importing icon DONE ({0}/{1})", numberOfImportedIcons, icons.size()));
	}

	public List<Iconizable> updateIconsForItems(List<? extends Iconizable> items, int oldIconID, int newIconID)
	{
		List<Iconizable> updatedItems = new ArrayList<>();
		for(Iconizable item : items)
		{
			final Icon iconReference = item.getIconReference();
			if(iconReference == null)
			{
				continue;
			}

			if(iconReference.getID() == oldIconID)
			{
				iconReference.setID(newIconID);
				updatedItems.add(item);
			}
		}

		return updatedItems;
	}
}
