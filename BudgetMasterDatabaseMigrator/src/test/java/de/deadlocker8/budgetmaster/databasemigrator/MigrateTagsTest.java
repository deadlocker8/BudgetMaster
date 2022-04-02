package de.deadlocker8.budgetmaster.databasemigrator;

import de.deadlocker8.budgetmaster.databasemigrator.destination.StepNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.tag.*;
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

@Import(MigrateTagsTest.TestDatabaseConfiguration.class)
@EnableAutoConfiguration
class MigrateTagsTest extends MigratorTestBase
{
	@TestConfiguration
	static class TestDatabaseConfiguration
	{
		@Value("classpath:tags.mv.db")
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
	private DestinationTagRepository tagRepository;

	@Autowired
	private DestinationTemplateTagRepository templateTagRepository;

	@Autowired
	private DestinationTransactionTagRepository transactionTagRepository;

	@Test
	void test_stepMigrateTags()
	{
		final JobExecution jobExecution = jobLauncherTestUtils.launchStep(StepNames.TAGS, DEFAULT_JOB_PARAMETERS);
		final List<StepExecution> stepExecutions = new ArrayList<>(jobExecution.getStepExecutions());

		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		assertThat(stepExecutions).hasSize(1);
		final StepExecution stepExecution = stepExecutions.get(0);
		assertThat(stepExecution.getReadCount()).isEqualTo(2);
		assertThat(stepExecution.getCommitCount()).isEqualTo(3);

		final DestinationTag tag1 = new DestinationTag(1, "My Awesome Tag");
		final DestinationTag tag2 = new DestinationTag(2, "ABC");

		final List<DestinationTag> tags = tagRepository.findAll();
		assertThat(tags)
				.hasSize(2)
				.containsExactly(tag1, tag2);
	}

	@Test
	void test_stepMigrateTemplateTags()
	{
		final JobExecution jobExecution = jobLauncherTestUtils.launchStep(StepNames.TEMPLATE_TAGS, DEFAULT_JOB_PARAMETERS);
		final List<StepExecution> stepExecutions = new ArrayList<>(jobExecution.getStepExecutions());

		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		assertThat(stepExecutions).hasSize(1);
		final StepExecution stepExecution = stepExecutions.get(0);
		assertThat(stepExecution.getReadCount()).isEqualTo(2);
		assertThat(stepExecution.getCommitCount()).isEqualTo(3);

		final DestinationTemplateTag tag1 = new DestinationTemplateTag(1, 1, 1);
		final DestinationTemplateTag tag2 = new DestinationTemplateTag(2, 1,2);

		final List<DestinationTemplateTag> tags = templateTagRepository.findAll();
		assertThat(tags)
				.hasSize(2)
				.containsExactly(tag1, tag2);
	}

	@Test
	void test_stepMigrateTransactionTags()
	{
		final JobExecution jobExecution = jobLauncherTestUtils.launchStep(StepNames.TRANSACTION_TAGS, DEFAULT_JOB_PARAMETERS);
		final List<StepExecution> stepExecutions = new ArrayList<>(jobExecution.getStepExecutions());

		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		assertThat(stepExecutions).hasSize(1);
		final StepExecution stepExecution = stepExecutions.get(0);
		assertThat(stepExecution.getReadCount()).isEqualTo(3);
		assertThat(stepExecution.getCommitCount()).isEqualTo(4);

		final DestinationTransactionTag tag1 = new DestinationTransactionTag(1, 1, 1);
		final DestinationTransactionTag tag2 = new DestinationTransactionTag(2, 2,1);
		final DestinationTransactionTag tag3 = new DestinationTransactionTag(3, 2,2);

		final List<DestinationTransactionTag> tags = transactionTagRepository.findAll();
		assertThat(tags)
				.hasSize(3)
				.containsExactly(tag1, tag2, tag3);
	}
}