package de.deadlocker8.budgetmaster.database.model.converter;

import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.charts.ChartDisplayType;
import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v5.BackupChart_v5;

public class ChartConverter implements Converter<Chart, BackupChart_v5>
{
	public Chart convertToInternalForm(BackupChart_v5 backupChart)
	{
		if(backupChart == null)
		{
			return null;
		}

		final Chart chart = new Chart();
		chart.setID(backupChart.getID());
		chart.setName(backupChart.getName());
		chart.setType(backupChart.getType());
		chart.setVersion(backupChart.getVersion());
		chart.setScript(backupChart.getScript());
		chart.setDisplayType(ChartDisplayType.CUSTOM);
		return chart;
	}

	@Override
	public BackupChart_v5 convertToExternalForm(Chart internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		final BackupChart_v5 chart = new BackupChart_v5();
		chart.setID(internalItem.getID());
		chart.setName(internalItem.getName());
		chart.setType(internalItem.getType());
		chart.setVersion(internalItem.getVersion());
		chart.setScript(internalItem.getScript());
		return chart;
	}
}
