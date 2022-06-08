package de.deadlocker8.budgetmaster.database.importer;

import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.charts.ChartRepository;
import de.deadlocker8.budgetmaster.charts.ChartService;
import de.deadlocker8.budgetmaster.services.EntityType;

public class ChartImporter extends ItemImporter<Chart>
{
	private final ChartService chartService;

	public ChartImporter(ChartService chartService)
	{
		super(chartService.getRepository(), EntityType.CHART);
		this.chartService = chartService;
	}

	@Override
	protected int importSingleItem(Chart chart) throws ImportException
	{
		if(!(repository instanceof ChartRepository repository))
		{
			throw new IllegalArgumentException("Invalid repository type");
		}

		final int highestUsedID = chartService.getHighestUsedID();
		chart.setID(highestUsedID + 1);

		repository.save(chart);

		return chart.getID();
	}

	@Override
	protected String getNameForItem(Chart item)
	{
		return item.getName();
	}
}
