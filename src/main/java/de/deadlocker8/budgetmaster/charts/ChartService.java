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
	private ChartRepository chartRepository;

	@Autowired
	public ChartService(ChartRepository categoryRepository)
	{
		this.chartRepository = categoryRepository;

		createDefaults();
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
}
