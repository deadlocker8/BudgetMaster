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
		if(chartRepository.findAllByType(ChartType.DEFAULT).size() != defaultCharts.size())
		{
			for(Chart chart : defaultCharts)
			{
				if(chartRepository.findByName(chart.getName()) == null)
				{
					chartRepository.save(chart);
					LOGGER.debug("Created default chart '" + chart.getName() + "'");
				}
			}
		}
	}
}
