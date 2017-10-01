package de.deadlocker8.budgetmaster.logic.report;

import java.util.HashSet;

public class ColumnFilter
{
	private HashSet<ColumnType> columns;

	public ColumnFilter()
	{
		columns = new HashSet<ColumnType>();
	}
	
	public void addColumn(ColumnType column)
	{
		columns.add(column);
	}
	
	public void removeColumn(ColumnType column)
	{
		columns.remove(column);
	}
	
	public void toggleColumn(ColumnType column, boolean add)
	{
		if(add)
		{
			columns.add(column);
		}
		else
		{
			columns.remove(column);
		}
	}
	
	public HashSet<ColumnType> getColumns()
	{
		return columns;
	}
	
	public boolean containsColumn(ColumnType column)
	{
		return columns.contains(column);
	}
}