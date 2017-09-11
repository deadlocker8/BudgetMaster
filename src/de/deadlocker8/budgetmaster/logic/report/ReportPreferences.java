package de.deadlocker8.budgetmaster.logic.report;

public class ReportPreferences
{
	private final int VERSION = 2;
	private ColumnOrder columnOrder;
	private boolean includeBudget;
	private boolean splitTable;
	private boolean includeCategoryBudgets;
	private ReportSorting reportSorting;
	private String reportFolderPath;
	
	public ReportPreferences()
	{
	
	}

	public ReportPreferences(ColumnOrder columnOrder, boolean includeBudget, boolean splitTable, boolean includeCategoryBudgets, ReportSorting reportSorting, String reportFolderPath)
	{
		this.columnOrder = columnOrder;
		this.includeBudget = includeBudget;
		this.splitTable = splitTable;
		this.includeCategoryBudgets = includeCategoryBudgets;
		this.reportSorting = reportSorting;
		this.reportFolderPath = reportFolderPath;
	}
	
	public int getVERSION()
	{
		return VERSION;
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
	
	public String getReportFolderPath()
	{
		return reportFolderPath;
	}

	public void setReportFolderPath(String reportFolderPath)
	{
		this.reportFolderPath = reportFolderPath;
	}

	@Override
	public String toString()
	{
		return "ReportPreferences [VERSION=" + VERSION + ", columnOrder=" + columnOrder + ", includeBudget=" + includeBudget + ", splitTable=" + splitTable + ", includeCategoryBudgets=" + includeCategoryBudgets + ", reportSorting=" + reportSorting + ", reportFolderPath=" + reportFolderPath + "]";
	}
}