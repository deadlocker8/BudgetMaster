package de.deadlocker8.budgetmaster.databasemigrator;

import de.deadlocker8.budgetmaster.databasemigrator.destination.StepNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.hint.DestinationHint;
import de.deadlocker8.budgetmaster.databasemigrator.destination.hint.DestinationHintRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.transaction.DestinationTransaction;
import de.deadlocker8.budgetmaster.databasemigrator.destination.transaction.DestinationTransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(MigrateTransactionsTest.TestDatabaseConfiguration.class)
@EnableAutoConfiguration
class MigrateTransactionsTest extends MigratorTestBase
{
	@TestConfiguration
	static class TestDatabaseConfiguration
	{
		@Value("classpath:transactions.mv.db")
		private Resource databaseResource;

		@Bean(name = "primaryDataSource")
		@Primary
		public DataSource dataSource() throws IOException
		{
			final String folderName = databaseResource.getFile().getAbsolutePath().replace(".mv.db", "");
			String jdbcString = "jdbc:h2:/" + folderName + ";DB_CLOSE_ON_EXIT=TRUE";
			return DataSourceBuilder.create().username("sa").password("").url(jdbcString).driverClassName("org.h2.Driver").build();
		}
	}

	@Autowired
	private DestinationTransactionRepository transactionRepository;

	@Test
	void test_stepMigrateTransactions()
	{
		final JobExecution jobExecution = jobLauncherTestUtils.launchStep(StepNames.TRANSACTIONS, DEFAULT_JOB_PARAMETERS);
		final List<StepExecution> stepExecutions = new ArrayList<>(jobExecution.getStepExecutions());

		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		assertThat(stepExecutions).hasSize(1);
		final StepExecution stepExecution = stepExecutions.get(0);
		assertThat(stepExecution.getReadCount()).isEqualTo(12);
		assertThat(stepExecution.getCommitCount()).isEqualTo(13);

		final DestinationTransaction transactionNormal = new DestinationTransaction(1, -1500, true, "2022-03-23 00:00:00", 2, 3, "Normal transaction", "", null, null);
		final DestinationTransaction transactionRepeating = new DestinationTransaction(5, -100, true, "2022-03-23 00:00:00", 2, 1, "Repeating month end date", "", 4, null);
		final DestinationTransaction transactionTransfer = new DestinationTransaction(12, -1000, true, "2022-03-27 00:00:00", 2, 1, "Transfer", "", null, 3);
		final DestinationTransaction transactionTransferRepeating = new DestinationTransaction(13, -200, true, "2022-03-27 00:00:00", 2, 1, "Repeating Transfer", "", 9, 3);

		final List<DestinationTransaction> transactions = transactionRepository.findAll();
		assertThat(transactions)
				.hasSize(12)
				.contains(transactionNormal, transactionRepeating, transactionTransfer, transactionTransferRepeating);
	}
}