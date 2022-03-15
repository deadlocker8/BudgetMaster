package de.deadlocker8.budgetmaster.databasemigrator;

import de.deadlocker8.budgetmaster.databasemigrator.destination.icon.DestinationIcon;
import de.deadlocker8.budgetmaster.databasemigrator.destination.icon.DestinationIconRepository;
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

@Import(MigrateIconsTest.TestDatabaseConfiguration.class)
@EnableAutoConfiguration
class MigrateIconsTest extends MigratorTestBase
{
	@TestConfiguration
	static class TestDatabaseConfiguration
	{
		@Value("classpath:categories.mv.db")
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
	private DestinationIconRepository iconRepository;

	@Test
	void test_stepMigrateImages()
	{
		final JobExecution jobExecution = jobLauncherTestUtils.launchStep("Migrate icons", DEFAULT_JOB_PARAMETERS);
		final List<StepExecution> stepExecutions = new ArrayList<>(jobExecution.getStepExecutions());

		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		assertThat(stepExecutions).hasSize(1);
		final StepExecution stepExecution = stepExecutions.get(0);
		assertThat(stepExecution.getReadCount()).isEqualTo(7);
		assertThat(stepExecution.getCommitCount()).isEqualTo(8);

		final DestinationIcon iconAllAccounts = new DestinationIcon(1, null, "fas fa-landmark", null);
		final DestinationIcon iconEmpty1 = new DestinationIcon(2, null, null, null);
		final DestinationIcon iconEmpty2 = new DestinationIcon(3, null, null, null);
		final DestinationIcon iconEmpty3 = new DestinationIcon(4, null, null, null);
		final DestinationIcon iconWithImage = new DestinationIcon(5, 1, null, null);
		final DestinationIcon iconBuiltin = new DestinationIcon(6, null, "fas fa-apple-alt", null);
		final DestinationIcon iconFontColor = new DestinationIcon(7, null, null, "#000000ff");

		final List<DestinationIcon> icons = iconRepository.findAll();
		assertThat(icons)
				.hasSize(7)
				.containsExactly(iconAllAccounts, iconEmpty1, iconEmpty2, iconEmpty3, iconWithImage, iconBuiltin, iconFontColor);
	}
}