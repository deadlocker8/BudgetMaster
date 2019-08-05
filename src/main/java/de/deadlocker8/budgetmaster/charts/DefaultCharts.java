package de.deadlocker8.budgetmaster.charts;

import de.thecodelabs.utils.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DefaultCharts
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCharts.class);

	private static final Chart CHART_ACCOUNT_SUM_PER_DAY = new Chart("charts.default.accountSumPerDay",
			getChartFromFile("charts/AccountSumPerDay.js"),
			ChartType.DEFAULT, 4);

	private static final Chart CHART_INCOMES_AND_EXPENDITURES_PER_MONTH_BAR = new Chart("charts.default.incomesAndExpendituresPerMonthBar",
			getChartFromFile("charts/IncomesAndExpendituresPerMonthBar.js"),
			ChartType.DEFAULT, 5);

	private static final Chart CHART_INCOMES_AND_EXPENDITURES_PER_MONTH_LINE = new Chart("charts.default.incomesAndExpendituresPerMonthLine",
			getChartFromFile("charts/IncomesAndExpendituresPerMonthLine.js"),
			ChartType.DEFAULT, 5);

	private static final Chart CHART_INCOMES_AND_EXPENDITURES_BY_CATEGORY_BAR = new Chart("charts.default.incomesAndExpendituresByCategoryBar",
			getChartFromFile("charts/IncomesAndExpendituresByCategoryBar.js"),
			ChartType.DEFAULT, 23);

	private static final Chart CHART_INCOMES_AND_EXPENDITURES_PER_MONTH_BY_CATEGORIES = new Chart("charts.default.incomesAndExpendituresPerMonthByCategories",
			getChartFromFile("charts/IncomesAndExpendituresPerMonthByCategories.js"),
			ChartType.DEFAULT, 13);


	public static List<Chart> getDefaultCharts()
	{
		List<Chart> charts = new ArrayList<>();
		charts.add(CHART_ACCOUNT_SUM_PER_DAY);
		charts.add(CHART_INCOMES_AND_EXPENDITURES_PER_MONTH_BAR);
		charts.add(CHART_INCOMES_AND_EXPENDITURES_PER_MONTH_LINE);
		charts.add(CHART_INCOMES_AND_EXPENDITURES_BY_CATEGORY_BAR);
		charts.add(CHART_INCOMES_AND_EXPENDITURES_PER_MONTH_BY_CATEGORIES);

		charts.sort(Comparator.comparing(Chart::getName));
		return charts;
	}

	private static String getChartFromFile(String filePath)
	{
		URL url = DefaultCharts.class.getClassLoader().getResource(filePath);
		if(url == null)
		{
			LOGGER.warn("Couldn't add default chart '" + filePath + "' due to missing file");
			return "";
		}

		try
		{
			return IOUtils.readURL(url);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		return "";
	}
}
