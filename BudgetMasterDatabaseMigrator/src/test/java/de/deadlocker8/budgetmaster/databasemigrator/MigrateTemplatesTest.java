package de.deadlocker8.budgetmaster.databasemigrator;

import de.deadlocker8.budgetmaster.databasemigrator.destination.StepNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.template.DestinationTemplate;
import de.deadlocker8.budgetmaster.databasemigrator.destination.template.DestinationTemplateRepository;
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

@Import(MigrateTemplatesTest.TestDatabaseConfiguration.class)
@EnableAutoConfiguration
class MigrateTemplatesTest extends MigratorTestBase
{
	@TestConfiguration
	static class TestDatabaseConfiguration
	{
		@Value("classpath:templates.mv.db")
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
	private DestinationTemplateRepository templateRepository;

	@Test
	void test_stepMigrateTemplates()
	{
		final JobExecution jobExecution = jobLauncherTestUtils.launchStep(StepNames.TEMPLATES, DEFAULT_JOB_PARAMETERS);
		final List<StepExecution> stepExecutions = new ArrayList<>(jobExecution.getStepExecutions());

		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		assertThat(stepExecutions).hasSize(1);
		final StepExecution stepExecution = stepExecutions.get(0);
		assertThat(stepExecution.getReadCount()).isEqualTo(3);
		assertThat(stepExecution.getCommitCount()).isEqualTo(4);

		final DestinationTemplate templateNormal = new DestinationTemplate(1, "Template normal", null, true, null, 1, "", "", 5, null, 1);
		final DestinationTemplate templateTransfer = new DestinationTemplate(2, "Template transfer", null, true, 2, 1, "", "", 7, 3, 1);
		final DestinationTemplate templateWithGroup = new DestinationTemplate(3, "Template with group", 1200, false, null, 1, "", "", 8, null, 2);

		final List<DestinationTemplate> templates = templateRepository.findAll();
		assertThat(templates)
				.hasSize(3)
				.contains(templateNormal, templateTransfer, templateWithGroup);
	}
}