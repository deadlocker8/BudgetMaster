package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.BudgetMasterServerMain;
import de.deadlocker8.budgetmaster.TestConstants;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.RepeatingTransactionUpdater;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndAfterXTimes;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierDays;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.utils.DateHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BudgetMasterServerMain.class)
@ActiveProfiles("test")
@Transactional
@Testcontainers
class TransactionServiceDatabaseTest
{
	@Container
	static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>(TestConstants.POSTGRES_VERSION)
			.withDatabaseName("budgetmaster-tests-db")
			.withUsername("budgetmaster")
			.withPassword("BudgetMaster");

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry)
	{
		registry.add("spring.datasource.url", postgresDB::getJdbcUrl);
		registry.add("spring.datasource.username", postgresDB::getUsername);
		registry.add("spring.datasource.password", postgresDB::getPassword);
	}

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private RepeatingTransactionUpdater repeatingTransactionUpdater;

	private Transaction transactionTransfer;
	private Transaction transactionNormal;
	private Transaction transactionForSecondAccount;
	private Transaction transactionRepeating;
	private Transaction transactionLastDayOfMay;
	private Transaction transactionLastDayOfJune;

	@BeforeEach
	void beforeEach()
	{
		Account secondAccount = new Account("Second Account", AccountType.CUSTOM);
		secondAccount = accountRepository.save(secondAccount);

		Category category1 = new Category("Regen", "#ffcc00", CategoryType.CUSTOM);
		category1 = categoryService.save(category1);

		Category category2 = new Category("Baumhaus", "#007afa", CategoryType.CUSTOM);
		category2 = categoryService.save(category2);

		transactionForSecondAccount = new Transaction();
		transactionForSecondAccount.setName("transaction from account 2");
		transactionForSecondAccount.setAmount(-2233);
		transactionForSecondAccount.setDate(LocalDate.of(2020, 5, 1));
		transactionForSecondAccount.setCategory(category1);
		transactionForSecondAccount.setAccount(secondAccount);
		transactionForSecondAccount.setTags(List.of());
		transactionForSecondAccount = transactionService.getRepository().save(transactionForSecondAccount);

		transactionRepeating = new Transaction();
		transactionRepeating.setName("repeating");
		transactionRepeating.setAmount(-100);
		final LocalDate date = LocalDate.of(2020, 5, 19);
		transactionRepeating.setDate(date);
		transactionRepeating.setCategory(categoryService.findByType(CategoryType.NONE));
		final RepeatingOption repeatingOption = new RepeatingOption(date, new RepeatingModifierDays(1), new RepeatingEndAfterXTimes(3));
		transactionRepeating.setRepeatingOption(repeatingOption);
		transactionRepeating.setAccount(accountRepository.findByIsDefault(true));
		transactionRepeating.setTags(List.of());
		transactionRepeating = transactionService.getRepository().save(transactionRepeating);

		transactionNormal = new Transaction();
		transactionNormal.setName("normal transaction");
		transactionNormal.setAmount(1000000);
		transactionNormal.setDate(LocalDate.of(2020, 5, 30));
		transactionNormal.setCategory(category2);
		transactionNormal.setAccount(accountRepository.findByIsDefault(true));
		transactionNormal.setTags(List.of());
		transactionNormal = transactionService.getRepository().save(transactionNormal);

		transactionLastDayOfMay = new Transaction();
		transactionLastDayOfMay.setName("last day of may");
		transactionLastDayOfMay.setAmount(-100);
		transactionLastDayOfMay.setDate(LocalDate.of(2021, 5, 31));
		transactionLastDayOfMay.setCategory(categoryService.findByType(CategoryType.NONE));
		transactionLastDayOfMay.setAccount(accountRepository.findByIsDefault(true));
		transactionLastDayOfMay.setTags(List.of());
		transactionLastDayOfMay = transactionService.getRepository().save(transactionLastDayOfMay);

		transactionLastDayOfJune = new Transaction();
		transactionLastDayOfJune.setName("last day of june");
		transactionLastDayOfJune.setAmount(-100);
		transactionLastDayOfJune.setDate(LocalDate.of(2021, 6, 30));
		transactionLastDayOfJune.setCategory(categoryService.findByType(CategoryType.NONE));
		transactionLastDayOfJune.setAccount(accountRepository.findByIsDefault(true));
		transactionLastDayOfJune.setTags(List.of());
		transactionLastDayOfJune = transactionService.getRepository().save(transactionLastDayOfJune);

		transactionTransfer = new Transaction();
		transactionTransfer.setName("My transfer 0815");
		transactionTransfer.setAmount(-1200);
		transactionTransfer.setDate(LocalDate.of(2021, 5, 30));
		transactionTransfer.setCategory(category1);
		transactionTransfer.setAccount(accountRepository.findByIsDefault(true));
		transactionTransfer.setTransferAccount(secondAccount);
		transactionTransfer.setTags(List.of());
		transactionTransfer = transactionService.getRepository().save(transactionTransfer);

		repeatingTransactionUpdater.updateRepeatingTransactions(LocalDate.now());
	}

	@Test
	void test_getTransactionsForAccount_specificAccount()
	{
		LocalDate date1 = LocalDate.of(2020, 4, 30);
		FilterConfiguration filterConfiguration = new FilterConfiguration(true, true, true, true, true, null, null, "");

		List<Transaction> transactions = transactionService.getTransactionsForAccount(accountRepository.findByName("Second Account"), date1, LocalDate.now(), filterConfiguration);
		assertThat(transactions)
				.hasSize(2)
				.containsExactly(transactionTransfer, transactionForSecondAccount);
	}

	@Test
	void test_getTransactionsForAccount_all()
	{
		LocalDate date1 = LocalDate.of(2020, 4, 30);
		FilterConfiguration filterConfiguration = new FilterConfiguration(true, true, true, true, true, null, null, "");

		List<Transaction> transactions = transactionService.getTransactionsForAccount(accountRepository.findAllByType(AccountType.ALL).get(0), date1, LocalDate.now(), filterConfiguration);
		assertThat(transactions).hasSize(8);
	}

	@Test
	void test_getTransactionsForAccountUntilDate()
	{
		LocalDate date1 = LocalDate.of(2020, 4, 30);
		LocalDate date2 = LocalDate.of(2020, 5, 20);
		FilterConfiguration filterConfiguration = new FilterConfiguration(true, true, true, true, true, null, null, "");

		List<Transaction> transactions = transactionService.getTransactionsForAccount(accountRepository.findByName("Default Account"), date1, date2, filterConfiguration);
		assertThat(transactions).hasSize(2);
	}

	@Test
	void test_getTransactionsForMonthAndYear()
	{
		FilterConfiguration filterConfiguration = new FilterConfiguration(true, true, true, true, true, null, null, "");

		List<Transaction> transactions = transactionService.getTransactionsForMonthAndYear(accountRepository.findByName("Default Account"), 6, 2021, filterConfiguration);
		assertThat(transactions)
				.hasSize(1)
				.containsExactly(transactionLastDayOfJune);
		assertThat(transactions.get(0).getDate())
				.isEqualTo(LocalDate.of(2021, 6, 30));
	}

	@Test
	void test_getTransactionsForMonthAndYear_CloseToMidnight()
	{
		try(MockedStatic<DateHelper> dateHelper = Mockito.mockStatic(DateHelper.class))
		{
			dateHelper.when(DateHelper::getCurrentDate).thenReturn(LocalDate.of(2021, 2, 5));

			FilterConfiguration filterConfiguration = new FilterConfiguration(true, true, true, true, true, null, null, "");

			List<Transaction> transactions = transactionService.getTransactionsForMonthAndYear(accountRepository.findByName("Default Account"), 6, 2021, filterConfiguration);
			assertThat(transactions)
					.hasSize(1)
					.containsExactly(transactionLastDayOfJune);
			assertThat(transactions.get(0).getDate())
					.isEqualTo(LocalDate.of(2021, 6, 30));
		}
	}

	@Test
	void test_getTransactionForBalanceLastMonth()
	{
		final Transaction transaction = transactionService.getTransactionForBalanceLastMonth(accountRepository.findByName("Default Account"), 6, 2021);

		final Transaction expected = new Transaction();
		expected.setName("Last month balance");
		expected.setAmount(998300);
		expected.setDate(LocalDate.of(2021, 6, 1));
		expected.setCategory(categoryService.findByType(CategoryType.REST));
		expected.setTags(List.of());

		assertThat(transaction).isEqualTo(expected);
	}

	@Test
	void test_getTransactionForBalanceCurrentMonth()
	{
		final Transaction transaction = transactionService.getTransactionForBalanceCurrentMonth(accountRepository.findByName("Default Account"), 6, 2021);

		final Transaction expected = new Transaction();
		expected.setName("Balance at end of month");
		expected.setAmount(998200);
		expected.setDate(LocalDate.of(2021, 6, 30));
		expected.setCategory(categoryService.findByType(CategoryType.REST));
		expected.setTags(List.of());

		assertThat(transaction).isEqualTo(expected);
	}
}
