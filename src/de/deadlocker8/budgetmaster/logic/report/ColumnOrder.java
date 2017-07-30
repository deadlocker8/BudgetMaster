package de.deadlocker8.budgetmaster.logic.report;

import java.util.ArrayList;

public class ColumnOrder
{
	private ArrayList<ColumnType> columns;

	public ColumnOrder()
	{
		columns = new ArrayList<ColumnType>();
	}

	public ArrayList<ColumnType> getColumns()
	{
		return columns;
	}
	
	public void addColumn(ColumnType column)
	{
		columns.add(column);
	}
}