package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.database.Database;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatch;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatchList;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.TagRepository;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionBase;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
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
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private final CategoryRepository categoryRepository;
	private final TransactionRepository transactionRepository;
	private final TemplateRepository templateRepository;
	private final TagRepository tagRepository;

	private Database database;

	@Autowired
	public ImportService(CategoryRepository categoryRepository, TransactionRepository transactionRepository, TemplateRepository templateRepository, TagRepository tagRepository)
	{
		this.categoryRepository = categoryRepository;
		this.transactionRepository = transactionRepository;
		this.templateRepository = templateRepository;
		this.tagRepository = tagRepository;
	}

	public void importDatabase(Database database, AccountMatchList accountMatchList)
	{
		this.database = database;
		LOGGER.debug("Importing database...");
		importCategories();
		importAccounts(accountMatchList);
		importTransactions();
		importTemplates();
		LOGGER.debug("Importing database DONE");
	}

	public Database getDatabase()
	{
		return database;
	}

	private void importCategories()
	{
		List<Category> categories = database.getCategories();
		LOGGER.debug(MessageFormat.format("Importing {0} categories...", categories.size()));
		List<TransactionBase> alreadyUpdatedTransactions = new ArrayList<>();
		List<TransactionBase> alreadyUpdatedTemplates = new ArrayList<>();

		for(Category category : categories)
		{
			LOGGER.debug(MessageFormat.format("Importing category {0}", category.getName()));
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
				category.setID(null);
				categoryRepository.save(category);

				Category newCategory = categoryRepository.findByNameAndColorAndType(category.getName(), category.getColor(), category.getType());
				newCategoryID = newCategory.getID();
			}
			else
			{
				//category already exists
				newCategoryID = existingCategory.getID();
			}

			int oldCategoryID = category.getID();

			if(oldCategoryID == newCategoryID)
			{
				continue;
			}

			List<TransactionBase> transactions = new ArrayList<>(database.getTransactions());
			transactions.removeAll(alreadyUpdatedTransactions);
			alreadyUpdatedTransactions.addAll(updateCategoriesForItems(transactions, oldCategoryID, newCategoryID));

			List<TransactionBase> templates = new ArrayList<>(database.getTemplates());
			templates.removeAll(alreadyUpdatedTemplates);
			alreadyUpdatedTemplates.addAll(updateCategoriesForItems(templates, oldCategoryID, newCategoryID));
		}

		LOGGER.debug("Importing categories DONE");
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

	private void importAccounts(AccountMatchList accountMatchList)
	{
		LOGGER.debug(MessageFormat.format("Importing {0} accounts...", accountMatchList.getAccountMatches().size()));
		List<TransactionBase> alreadyUpdatedTransactions = new ArrayList<>();
		List<TransactionBase> alreadyUpdatedTransferTransactions = new ArrayList<>();
		List<TransactionBase> alreadyUpdatedTemplates = new ArrayList<>();

		for(AccountMatch accountMatch : accountMatchList.getAccountMatches())
		{
			LOGGER.debug(MessageFormat.format("Importing account {0} -> {1}", accountMatch.getAccountSource().getName(), accountMatch.getAccountDestination().getName()));

			List<TransactionBase> transactions = new ArrayList<>(database.getTransactions());
			transactions.removeAll(alreadyUpdatedTransactions);
			alreadyUpdatedTransactions.addAll(updateAccountsForItems(transactions, accountMatch.getAccountSource().getID(), accountMatch.getAccountDestination()));

			List<TransactionBase> transferTransactions = new ArrayList<>(database.getTransactions());
			transferTransactions.removeAll(alreadyUpdatedTransferTransactions);
			alreadyUpdatedTransferTransactions.addAll(updateTransferAccountsForTransactions(transferTransactions, accountMatch.getAccountSource().getID(), accountMatch.getAccountDestination()));

			List<TransactionBase> templates = new ArrayList<>(database.getTemplates());
			transactions.removeAll(alreadyUpdatedTemplates);
			alreadyUpdatedTemplates.addAll(updateAccountsForItems(templates, accountMatch.getAccountSource().getID(), accountMatch.getAccountDestination()));
		}

		LOGGER.debug("Importing accounts DONE");
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

	public List<TransactionBase> updateTransferAccountsForTransactions(List<TransactionBase> transactions, int oldAccountID, Account newAccount)
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

	private void importTransactions()
	{
		List<Transaction> transactions = database.getTransactions();
		LOGGER.debug(MessageFormat.format("Importing {0} transactions...", transactions.size()));
		for(int i = 0; i < transactions.size(); i++)
		{
			Transaction transaction = transactions.get(i);
			LOGGER.debug(MessageFormat.format("Importing transaction {0}/{1} (name: {2}, date: {3})", i + 1, transactions.size(), transaction.getName(), transaction.getDate()));
			updateTagsForItem(transaction);
			transaction.setID(null);
			transactionRepository.save(transaction);
		}
		LOGGER.debug("Importing transactions DONE");
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

	private void importTemplates()
	{
		List<Template> templates = database.getTemplates();
		LOGGER.debug(MessageFormat.format("Importing {0} templates...", templates.size()));
		for(int i = 0; i < templates.size(); i++)
		{
			Template template = templates.get(i);
			LOGGER.debug(new StringBuilder().append("Importing template ").append(i + 1).append("/").append(templates.size()).append(" (templateName: ").append(template.getTemplateName()).append(")").toString());
			updateTagsForItem(template);
			template.setID(null);
			templateRepository.save(template);
		}
		LOGGER.debug("Importing transactions DONE");
	}
}
