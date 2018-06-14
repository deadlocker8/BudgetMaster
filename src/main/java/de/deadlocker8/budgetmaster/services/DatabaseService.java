package de.deadlocker8.budgetmaster.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private AccountService accountService;
	private CategoryService categoryService;
	private PaymentService paymentService;

	@Autowired
	public DatabaseService(AccountService accountService, CategoryService categoryService, PaymentService paymentService)
	{
		this.accountService = accountService;
		this.categoryService = categoryService;
		this.paymentService = paymentService;
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
		paymentService.deleteAll();
		paymentService.createDefaults();
		LOGGER.info("All tags reset.");
	}
}
