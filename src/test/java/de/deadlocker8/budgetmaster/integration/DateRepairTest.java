package de.deadlocker8.budgetmaster.integration;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@Import(DateRepairTest.TestDatabaseConfiguration.class)
@ActiveProfiles("test")
@Transactional
public class DateRepairTest
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
	private TransactionRepository transactionRepository;

	@Test
	public void test_Repeating_WithTags()
	{
		final List<Transaction> transactions = transactionRepository.findAll();
		assertThat(transactions).hasSize(8);

		assertThat(transactions.stream()
				.map(t -> t.getTags().stream()
						.map(Tag::getName).toArray(String[]::new))
				.toArray(String[][]::new))
				.containsOnly(new String[]{"0815", "abc"}, new String[0]);
	}

	@Test
	public void test_Repeating()
	{
		final List<Transaction> transactions = transactionRepository.findAll();
		assertThat(transactions).hasSize(8);

		assertThat(transactions.stream()
				.map(t -> t.getDate().getHourOfDay())
				.collect(Collectors.toList()))
				.containsOnly(0);
	}
}
