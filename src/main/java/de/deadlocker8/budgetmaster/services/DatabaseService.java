package de.deadlocker8.budgetmaster.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.entities.Account;
import de.deadlocker8.budgetmaster.entities.Category;
import de.deadlocker8.budgetmaster.entities.Payment;
import de.deadlocker8.budgetmaster.entities.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private AccountService accountService;
	private CategoryService categoryService;
	private PaymentService paymentService;
	private TagService tagService;

	@Autowired
	public DatabaseService(AccountService accountService, CategoryService categoryService, PaymentService paymentService, TagService tagService)
	{
		this.accountService = accountService;
		this.categoryService = categoryService;
		this.paymentService = paymentService;
		this.tagService = tagService;
	}

	public void reset()
	{
		resetPayments();
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

	private void resetPayments()
	{
		LOGGER.info("Resetting payments...");
		paymentService.deleteAll();
		paymentService.createDefaults();
		LOGGER.info("All payments reset.");
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
		List<Payment> payments = paymentService.getRepository().findAll();

		Database database = new Database(categories, accounts, payments);

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
		return gson.toJson(database);
	}
}
