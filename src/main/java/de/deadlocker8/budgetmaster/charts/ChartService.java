package de.deadlocker8.budgetmaster.charts;

import de.deadlocker8.budgetmaster.services.Resetable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChartService implements Resetable
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private final String PATTERN_OLD_CONTAINER_ID = "Plotly.newPlot('chart-canvas',";
	private final String PATTERN_DYNAMIC_CONTAINER_ID = "Plotly.newPlot('containerID',";

	private ChartRepository chartRepository;

	@Autowired
	public ChartService(ChartRepository categoryRepository)
	{
		this.chartRepository = categoryRepository;

		createDefaults();
		updateUserCharts();
	}

	public ChartRepository getRepository()
	{
		return chartRepository;
	}

	@Override
	public void deleteAll()
	{
		chartRepository.deleteAll();
	}

	@Override
	public void createDefaults()
	{
		List<Chart> defaultCharts = DefaultCharts.getDefaultCharts();
		for(Chart chart : defaultCharts)
		{
			Chart currentChart = chartRepository.findByName(chart.getName());
			if(currentChart == null)
			{
				chart.setID(defaultCharts.indexOf(chart) + 1);
				chartRepository.save(chart);
				LOGGER.debug("Created default chart '" + chart.getName() + "'");
			}
			else if(currentChart.getVersion() < chart.getVersion())
			{
				LOGGER.debug("Update default chart '" + chart.getName() + "' from version " + currentChart.getVersion() + " to " + chart.getVersion());
				currentChart.setVersion(chart.getVersion());
				currentChart.setScript(chart.getScript());
				chartRepository.save(currentChart);
			}
		}
	}

	public int getHighestUsedID()
	{
		final List<Chart> chartsOrderedByID = chartRepository.findAllByOrderByIDDesc();
		if(chartsOrderedByID.isEmpty())
		{
			return 0;
		}

		return chartsOrderedByID.get(0).getID();
	}

	private void updateUserCharts()
	{
		final List<Chart> userCharts = getRepository().findAllByType(ChartType.CUSTOM);
		for(Chart userChart : userCharts)
		{
			String script = userChart.getScript();
			if(script.contains(PATTERN_OLD_CONTAINER_ID))
			{
				LOGGER.debug("Updating user chart '" + userChart.getName() + "' with ID " + userChart.getID());
				script = script.replace(PATTERN_OLD_CONTAINER_ID, PATTERN_DYNAMIC_CONTAINER_ID);
				userChart.setScript(script);
				getRepository().save(userChart);
			}
		}
	}
}
