package de.deadlocker8.budgetmaster.databasemigrator;

import de.deadlocker8.budgetmaster.databasemigrator.destination.StepNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.account.DestinationAccount;
import de.deadlocker8.budgetmaster.databasemigrator.destination.account.DestinationAccountIntegerRepository;
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

@Import(MigrateAccountsTest.TestDatabaseConfiguration.class)
@EnableAutoConfiguration
class MigrateAccountsTest extends MigratorTestBase
{
	@TestConfiguration
	static class TestDatabaseConfiguration
	{
		@Value("classpath:accounts.mv.db")
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
	private DestinationAccountIntegerRepository accountRepository;

	@Test
	void test_stepMigrateAccounts()
	{
		final JobExecution jobExecution = jobLauncherTestUtils.launchStep(StepNames.ACCOUNTS, DEFAULT_JOB_PARAMETERS);
		final List<StepExecution> stepExecutions = new ArrayList<>(jobExecution.getStepExecutions());

		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		assertThat(stepExecutions).hasSize(1);
		final StepExecution stepExecution = stepExecutions.get(0);
		assertThat(stepExecution.getReadCount()).isEqualTo(7);
		assertThat(stepExecution.getCommitCount()).isEqualTo(8);

		final DestinationAccount accountPlaceholder = new DestinationAccount(1, "Placeholder", false, false, 0, 1, 0);
		final DestinationAccount accountDefault = new DestinationAccount(2, "Default Account", true, true, 0, 2, 1);
		final DestinationAccount accountWithBuiltinIcon = new DestinationAccount(3, "Account with builtin icon", false, false, 0, 5, 1);
		final DestinationAccount accountWithoutIcon = new DestinationAccount(4, "Account without icon", false, false, 0, 6, 1);
		final DestinationAccount accountWithImage = new DestinationAccount(5, "Account with image", false, false, 0, 7, 1);
		final DestinationAccount accountReadOnly = new DestinationAccount(6, "Read-only account", false, false, 1, 8, 1);
		final DestinationAccount accountHidden = new DestinationAccount(7, "Hidden account", false, false, 2, 9, 1);


		final List<DestinationAccount> accounts = accountRepository.findAll();
		assertThat(accounts)
				.hasSize(7)
				.containsExactly(accountPlaceholder, accountDefault, accountWithBuiltinIcon, accountWithoutIcon, accountWithImage, accountReadOnly, accountHidden);
	}
}