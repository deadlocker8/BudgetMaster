package de.deadlocker8.budgetmaster.services;

import com.google.gson.*;
import de.deadlocker8.budgetmaster.database.Database;
import de.deadlocker8.budgetmaster.entities.Account;
import de.deadlocker8.budgetmaster.entities.Category;
import de.deadlocker8.budgetmaster.entities.Transaction;
import de.tobias.logger.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class DatabaseService
{
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
		Logger.info("Resetting accounts...");
		accountService.deleteAll();
		accountService.createDefaults();
		Logger.info("All accounts reset.");
	}

	private void resetCategories()
	{
		Logger.info("Resetting categories...");
		categoryService.deleteAll();
		categoryService.createDefaults();
		Logger.info("All categories reset.");
	}

	private void resetTransactions()
	{
		Logger.info("Resetting transactions...");
		transactionService.deleteAll();
		transactionService.createDefaults();
		Logger.info("All transactions reset.");
	}

	private void resetTags()
	{
		Logger.info("Resetting tags...");
		tagService.deleteAll();
		tagService.createDefaults();
		Logger.info("All tags reset.");
	}

	public String getDatabaseAsJSON()
	{
		List<Category> categories = categoryService.getRepository().findAll();
		List<Account> accounts = accountService.getRepository().findAll();
		List<Transaction> transactions = transactionService.getRepository().findAll();

		Database database = new Database(categories, accounts, transactions);

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().registerTypeAdapter(DateTime.class, new JsonSerializer<DateTime>(){
			@Override
			public JsonElement serialize(DateTime json, Type typeOfSrc, JsonSerializationContext context) {
				return new JsonPrimitive(ISODateTimeFormat.date().print(json));
			}
		}).create();
		return gson.toJson(database);
	}
}
