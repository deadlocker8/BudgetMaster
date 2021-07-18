package de.deadlocker8.budgetmaster.charts;

import de.deadlocker8.budgetmaster.services.AccessAllEntities;
import de.deadlocker8.budgetmaster.services.Resettable;
import org.padler.natorder.NaturalOrderComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class ChartService implements Resettable, AccessAllEntities<Chart>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ChartService.class);

	private static final String PATTERN_OLD_CONTAINER_ID = "Plotly.newPlot('chart-canvas',";
	private static final String PATTERN_DYNAMIC_CONTAINER_ID = "Plotly.newPlot('containerID',";

	private final ChartRepository chartRepository;

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

	@SuppressWarnings("OptionalIsPresent")
	public boolean isDeletable(Integer ID)
	{
		Optional<Chart> chartOptional = getRepository().findById(ID);
		if(chartOptional.isPresent())
		{
			return chartOptional.get().getType() == ChartType.CUSTOM;
		}
		return false;
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
				LOGGER.debug(MessageFormat.format("Created default chart ''{0}''", chart.getName()));
			}
			else if(currentChart.getVersion() < chart.getVersion())
			{
				LOGGER.debug(MessageFormat.format("Update default chart ''{0}'' from version {1} to {2}", chart.getName(), currentChart.getVersion(), chart.getVersion()));
				currentChart.setVersion(chart.getVersion());
				currentChart.setScript(chart.getScript());
				currentChart.setDisplayType(chart.getDisplayType());
				currentChart.setGroupType(chart.getGroupType());
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
		updateContainerIdUsage();

		updateMissingAttributes();
	}

	private void updateContainerIdUsage()
	{
		final List<Chart> userCharts = getRepository().findAllByType(ChartType.CUSTOM);
		for(Chart userChart : userCharts)
		{
			String script = userChart.getScript();
			if(script.contains(PATTERN_OLD_CONTAINER_ID))
			{
				LOGGER.debug(MessageFormat.format("Updating container id usage for user chart ''{0}'' with ID {1}", userChart.getName(), userChart.getID()));
				script = script.replace(PATTERN_OLD_CONTAINER_ID, PATTERN_DYNAMIC_CONTAINER_ID);
				userChart.setScript(script);
				getRepository().save(userChart);
			}
		}
	}

	private void updateMissingAttributes()
	{
		final List<Chart> userCharts = getRepository().findAllByType(ChartType.CUSTOM);
		for(Chart userChart : userCharts)
		{
			boolean changed = false;
			if(userChart.getDisplayType() == null)
			{
				userChart.setDisplayType(ChartDisplayType.CUSTOM);
				changed = true;
			}
			if(userChart.getGroupType() == null)
			{
				userChart.setGroupType(ChartGroupType.NONE);
				changed = true;
			}

			if(changed)
			{
				LOGGER.debug(MessageFormat.format("Updating missing attributes for user chart ''{0}'' with ID {1}", userChart.getName(), userChart.getID()));
				getRepository().save(userChart);
			}
		}
	}

	@Override
	public List<Chart> getAllEntitiesAsc()
	{
		final List<Chart> charts = chartRepository.findAllByOrderByNameAsc();
		charts.sort((c1, c2) -> new NaturalOrderComparator().compare(c1.getName(), c2.getName()));
		return charts;
	}
}
