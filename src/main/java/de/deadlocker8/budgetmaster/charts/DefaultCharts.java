package de.deadlocker8.budgetmaster.charts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DefaultCharts
{
	private static final Chart CHART_TEST = new Chart("charts.default.test", "console.log('Test')", ChartType.DEFAULT);

	public static List<Chart> getDefaultCharts()
	{
		List<Chart> charts = new ArrayList<>();
		charts.add(CHART_TEST);

		charts.sort(Comparator.comparing(Chart::getName));
		return charts;
	}
}
