package de.deadlocker8.budgetmaster.databasemigrator;

import de.deadlocker8.budgetmaster.databasemigrator.destination.StepNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.category.DestinationCategory;
import de.deadlocker8.budgetmaster.databasemigrator.destination.category.DestinationCategoryRepository;
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

@Import(MigrateCategoriesTest.TestDatabaseConfiguration.class)
@EnableAutoConfiguration
class MigrateCategoriesTest extends MigratorTestBase
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
	private DestinationCategoryRepository categoryRepository;

	@Test
	void test_stepMigraCategories()
	{
		final JobExecution jobExecution = jobLauncherTestUtils.launchStep(StepNames.CATEGORIES, DEFAULT_JOB_PARAMETERS);
		final List<StepExecution> stepExecutions = new ArrayList<>(jobExecution.getStepExecutions());

		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		assertThat(stepExecutions).hasSize(1);
		final StepExecution stepExecution = stepExecutions.get(0);
		assertThat(stepExecution.getReadCount()).isEqualTo(5);
		assertThat(stepExecution.getCommitCount()).isEqualTo(6);

		final DestinationCategory categoryNoCategory = new DestinationCategory(1, "No Category", "#FFFFFF", 0, 3);
		final DestinationCategory categoryRest = new DestinationCategory(2, "Rest", "#FFFF00", 1, 4);
		final DestinationCategory categoryWithIcon = new DestinationCategory(3, "Awesome Category", "#ff9500", 2, 5);
		final DestinationCategory categoryWithBuiltinIcon = new DestinationCategory(4, "Category with built in icon", "#4cd964", 2, 6);
		final DestinationCategory categoryWithoutIcon = new DestinationCategory(5, "Category without icon", "#a90329", 2, 7);

		final List<DestinationCategory> categories = categoryRepository.findAll();
		assertThat(categories)
				.hasSize(5)
				.containsExactly(categoryNoCategory, categoryRest, categoryWithIcon, categoryWithBuiltinIcon, categoryWithoutIcon);
	}
}