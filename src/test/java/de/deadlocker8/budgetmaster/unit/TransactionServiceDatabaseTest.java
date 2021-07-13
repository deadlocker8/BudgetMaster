package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTest;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Main.class)
@Import(TransactionServiceDatabaseTest.TestDatabaseConfiguration.class)
@ActiveProfiles("test")
@SeleniumTest
@Transactional
class TransactionServiceDatabaseTest
{
	@TestConfiguration
	static class TestDatabaseConfiguration
	{
		@Value("classpath:repeating_with_tags.mv.db")
		private Resource databaseResource;

		@Bean
		@Primary
		public DataSource dataSource() throws IOException
		{
			final String folderName = databaseResource.getFile().getAbsolutePath().replace(".mv.db", "");
			String jdbcString = "jdbc:h2:/" + folderName + ";DB_CLOSE_ON_EXIT=TRUE";
			return DataSourceBuilder.create().username("sa").password("").url(jdbcString).driverClassName("org.h2.Driver").build();
		}
	}

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private AccountService accountService;

	@Test
	void test_deleteAll()
	{
		transactionService.deleteAll();

		assertThat(transactionService.getRepository().findAll()).isEmpty();
	}

	@Test
	void test_getTransactionsForAccount_specificAccount()
	{
		DateTime date1 = DateTime.parse("2020-04-30", DateTimeFormat.forPattern("yyyy-MM-dd"));
		FilterConfiguration filterConfiguration = new FilterConfiguration(true, true, true, true, true, null, null, "");

		Transaction transaction1 = transactionService.getRepository().getOne(37);  // normal transaction
		Transaction transaction2 = transactionService.getRepository().getOne(9);  //transfer

		List<Transaction> transactions = transactionService.getTransactionsForAccount(accountService.getRepository().findByName("Second Account"), date1, DateTime.now(), filterConfiguration);
		assertThat(transactions).hasSize(2)
				.containsExactlyInAnyOrder(transaction1, transaction2);
	}

	@Test
	void test_getTransactionsForAccount_all()
	{
		DateTime date1 = DateTime.parse("2020-04-30", DateTimeFormat.forPattern("yyyy-MM-dd"));
		FilterConfiguration filterConfiguration = new FilterConfiguration(true, true, true, true, true, null, null, "");

		List<Transaction> transactions = transactionService.getTransactionsForAccount(accountService.getRepository().findAllByType(AccountType.ALL).get(0), date1, DateTime.now(), filterConfiguration);
		assertThat(transactions).hasSize(7);
	}

	@Test
	void test_getTransactionsForAccountUntilDate()
	{
		DateTime date1 = DateTime.parse("2020-04-30", DateTimeFormat.forPattern("yyyy-MM-dd"));
		DateTime date2 = DateTime.parse("2020-05-20", DateTimeFormat.forPattern("yyyy-MM-dd"));
		FilterConfiguration filterConfiguration = new FilterConfiguration(true, true, true, true, true, null, null, "");

		List<Transaction> transactions = transactionService.getTransactionsForAccount(accountService.getRepository().findByName("Default Account"), date1, date2, filterConfiguration);
		assertThat(transactions).hasSize(2);
	}

	@Test
	void test_getTransactionsForMonthAndYear()
	{
		FilterConfiguration filterConfiguration = new FilterConfiguration(true, true, true, true, true, null, null, "");

		List<Transaction> transactions = transactionService.getTransactionsForMonthAndYear(accountService.getRepository().findByName("Default Account"), 6, 2020, false, filterConfiguration);
		assertThat(transactions).hasSize(1);
	}
}
