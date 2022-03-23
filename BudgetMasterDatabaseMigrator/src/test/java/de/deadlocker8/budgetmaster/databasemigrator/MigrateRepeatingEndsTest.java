package de.deadlocker8.budgetmaster.databasemigrator;

import de.deadlocker8.budgetmaster.databasemigrator.destination.StepNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.end.DestinationRepeatingEnd;
import de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.end.DestinationRepeatingEndRepository;
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

@Import(MigrateRepeatingEndsTest.TestDatabaseConfiguration.class)
@EnableAutoConfiguration
class MigrateRepeatingEndsTest extends MigratorTestBase
{
	@TestConfiguration
	static class TestDatabaseConfiguration
	{
		@Value("classpath:transactions.mv.db")
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
	private DestinationRepeatingEndRepository repeatingEndRepository;

	@Test
	void test_stepMigrateRepeatingEnds()
	{
		final JobExecution jobExecution = jobLauncherTestUtils.launchStep(StepNames.REPEATING_ENDS, DEFAULT_JOB_PARAMETERS);
		final List<StepExecution> stepExecutions = new ArrayList<>(jobExecution.getStepExecutions());

		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		assertThat(stepExecutions).hasSize(1);
		final StepExecution stepExecution = stepExecutions.get(0);
		assertThat(stepExecution.getReadCount()).isEqualTo(5);
		assertThat(stepExecution.getCommitCount()).isEqualTo(6);

		final DestinationRepeatingEnd repeatingEndDate = new DestinationRepeatingEnd(4, "repeating.end.key.date", "RepeatingEndDate");
		final DestinationRepeatingEnd repeatingEndNever = new DestinationRepeatingEnd(5, "repeating.end.key.never", "RepeatingEndNever");
		final DestinationRepeatingEnd repeatingEndAfterXTimes = new DestinationRepeatingEnd(6, "repeating.end.key.afterXTimes", "RepeatingEndAfterXTimes");

		final List<DestinationRepeatingEnd> repeatingEnds = repeatingEndRepository.findAll();
		assertThat(repeatingEnds)
				.hasSize(5)
				.contains(repeatingEndDate, repeatingEndNever, repeatingEndAfterXTimes);
	}
}