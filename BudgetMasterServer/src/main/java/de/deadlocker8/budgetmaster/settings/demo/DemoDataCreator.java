package de.deadlocker8.budgetmaster.settings.demo;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import java.security.SecureRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Component
public class DemoDataCreator
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DemoDataCreator.class);

	private final CategoryService categoryService;
	private final IconService iconService;
	private final AccountService accountService;
	private final TransactionService transactionService;

	private final Random random;

	private Category categorySalary;

	@Autowired
	public DemoDataCreator(CategoryService categoryService, IconService iconService, AccountService accountService, TransactionService transactionService)
	{
		this.categoryService = categoryService;
		this.iconService = iconService;
		this.accountService = accountService;
		this.transactionService = transactionService;

		this.random = new SecureRandom();
	}

	public void createDemoData()
	{
		LOGGER.debug("Creating demo data...");

		LOGGER.debug("Creating demo accounts...");
		createDemoAccounts();

		LOGGER.debug("Creating demo categories...");
		createDemoCategories();

		LOGGER.debug("Creating demo transactions...");
		createMultipleDemoTransaction();

		LOGGER.debug("Demo data created");
	}

	private void createDemoAccounts()
	{
		final Icon icon = iconService.createIconReference(null, null, null);
		iconService.getRepository().save(icon);
		final Account oldAccount = new Account("Old account", AccountType.CUSTOM, icon);
		oldAccount.setAccountState(AccountState.HIDDEN);
		accountService.getRepository().save(oldAccount);

		final Icon icon2 = iconService.createIconReference(null, null, null);
		iconService.getRepository().save(icon2);
		final Account readOnlyAccount = new Account("Read-only account", AccountType.CUSTOM, icon2);
		readOnlyAccount.setAccountState(AccountState.READ_ONLY);
		accountService.getRepository().save(readOnlyAccount);

		final Icon icon3 = iconService.createIconReference(null, "fas fa-piggy-bank", null);
		iconService.getRepository().save(icon3);
		final Account savingsBankAccount = new Account("Savings Bank", AccountType.CUSTOM, icon3);
		accountService.getRepository().save(savingsBankAccount);
	}

	private void createDemoCategories()
	{
		final Icon icon = iconService.createIconReference(null, "fas fa-car", null);
		iconService.getRepository().save(icon);
		categoryService.save(new Category("Car", "#007afa", CategoryType.CUSTOM, icon));

		final Icon icon2 = iconService.createIconReference(null, "fas fa-money-bill-alt", null);
		iconService.getRepository().save(icon2);
		categoryService.save(new Category("Cash", "#888888", CategoryType.CUSTOM, icon2));

		final Icon icon3 = iconService.createIconReference(null, null, null);
		iconService.getRepository().save(icon3);
		categoryService.save(new Category("Electricity/Gas", "#888888", CategoryType.CUSTOM, icon3));

		final Icon icon4 = iconService.createIconReference(null, null, null);
		iconService.getRepository().save(icon4);
		categoryService.save(new Category("Electronics", "#ff9500", CategoryType.CUSTOM, icon4));

		final Icon icon5 = iconService.createIconReference(null, null, null);
		iconService.getRepository().save(icon5);
		categoryService.save(new Category("Misc", "#333333", CategoryType.CUSTOM, icon5));

		final Icon icon6 = iconService.createIconReference(null, "fas fa-mobile-alt", null);
		iconService.getRepository().save(icon6);
		categoryService.save(new Category("Mobile Phone/Internet", "#5ac8fa", CategoryType.CUSTOM, icon6));

		final Icon icon7 = iconService.createIconReference(null, "fas fa-home", null);
		iconService.getRepository().save(icon7);
		categoryService.save(new Category("Rent", "#9b59b6", CategoryType.CUSTOM, icon7));

		final Icon icon8 = iconService.createIconReference(null, "fas fa-coins", null);
		iconService.getRepository().save(icon8);
		categorySalary = categoryService.save(new Category("Salary", "#4cd964", CategoryType.CUSTOM, icon8));
	}

	private void createMultipleDemoTransaction()
	{
		final LocalDate now = LocalDate.now();

		for(int k = 0; k < 3; k++)
		{
			final LocalDate date = now.minusMonths(k);

			for(int i = 0; i < 20; i++)
			{
				createSingleDemoTransaction(date);
			}

			final Transaction transaction = new Transaction();
			transaction.setName("Salary");
			transaction.setAmount(getRandomNumber(2000 * 100, 4000 * 100));
			transaction.setIsExpenditure(false);
			transaction.setCategory(categorySalary);
			transaction.setDate(date.withDayOfMonth(1));
			transaction.setDescription("");
			transaction.setTags(new ArrayList<>());
			transaction.setAccount(accountService.getAllActivatedAccountsAsc().get(1));
			transaction.setTransferAccount(null);
			transaction.setRepeatingOption(null);

			transactionService.getRepository().save(transaction);
		}
	}

	private void createSingleDemoTransaction(LocalDate date)
	{
		final Transaction transaction = new Transaction();
		transaction.setName(getRandomTransactionName());
		transaction.setAmount(-getRandomNumber(100, 250 * 100));
		transaction.setIsExpenditure(true);
		transaction.setCategory(getRandomCategory());
		transaction.setDate(getRandomDateInMonth(date));
		transaction.setDescription(getRandomTransactionDescription());
		transaction.setTags(new ArrayList<>());
		transaction.setAccount(accountService.getAllActivatedAccountsAsc().get(1));
		transaction.setTransferAccount(null);
		transaction.setRepeatingOption(null);

		LOGGER.debug(MessageFormat.format("Creating demo transaction: {0}", transaction));

		transactionService.getRepository().save(transaction);
	}

	private String getRandomTransactionName()
	{
		final List<String> names = List.of("Electricity", "Internet and Phone", "Pizza", "Pizza", "Bread", "Fuel", "Fuel", "Rent", "Train Ticket", "Train Ticket", "Train Ticket", "Train Ticket", "Train Ticket", "Coffee", "Coffee", "Coffee", "Coffee", "Groceries");
		return names.get(getRandomNumber(0, names.size()));
	}

	private String getRandomTransactionDescription()
	{
		final List<String> names = List.of("Lorem Ipsum", "dolor sit amet", "the best in town", "", "", "", "");
		return names.get(getRandomNumber(0, names.size()));
	}

	private Category getRandomCategory()
	{
		final List<Category> categories = categoryService.getAllCustomCategories();
		return categories.get(getRandomNumber(0, categories.size()));
	}

	private int getRandomNumber(int min, int max)
	{
		return random.nextInt(min, max);
	}

	private LocalDate getRandomDateInMonth(LocalDate date)
	{
		final LocalDate firstDayInMonth = date.with(firstDayOfMonth());
		final int maxDayNumber = date.with(lastDayOfMonth()).getDayOfMonth();

		return firstDayInMonth.withDayOfMonth(getRandomNumber(1, maxDayNumber));
	}
}
