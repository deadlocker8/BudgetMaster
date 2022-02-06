package de.deadlocker8.budgetmaster.charts;

import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import de.deadlocker8.budgetmaster.utils.DateHelper;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;


public class ChartSettings
{
	private ChartDisplayType displayType;
	private ChartGroupType groupType;

	private Integer chartID;
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private DateTime startDate;
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private DateTime endDate;
	private FilterConfiguration filterConfiguration;

	public static ChartSettings getDefault(FilterConfiguration filterConfiguration)
	{
		return new ChartSettings(ChartDisplayType.BAR, ChartGroupType.MONTH, null, DateHelper.getCurrentDateWithUTC().withDayOfMonth(1), DateHelper.getCurrentDateWithUTC().dayOfMonth().withMaximumValue(), filterConfiguration);
	}

	public ChartSettings()
	{
	}

	public ChartSettings(ChartDisplayType displayType, ChartGroupType groupType, Integer chartID, DateTime startDate, DateTime endDate, FilterConfiguration filterConfiguration)
	{
		this.displayType = displayType;
		this.groupType = groupType;
		this.chartID = chartID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.filterConfiguration = filterConfiguration;
	}

	public ChartDisplayType getDisplayType()
	{
		return displayType;
	}

	public void setDisplayType(ChartDisplayType displayType)
	{
		this.displayType = displayType;
	}

	public ChartGroupType getGroupType()
	{
		return groupType;
	}

	public void setGroupType(ChartGroupType groupType)
	{
		this.groupType = groupType;
	}

	public Integer getChartID()
	{
		return chartID;
	}

	public void setChartID(Integer chartID)
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

	public boolean isChartSelected()
	{
		return chartID != null;
	}

	@Override
	public String toString()
	{
		return "ChartSettings{" +
				"displayType=" + displayType +
				", groupType=" + groupType +
				", chartID=" + chartID +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", filterConfiguration=" + filterConfiguration +
				'}';
	}
}
