package de.deadlocker8.budgetmaster.databasemigrator;

import de.deadlocker8.budgetmaster.databasemigrator.destination.StepNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.report.DestinationReportColumn;
import de.deadlocker8.budgetmaster.databasemigrator.destination.report.DestinationReportColumnIntegerRepository;
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

@Import(MigrateReportColumnsTest.TestDatabaseConfiguration.class)
@EnableAutoConfiguration
class MigrateReportColumnsTest extends MigratorTestBase
{
	@TestConfiguration
	static class TestDatabaseConfiguration
	{
		@Value("classpath:default_database_after_first_start.mv.db")
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
	private DestinationReportColumnIntegerRepository columnRepository;

	@Test
	void test_stepMigrateReportColumns()
	{
		final JobExecution jobExecution = jobLauncherTestUtils.launchStep(StepNames.REPORT_COLUMNS, DEFAULT_JOB_PARAMETERS);
		final List<StepExecution> stepExecutions = new ArrayList<>(jobExecution.getStepExecutions());

		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		assertThat(stepExecutions).hasSize(1);
		final StepExecution stepExecution = stepExecutions.get(0);
		assertThat(stepExecution.getReadCount()).isEqualTo(11);
		assertThat(stepExecution.getCommitCount()).isEqualTo(12);

		final DestinationReportColumn column1 = new DestinationReportColumn(1, true, "report.position", 0, 1);
		final DestinationReportColumn column2 = new DestinationReportColumn(2, true, "report.date", 1, 1);
		final DestinationReportColumn column3 = new DestinationReportColumn(3, true, "report.repeating", 2, 1);
		final DestinationReportColumn column4 = new DestinationReportColumn(4, true, "report.transfer", 3, 1);
		final DestinationReportColumn column5 = new DestinationReportColumn(5, true, "report.category", 4, 1);
		final DestinationReportColumn column6 = new DestinationReportColumn(6, true, "report.name", 5, 1);
		final DestinationReportColumn column7 = new DestinationReportColumn(7, true, "report.description", 6, 1);
		final DestinationReportColumn column8 = new DestinationReportColumn(8, true, "report.tags", 7, 1);
		final DestinationReportColumn column9 = new DestinationReportColumn(9, true, "report.account", 8, 1);
		final DestinationReportColumn column10 = new DestinationReportColumn(10, true, "report.rating", 9, 1);
		final DestinationReportColumn column11 = new DestinationReportColumn(11, true, "report.amount", 10, 1);

		final List<DestinationReportColumn> reportColumns = columnRepository.findAll();
		assertThat(reportColumns)
				.hasSize(11)
				.containsExactly(column1, column2, column3, column4, column5, column6, column7, column8, column9, column10, column11);
	}
}