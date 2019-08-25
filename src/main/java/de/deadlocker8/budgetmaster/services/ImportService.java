package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.database.Database;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatch;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatchList;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.tags.TagRepository;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImportService
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private TagRepository tagRepository;

	@Autowired
	public ImportService(CategoryRepository categoryRepository, TransactionRepository transactionRepository, TagRepository tagRepository)
	{
		this.categoryRepository = categoryRepository;
		this.transactionRepository = transactionRepository;
		this.tagRepository = tagRepository;
	}

	public void importDatabase(Database database, AccountMatchList accountMatchList)
	{
		LOGGER.debug("Importing database...");
		importCategories(database);
		importAccounts(database, accountMatchList);
		importTransactions(database);
		LOGGER.debug("Importing database DONE");
	}

	private void importCategories(Database database)
	{
		List<Category> categories = database.getCategories();
		LOGGER.debug("Importing " + categories.size() + " categories...");
		List<Transaction> alreadyUpdatedTransactions = new ArrayList<>();

		for(Category category : categories)
		{
			LOGGER.debug("Importing category " + category.getName());
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

			List<Transaction> transactions = new ArrayList<>(database.getTransactions());
			transactions.removeAll(alreadyUpdatedTransactions);

			alreadyUpdatedTransactions.addAll(updateCategoriesForTransactions(transactions, oldCategoryID, newCategoryID));
		}

		LOGGER.debug("Importing categories DONE");
	}

	public List<Transaction> updateCategoriesForTransactions(List<Transaction> transactions, int oldCategoryID, int newCategoryID)
	{
		List<Transaction> updatedTransactions = new ArrayList<>();
		for(Transaction transaction : transactions)
		{
			if(transaction.getCategory().getID() == oldCategoryID)
			{
				transaction.getCategory().setID(newCategoryID);
				updatedTransactions.add(transaction);
			}
		}

		return updatedTransactions;
	}

	private void importAccounts(Database database, AccountMatchList accountMatchList)
	{
		LOGGER.debug("Importing " + accountMatchList.getAccountMatches().size() + " accounts...");
		List<Transaction> alreadyUpdatedTransactions = new ArrayList<>();
		List<Transaction> alreadyUpdatedTransferTransactions = new ArrayList<>();
		for(AccountMatch accountMatch : accountMatchList.getAccountMatches())
		{
			LOGGER.debug("Importing account " + accountMatch.getAccountSource().getName() + " -> " + accountMatch.getAccountDestination().getName());

			List<Transaction> transactions = new ArrayList<>(database.getTransactions());
			transactions.removeAll(alreadyUpdatedTransactions);

			List<Transaction> transferTransactions = new ArrayList<>(database.getTransactions());
			transferTransactions.removeAll(alreadyUpdatedTransferTransactions);

			alreadyUpdatedTransactions.addAll(updateAccountsForTransactions(transactions, accountMatch.getAccountSource().getID(), accountMatch.getAccountDestination().getID()));
			alreadyUpdatedTransferTransactions.addAll(updateTransferAccountsForTransactions(transferTransactions, accountMatch.getAccountSource().getID(), accountMatch.getAccountDestination().getID()));
		}
		LOGGER.debug("Importing accounts DONE");
	}

	private List<Transaction> updateAccountsForTransactions(List<Transaction> transactions, int oldAccountID, int newAccountID)
	{
		List<Transaction> updatedTransactions = new ArrayList<>();
		for(Transaction transaction : transactions)
		{
			// legacy database
			if(oldAccountID == -1)
			{
				transaction.getAccount().setID(newAccountID);
				updatedTransactions.add(transaction);
			}
			else if(transaction.getAccount().getID() == oldAccountID)
			{
				transaction.getAccount().setID(newAccountID);
				updatedTransactions.add(transaction);
			}
		}

		return updatedTransactions;
	}

	private List<Transaction> updateTransferAccountsForTransactions(List<Transaction> transactions, int oldAccountID, int newAccountID)
	{
		List<Transaction> updatedTransactions = new ArrayList<>();
		for(Transaction transaction : transactions)
		{
			if(transaction.getTransferAccount() != null && transaction.getTransferAccount().getID() == oldAccountID)
			{
				transaction.getTransferAccount().setID(newAccountID);
				updatedTransactions.add(transaction);
			}
		}

		return updatedTransactions;
	}

	private void importTransactions(Database database)
	{
		List<Transaction> transactions = database.getTransactions();
		LOGGER.debug("Importing " + transactions.size() + " transactions...");
		for(int i = 0; i < transactions.size(); i++)
		{
			Transaction transaction = transactions.get(i);
			LOGGER.debug("Importing transaction " + (i+1) + "/" + transactions.size() + " (name: " + transaction.getName() + ", date: " + transaction.getDate() + ")");
			updateTagsForTransaction(transaction);
			transaction.setID(null);
			transactionRepository.save(transaction);
		}
		LOGGER.debug("Importing transactions DONE");
	}

	private void updateTagsForTransaction(Transaction transaction)
	{
		List<Tag> tags = transaction.getTags();
		for(int i = 0; i < tags.size(); i++)
		{
			Tag currentTag = tags.get(i);
			Tag existingTag = tagRepository.findByName(currentTag.getName());
			if(existingTag == null)
			{
				tagRepository.save(new Tag(currentTag.getName()));
				tags.set(i, tagRepository.findByName(currentTag.getName()));
			}
			else
			{
				tags.set(i, existingTag);
			}
		}
	}
}
