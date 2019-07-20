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
	private static final Chart CHART_TEST = new Chart("charts.default.accountsum", getChartFromFile("charts/AccountSumPerDay.js"), ChartType.DEFAULT);

	public static List<Chart> getDefaultCharts()
	{
		List<Chart> charts = new ArrayList<>();
		charts.add(CHART_TEST);

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
