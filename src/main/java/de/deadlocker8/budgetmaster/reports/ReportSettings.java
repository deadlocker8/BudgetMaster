package de.deadlocker8.budgetmaster.reports;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.HashMap;
import java.util.Map;

public class ReportSettings
{
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private DateTime date;
	private boolean includeBudget;
	private boolean splitTables;
	private boolean includeCategoryBudgets;

	private Map<String, ReportColumn> columns;


	public ReportSettings(DateTime date, boolean includeBudget, boolean splitTables, boolean includeCategoryBudgets)
	{
		this.date = date;
		this.includeBudget = includeBudget;
		this.splitTables = splitTables;
		this.includeCategoryBudgets = includeCategoryBudgets;

		initColumns();
	}

	public ReportSettings()
	{
		initColumns();
	}

	private void initColumns()
	{
		this.columns = new HashMap<>();

		this.columns.put("report.position", new ReportColumn());
		this.columns.put("report.date", new ReportColumn());
		this.columns.put("report.repeating", new ReportColumn());
		this.columns.put("report.name", new ReportColumn());
		this.columns.put("report.category", new ReportColumn());
		this.columns.put("report.description", new ReportColumn());
		this.columns.put("report.tags", new ReportColumn());
		this.columns.put("report.account", new ReportColumn());
		this.columns.put("report.rating", new ReportColumn());
		this.columns.put("report.amount", new ReportColumn());
	}

	public DateTime getDate()
	{
		return date;
	}

	public void setDate(DateTime date)
	{
		this.date = date;
	}

	public boolean isIncludeBudget()
	{
		return includeBudget;
	}

	public void setIncludeBudget(boolean includeBudget)
	{
		this.includeBudget = includeBudget;
	}

	public boolean isSplitTables()
	{
		return splitTables;
	}

	public void setSplitTables(boolean splitTables)
	{
		this.splitTables = splitTables;
	}

	public boolean isIncludeCategoryBudgets()
	{
		return includeCategoryBudgets;
	}

	public void setIncludeCategoryBudgets(boolean includeCategoryBudgets)
	{
		this.includeCategoryBudgets = includeCategoryBudgets;
	}

	public Map<String, ReportColumn> getColumns()
	{
		return this.columns;
	}

	public void setColumns(Map<String, ReportColumn> columns)
	{
		this.columns = columns;
	}

	@Override
	public String toString()
	{
		return "ReportSettings{" +
				"date=" + date +
				", includeBudget=" + includeBudget +
				", splitTables=" + splitTables +
				", includeCategoryBudgets=" + includeCategoryBudgets +
				", columns=" + columns +
				'}';
	}
}
