package de.deadlocker8.budgetmaster.databasemigrator;

import de.deadlocker8.budgetmaster.databasemigrator.destination.StepNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.chart.DestinationChart;
import de.deadlocker8.budgetmaster.databasemigrator.destination.chart.DestinationChartRepository;
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

@Import(MigrateChartsTest.TestDatabaseConfiguration.class)
@EnableAutoConfiguration
class MigrateChartsTest extends MigratorTestBase
{
	@TestConfiguration
	static class TestDatabaseConfiguration
	{
		@Value("classpath:charts.mv.db")
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
	private DestinationChartRepository chartRepository;

	private static final String DEFAULT_CHART_SCRIPT = """
			/* This list will be dynamically filled with all the transactions between
			 * the start and and date you select on the "Show Chart" page
			 * and filtered according to your specified filter.
			 * An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:
			 * https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts
			 */
			var transactionData = [];

			// Note: All variables starting with "localized" are only available inside default charts.

			// group transactions by date
			var groups = transactionData.reverse().reduce((groups, transaction) =>
			{
			    var date = transaction.date;
			    if(!groups[date])
			    {
			        groups[date] = [];
			    }
			    groups[date].push(transaction);
			    return groups;
			}, {});

			var dates = Object.keys(groups);
			var previousSum = 0;
			var sums = [];

			// calculate sum for each date
			for(var key in groups)
			{
			    if(groups.hasOwnProperty(key))
			    {
			        var group = groups[key];

			        // extract all amount values
			        var amounts = group.map(transaction => transaction.amount);

			        // sum up all amounts
			        var currentSum = amounts.reduce((a, b) => a + b, 0);

			        // add sum of current date to previous sum
			        currentSum = previousSum + currentSum;

			        // save current sum for next loop cycle
			        previousSum = currentSum;

			        // add sum to array
			        sums.push(currentSum / 100);
			    }
			}

			// Prepare your chart settings here (mandatory)
			var plotlyData = [
			    {
			        x: dates,
			        y: sums,
			        type: 'line'
			    }
			];

			// Add your Plotly layout settings here (optional)
			var plotlyLayout = {
			    title: {
			        text: localizedTitle,
			    },
			    yaxis: {
			        title: localizedData['axisY'] + localizedCurrency,
			        rangemode: 'tozero',
			        tickformat: '.2f',
			        showline: true
			    },
			    xaxis: {
			        tickformat: '%d.%m.%y'
			    }
			};

			// Add your Plotly configuration settings here (optional)
			var plotlyConfig = {
			    showSendToCloud: false,
			    displaylogo: false,
			    showLink: false,
			    responsive: true,
			    displayModeBar: true,
			    toImageButtonOptions: {
			        format: 'png',
			        filename: 'BudgetMaster_chart_export',
			        height: 1080,
			        width: 1920,
			    }
			};

			// Don't touch this line
			Plotly.newPlot("containerID", plotlyData, plotlyLayout, plotlyConfig);""";

	private static final String CUSTOM_CHART_SCRIPT = """
			/* This list will be dynamically filled with all the transactions between
			* the start and and date you select on the "Show Chart" page
			* and filtered according to your specified filter.
			* An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:
			* https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts
			*/
			var transactionData = [];

			// Prepare your chart settings here (mandatory)
			var plotlyData = [{
			    x: [],
			    y: [],
			    type: 'bar'
			}];

			// Add your Plotly layout settings here (optional)
			var plotlyLayout = {};

			// Add your Plotly configuration settings here (optional)
			var plotlyConfig = {
			    showSendToCloud: false,
			    displaylogo: false,
			    showLink: false,
			    responsive: true
			};

			console.log("my custom chart");

			// Don't touch this line
			Plotly.newPlot("containerID", plotlyData, plotlyLayout, plotlyConfig);""";


	@Test
	void test_stepMigrateCharts()
	{
		final JobExecution jobExecution = jobLauncherTestUtils.launchStep(StepNames.CHARTS, DEFAULT_JOB_PARAMETERS);
		final List<StepExecution> stepExecutions = new ArrayList<>(jobExecution.getStepExecutions());

		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		assertThat(stepExecutions).hasSize(1);
		final StepExecution stepExecution = stepExecutions.get(0);
		assertThat(stepExecution.getReadCount()).isEqualTo(13);
		assertThat(stepExecution.getCommitCount()).isEqualTo(14);

		final List<DestinationChart> charts = chartRepository.findAll();
		assertThat(charts).hasSize(13);

		final DestinationChart migratedDefaultChart = charts.get(0);
		assertThat(migratedDefaultChart)
				.hasFieldOrPropertyWithValue("ID", 1)
				.hasFieldOrPropertyWithValue("name", "charts.default.accountSumPerDay")
				.hasFieldOrPropertyWithValue("type", 0)
				.hasFieldOrPropertyWithValue("version", 11)
				.hasFieldOrPropertyWithValue("displayType", 2)
				.hasFieldOrPropertyWithValue("groupType", 0)
				.hasFieldOrPropertyWithValue("previewImageFileName", "accountSumPerDay.png");

		assertThat(migratedDefaultChart.getScript())
				.isEqualTo(DEFAULT_CHART_SCRIPT.replace("\n", "\r\n"));

		final DestinationChart migratedCustomChart = charts.get(12);
		assertThat(migratedCustomChart)
				.hasFieldOrPropertyWithValue("ID", 13)
				.hasFieldOrPropertyWithValue("name", "Custom chart")
				.hasFieldOrPropertyWithValue("type", 1)
				.hasFieldOrPropertyWithValue("version", 0)
				.hasFieldOrPropertyWithValue("displayType", 3)
				.hasFieldOrPropertyWithValue("groupType", 0)
				.hasFieldOrPropertyWithValue("previewImageFileName", null);

		assertThat(migratedCustomChart.getScript())
				.isEqualTo(CUSTOM_CHART_SCRIPT.replace("\n", "\r\n"));
	}
}