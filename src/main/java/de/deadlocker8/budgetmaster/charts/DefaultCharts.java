package de.deadlocker8.budgetmaster.charts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DefaultCharts
{
	private static final Chart CHART_ACCOUNT_SUM_PER_DAY = new Chart("charts.default.accountSumPerDay", getChartFromFile("charts/AccountSumPerDay.js"), ChartType.DEFAULT);
	private static final Chart CHART_INCOMES_AND_EXPENDITURES_PER_MONTH_BAR = new Chart("charts.default.incomesAndExpendituresPerMonthBar", getChartFromFile("charts/IncomesAndExpendituresPerMonthBar.js"), ChartType.DEFAULT);
	private static final Chart CHART_INCOMES_AND_EXPENDITURES_PER_MONTH_LINE = new Chart("charts.default.incomesAndExpendituresPerMonthLine", getChartFromFile("charts/IncomesAndExpendituresPerMonthLine.js"), ChartType.DEFAULT);


	public static List<Chart> getDefaultCharts()
	{
		List<Chart> charts = new ArrayList<>();
		charts.add(CHART_ACCOUNT_SUM_PER_DAY);
		charts.add(CHART_INCOMES_AND_EXPENDITURES_PER_MONTH_BAR);
		charts.add(CHART_INCOMES_AND_EXPENDITURES_PER_MONTH_LINE);

		charts.sort(Comparator.comparing(Chart::getName));
		return charts;
	}

	private static String getChartFromFile(String filePath)
	{
		InputStream stream = DefaultCharts.class.getClassLoader().getResourceAsStream(filePath);
		try
		{
			return readFromInputStream(stream);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		return "";
	}

	private static String readFromInputStream(InputStream inputStream) throws IOException
	{
		StringBuilder resultStringBuilder = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream)))
		{
			String line;
			while((line = br.readLine()) != null)
			{
				resultStringBuilder.append(line).append("\n");
			}
		}
		return resultStringBuilder.toString();
	}
}
