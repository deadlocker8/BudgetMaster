package de.deadlocker8.budgetmaster.database.model.v5.converter;

import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.database.model.v5.BackupChart_v5;

public class ChartConverter_v5 implements Converter<Chart, BackupChart_v5>
{
	public Chart convert(BackupChart_v5 backupChart)
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
		return chart;
	}
}
