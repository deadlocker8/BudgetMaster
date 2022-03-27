package de.deadlocker8.budgetmaster.databasemigrator;

import de.deadlocker8.budgetmaster.databasemigrator.destination.StepNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.modifier.DestinationRepeatingModifier;
import de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.modifier.DestinationRepeatingModifierRepository;
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

@Import(MigrateRepeatingModifiersTest.TestDatabaseConfiguration.class)
@EnableAutoConfiguration
class MigrateRepeatingModifiersTest extends MigratorTestBase
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
	private DestinationRepeatingModifierRepository repeatingModifierRepository;

	@Test
	void test_stepMigrateRepeatingModifiers()
	{
		final JobExecution jobExecution = jobLauncherTestUtils.launchStep(StepNames.REPEATING_MODIFIERS, DEFAULT_JOB_PARAMETERS);
		final List<StepExecution> stepExecutions = new ArrayList<>(jobExecution.getStepExecutions());

		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		assertThat(stepExecutions).hasSize(1);
		final StepExecution stepExecution = stepExecutions.get(0);
		assertThat(stepExecution.getReadCount()).isEqualTo(6);
		assertThat(stepExecution.getCommitCount()).isEqualTo(7);

		final DestinationRepeatingModifier modifierDays = new DestinationRepeatingModifier(7, "repeating.modifier.days", "RepeatingModifierDays", 2);
		final DestinationRepeatingModifier modifierMonths = new DestinationRepeatingModifier(4, "repeating.modifier.months", "RepeatingModifierMonths", 1);
		final DestinationRepeatingModifier modifierYears = new DestinationRepeatingModifier(8, "repeating.modifier.years", "RepeatingModifierYears", 1);

		final List<DestinationRepeatingModifier> repeatingModifiers = repeatingModifierRepository.findAll();
		assertThat(repeatingModifiers)
				.hasSize(6)
				.contains(modifierDays, modifierMonths, modifierYears);
	}
}