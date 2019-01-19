package de.deadlocker8.budgetmaster.services;

import com.google.gson.*;
import de.deadlocker8.budgetmaster.database.Database;
import de.deadlocker8.budgetmaster.entities.account.Account;
import de.deadlocker8.budgetmaster.entities.category.Category;
import de.deadlocker8.budgetmaster.entities.Transaction;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseService
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private AccountService accountService;
	private CategoryService categoryService;
	private TransactionService transactionService;
	private TagService tagService;

	@Autowired
	public DatabaseService(AccountService accountService, CategoryService categoryService, TransactionService transactionService, TagService tagService)
	{
		this.accountService = accountService;
		this.categoryService = categoryService;
		this.transactionService = transactionService;
		this.tagService = tagService;
	}

	public void reset()
	{
		resetTransactions();
		resetCategories();
		resetAccounts();
		resetTags();
	}

	private void resetAccounts()
	{
		LOGGER.info("Resetting accounts...");
		accountService.deleteAll();
		accountService.createDefaults();
		LOGGER.info("All accounts reset.");
	}

	private void resetCategories()
	{
		LOGGER.info("Resetting categories...");
		categoryService.deleteAll();
		categoryService.createDefaults();
		LOGGER.info("All categories reset.");
	}

	private void resetTransactions()
	{
		LOGGER.info("Resetting transactions...");
		transactionService.deleteAll();
		transactionService.createDefaults();
		LOGGER.info("All transactions reset.");
	}

	private void resetTags()
	{
		LOGGER.info("Resetting tags...");
		tagService.deleteAll();
		tagService.createDefaults();
		LOGGER.info("All tags reset.");
	}

	public String getDatabaseAsJSON()
	{
		List<Category> categories = categoryService.getRepository().findAll();
		List<Account> accounts = accountService.getRepository().findAll();
		List<Transaction> transactions = transactionService.getRepository().findAll();
		List<Transaction> filteredTransactions = filterRepeatingTransactions(transactions);
		LOGGER.debug("Reduced " + transactions.size() + " transactions to " + filteredTransactions.size());

		Database database = new Database(categories, accounts, filteredTransactions);
		LOGGER.debug("Created database JSON with " + database.getTransactions().size() + " transactions, " + database.getCategories().size() + " categories and " + database.getAccounts().size() + " accounts");

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().registerTypeAdapter(DateTime.class, new JsonSerializer<DateTime>(){
			@Override
			public JsonElement serialize(DateTime json, Type typeOfSrc, JsonSerializationContext context) {
				return new JsonPrimitive(ISODateTimeFormat.date().print(json));
			}
		}).create();
		return gson.toJson(database);
	}

	private List<Transaction> filterRepeatingTransactions(List<Transaction> transactions)
	{
		List<Transaction> filteredTransactions = new ArrayList<>();

		for(Transaction transaction : transactions)
		{
			if(transaction.isRepeating())
			{
				if(isRepeatingOptionInList(transaction.getRepeatingOption(), filteredTransactions))
				{
					continue;
				}
			}

			filteredTransactions.add(transaction);
		}

		return filteredTransactions;
	}

	private boolean isRepeatingOptionInList(RepeatingOption repeatingOption, List<Transaction> transactions)
	{
		for(Transaction transaction : transactions)
		{
			if(transaction.isRepeating())
			{
				if(transaction.getRepeatingOption().equals(repeatingOption))
				{
					return true;
				}
			}
		}
		return false;
	}
}
