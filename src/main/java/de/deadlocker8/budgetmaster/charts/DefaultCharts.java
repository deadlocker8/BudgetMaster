package de.deadlocker8.budgetmaster.charts;

import de.thecodelabs.utils.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class DefaultCharts
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCharts.class);

	public static final Chart CHART_DEFAULT = new Chart(null,
			getChartFromFile("charts/Default.js"),
			ChartType.CUSTOM, -1, ChartDisplayType.CUSTOM, ChartGroupType.NONE, null);

	private static final Chart CHART_ACCOUNT_SUM_PER_DAY = new Chart("charts.default.accountSumPerDay",
			getChartFromFile("charts/AccountSumPerDay.js"),
			ChartType.DEFAULT, 11, ChartDisplayType.LINE, ChartGroupType.NONE, "accountSumPerDay.png");

	private static final Chart CHART_INCOMES_AND_EXPENDITURES_PER_MONTH_BAR = new Chart("charts.default.incomesAndExpendituresPerMonthBar",
			getChartFromFile("charts/IncomesAndExpendituresPerMonthBar.js"),
			ChartType.DEFAULT, 11, ChartDisplayType.BAR, ChartGroupType.MONTH, "incomesAndExpendituresPerMonthBar.png");

	private static final Chart CHART_INCOMES_AND_EXPENDITURES_PER_MONTH_LINE = new Chart("charts.default.incomesAndExpendituresPerMonthLine",
			getChartFromFile("charts/IncomesAndExpendituresPerMonthLine.js"),
			ChartType.DEFAULT, 11, ChartDisplayType.LINE, ChartGroupType.MONTH, "incomesAndExpendituresPerMonthLine.png");

	private static final Chart CHART_INCOMES_AND_EXPENDITURES_BY_CATEGORY_BAR = new Chart("charts.default.incomesAndExpendituresByCategoryBar",
			getChartFromFile("charts/IncomesAndExpendituresByCategoryBar.js"),
			ChartType.DEFAULT, 8, ChartDisplayType.BAR, ChartGroupType.NONE, "incomesAndExpendituresByCategoryBar.png");

	private static final Chart CHART_INCOMES_AND_EXPENDITURES_BY_CATEGORY_PIE = new Chart("charts.default.incomesAndExpendituresByCategoryPie",
			getChartFromFile("charts/IncomesAndExpendituresByCategoryPie.js"),
			ChartType.DEFAULT, 8, ChartDisplayType.PIE, ChartGroupType.NONE, "incomesAndExpendituresByCategoryPie.png");

	private static final Chart CHART_INCOMES_AND_EXPENDITURES_PER_MONTH_BY_CATEGORIES = new Chart("charts.default.incomesAndExpendituresPerMonthByCategories",
			getChartFromFile("charts/IncomesAndExpendituresPerMonthByCategories.js"),
			ChartType.DEFAULT, 23, ChartDisplayType.BAR, ChartGroupType.MONTH, "incomesAndExpendituresPerMonthByCategories.png");

	private static final Chart CHART_REST_PER_MONTH = new Chart("charts.default.restPerMonth",
			getChartFromFile("charts/RestPerMonth.js"),
			ChartType.DEFAULT, 7, ChartDisplayType.BAR, ChartGroupType.MONTH, "restPerMonth.png");

	private static final Chart CHART_INCOMES_AND_EXPENDITURES_PER_YEAR_BAR = new Chart("charts.default.incomesAndExpendituresPerYearBar",
			getChartFromFile("charts/IncomesAndExpendituresPerYearBar.js"),
			ChartType.DEFAULT, 6, ChartDisplayType.BAR, ChartGroupType.YEAR, "incomesAndExpendituresPerYearBar.png");

	private static final Chart CHART_INCOMES_AND_EXPENDITURES_PER_YEAR_BY_CATEGORIES = new Chart("charts.default.incomesAndExpendituresPerYearByCategories",
			getChartFromFile("charts/IncomesAndExpendituresPerYearByCategories.js"),
			ChartType.DEFAULT, 3, ChartDisplayType.BAR, ChartGroupType.YEAR, "incomesAndExpendituresPerYearByCategories.png");

	private static final Chart CHART_AVERAGE_TRANSACTION_AMOUNT_PER_CATEGORY = new Chart("charts.default.averageTransactionAmountPerCategory",
			getChartFromFile("charts/AverageTransactionAmountPerCategoryBar.js"),
			ChartType.DEFAULT, 9, ChartDisplayType.BAR, ChartGroupType.NONE, "averageTransactionAmountPerCategory.png");

	private static final Chart CHART_AVERAGE_MONTHLY_INCOMES_AND_EXPENDITURES_PER_YEAR_BAR = new Chart("charts.default.averageMonthlyIncomesAndExpendituresPerYearBar",
			getChartFromFile("charts/AverageMonthlyIncomesAndExpendituresPerYearBar.js"),
			ChartType.DEFAULT, 9, ChartDisplayType.BAR, ChartGroupType.YEAR, "averageMonthlyIncomesAndExpendituresPerYearBar.png");

	private DefaultCharts()
	{
	}

	public static List<Chart> getDefaultCharts()
	{
		List<Chart> charts = new ArrayList<>();
		charts.add(CHART_ACCOUNT_SUM_PER_DAY);
		charts.add(CHART_INCOMES_AND_EXPENDITURES_BY_CATEGORY_BAR);
		charts.add(CHART_INCOMES_AND_EXPENDITURES_PER_MONTH_LINE);
		charts.add(CHART_INCOMES_AND_EXPENDITURES_PER_MONTH_BAR);
		charts.add(CHART_INCOMES_AND_EXPENDITURES_BY_CATEGORY_PIE);
		charts.add(CHART_INCOMES_AND_EXPENDITURES_PER_MONTH_BY_CATEGORIES);
		charts.add(CHART_REST_PER_MONTH);
		charts.add(CHART_INCOMES_AND_EXPENDITURES_PER_YEAR_BAR);
		charts.add(CHART_INCOMES_AND_EXPENDITURES_PER_YEAR_BY_CATEGORIES);
		charts.add(CHART_AVERAGE_TRANSACTION_AMOUNT_PER_CATEGORY);
		charts.add(CHART_AVERAGE_MONTHLY_INCOMES_AND_EXPENDITURES_PER_YEAR_BAR);
		return charts;
	}

	private static String getChartFromFile(String filePath)
	{
		URL url = DefaultCharts.class.getClassLoader().getResource(filePath);
		if(url == null)
		{
			LOGGER.warn(MessageFormat.format("Couldn''t add default chart ''{0}'' due to missing file", filePath));
			return "";
		}

		try
		{
			return IOUtils.readURL(url);
		}
		catch(IOException e)
		{
			LOGGER.error("Error getting chart from file", e);
		}

		return "";
	}
}
