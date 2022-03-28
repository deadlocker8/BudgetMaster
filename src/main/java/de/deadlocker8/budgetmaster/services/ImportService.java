package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.charts.ChartService;
import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatch;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatchList;
import de.deadlocker8.budgetmaster.database.importer.*;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconRepository;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.icon.Iconizable;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageRepository;
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
import java.time.format.DateTimeFormatter;
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
	private final ImageRepository imageRepository;
	private final RepeatingTransactionUpdater repeatingTransactionUpdater;
	private final AccountRepository accountRepository;
	private final IconRepository iconRepository;


	private InternalDatabase database;
	private List<String> collectedErrorMessages;

	@Autowired
	public ImportService(CategoryRepository categoryRepository, TransactionRepository transactionRepository, TemplateGroupRepository templateGroupRepository, TemplateRepository templateRepository,
						 TagRepository tagRepository, ChartService chartService, ImageRepository imageRepository, RepeatingTransactionUpdater repeatingTransactionUpdater, AccountRepository accountRepository, IconRepository iconRepository)
	{
		this.categoryRepository = categoryRepository;
		this.transactionRepository = transactionRepository;
		this.templateGroupRepository = templateGroupRepository;
		this.templateRepository = templateRepository;
		this.tagRepository = tagRepository;
		this.chartService = chartService;
		this.imageRepository = imageRepository;
		this.repeatingTransactionUpdater = repeatingTransactionUpdater;
		this.accountRepository = accountRepository;
		this.iconRepository = iconRepository;
	}

	public List<ImportResultItem> importDatabase(InternalDatabase database, AccountMatchList accountMatchList, Boolean importTemplateGroups, Boolean importTemplates, Boolean importCharts)
	{
		this.database = database;
		this.collectedErrorMessages = new ArrayList<>();

		final List<ImportResultItem> importResultItems = new ArrayList<>();

		LOGGER.debug("Importing database...");
		importResultItems.add(new ImageImporter(imageRepository).importItems(database.getImages()));
		new IconImporter(iconRepository).importItems(database.getIcons());
		importResultItems.add(new CategoryImporter(categoryRepository).importItems(database.getCategories()));
		importResultItems.add(new AccountImporter(accountRepository).importItems(database.getAccounts(), accountMatchList));
		importResultItems.add(importTransactions());

		if(importTemplateGroups)
		{
			importResultItems.add(importTemplateGroups());
		}
		else
		{
			importResultItems.add(new ImportResultItem(EntityType.TEMPLATE_GROUP, 0, 0, collectedErrorMessages));
		}

		if(importTemplates)
		{
			importResultItems.add(importTemplates(importTemplateGroups));
		}
		else
		{
			importResultItems.add(new ImportResultItem(EntityType.TEMPLATE, 0, 0, collectedErrorMessages));
		}

		if(importCharts)
		{
			importResultItems.add(new ChartImporter(chartService).importItems(database.getCharts()));
		}
		else
		{
			importResultItems.add(new ImportResultItem(EntityType.CHART, 0, 0, collectedErrorMessages));
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
				final String errorMessage = MessageFormat.format("Error while importing transaction with name \"{0}\" from {1}", transaction.getName(), transaction.getDate().format(DateTimeFormatter.ofPattern(DateFormatStyle.NORMAL.getKey())));
				LOGGER.error(errorMessage, e);
				collectedErrorMessages.add(formatErrorMessage(errorMessage, e));
			}
		}

		LOGGER.debug(MessageFormat.format("Importing transactions DONE ({0}/{1})", numberOfImportedTransactions, transactions.size()));
		return new ImportResultItem(EntityType.TRANSACTION, numberOfImportedTransactions, transactions.size(), collectedErrorMessages);
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
		return new ImportResultItem(EntityType.TEMPLATE_GROUP, numberOfImportedTemplateGroups, templateGroups.size(), collectedErrorMessages);
	}

	private int importTemplateGroup(TemplateGroup templateGroup)
	{
		TemplateGroup existingTemplateGroup;
		if(templateGroup.getType().equals(TemplateGroupType.DEFAULT))
		{
			existingTemplateGroup = templateGroupRepository.findFirstByType(TemplateGroupType.DEFAULT);
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

				if(!importTemplateGroups || template.getTemplateGroup() == null)
				{
					template.setTemplateGroup(templateGroupRepository.findFirstByType(TemplateGroupType.DEFAULT));
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
		return new ImportResultItem(EntityType.TEMPLATE, numberOfImportedTemplates, templates.size(), collectedErrorMessages);
	}
}
