package de.deadlocker8.budgetmaster.logic.report;

public class ReportPreferences
{
	private ColumnOrder columnOrder;
	private boolean includeBudget;
	private boolean splitTable;
	private boolean includeCategoryBudgets;
	private ReportSorting reportSorting;
	
	public ReportPreferences()
	{
	
	}

	public ReportPreferences(ColumnOrder columnOrder, boolean includeBudget, boolean splitTable, boolean includeCategoryBudgets, ReportSorting reportSorting)
	{		
		this.columnOrder = columnOrder;
		this.includeBudget = includeBudget;
		this.splitTable = splitTable;
		this.includeCategoryBudgets = includeCategoryBudgets;
		this.reportSorting = reportSorting;
	}

	public ColumnOrder getColumnOrder()
	{
		return columnOrder;
	}

	public void setColumnOrder(ColumnOrder columnOrder)
	{
		this.columnOrder = columnOrder;
	}

	public boolean isIncludeBudget()
	{
		return includeBudget;
	}

	public void setIncludeBudget(boolean includeBudget)
	{
		this.includeBudget = includeBudget;
	}

	public boolean isSplitTable()
	{
		return splitTable;
	}

	public void setSplitTable(boolean splitTable)
	{
		this.splitTable = splitTable;
	}

	public boolean isIncludeCategoryBudgets()
	{
		return includeCategoryBudgets;
	}

	public void setIncludeCategoryBudgets(boolean includeCategoryBudgets)
	{
		this.includeCategoryBudgets = includeCategoryBudgets;
	}

	public ReportSorting getReportSorting()
	{
		return reportSorting;
	}

	public void setReportSorting(ReportSorting reportSorting)
	{
		this.reportSorting = reportSorting;
	}

	@Override
	public String toString()
	{
		return "ReportPreferences [columnOrder=" + columnOrder + ", includeBudget=" + includeBudget + ", splitTable=" + splitTable + ", includeCategoryBudgets=" + includeCategoryBudgets + ", reportSorting=" + reportSorting + "]";
	}
}