package de.deadlocker8.budgetmaster.logic.report;

import javafx.scene.control.TableColumn.SortType;

public class ReportSorting
{
	private ColumnType columnType;
	private SortType sortType;
	
	public ReportSorting()
	{
		
	}

	public ReportSorting(ColumnType columnType, SortType sortType)
	{
		this.columnType = columnType;
		this.sortType = sortType;
	}

	public ColumnType getColumnType()
	{
		return columnType;
	}

	public void setColumnType(ColumnType columnType)
	{
		this.columnType = columnType;
	}

	public SortType getSortType()
	{
		return sortType;
	}

	public void setSortType(SortType sortType)
	{
		this.sortType = sortType;
	}

	@Override
	public String toString()
	{
		return "ReportSorting [columnType=" + columnType + ", sortType=" + sortType + "]";
	}
}