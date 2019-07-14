package de.deadlocker8.budgetmaster.charts;

import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;


public class ChartSettings
{
	private int chartID;
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private DateTime startDate;
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private DateTime endDate;
	private FilterConfiguration filterConfiguration;

	public static ChartSettings getDefault(int chartID, FilterConfiguration filterConfiguration)
	{
		return new ChartSettings(chartID, DateTime.now().withDayOfMonth(1), DateTime.now().dayOfMonth().withMaximumValue(), filterConfiguration);
	}

	public ChartSettings()
	{
	}

	public ChartSettings(int chartID, DateTime startDate, DateTime endDate, FilterConfiguration filterConfiguration)
	{
		this.chartID = chartID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.filterConfiguration = filterConfiguration;
	}

	public int getChartID()
	{
		return chartID;
	}

	public void setChartID(int chartID)
	{
		this.chartID = chartID;
	}

	public DateTime getStartDate()
	{
		return startDate;
	}

	public void setStartDate(DateTime startDate)
	{
		this.startDate = startDate;
	}

	public DateTime getEndDate()
	{
		return endDate;
	}

	public void setEndDate(DateTime endDate)
	{
		this.endDate = endDate;
	}

	public FilterConfiguration getFilterConfiguration()
	{
		return filterConfiguration;
	}

	public void setFilterConfiguration(FilterConfiguration filterConfiguration)
	{
		this.filterConfiguration = filterConfiguration;
	}

	@Override
	public String toString()
	{
		return "ChartSettings{" +
				"chartID=" + chartID +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", filterConfiguration=" + filterConfiguration +
				'}';
	}
}
