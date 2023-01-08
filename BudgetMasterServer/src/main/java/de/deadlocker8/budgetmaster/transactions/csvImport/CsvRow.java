package de.deadlocker8.budgetmaster.transactions.csvImport;

import java.util.List;

public class CsvRow
{
	private final List<String> columns;

	public CsvRow(List<String> columns)
	{
		this.columns = columns;
	}

	public CsvRow(String... columns)
	{
		this.columns = List.of(columns);
	}

	public List<String> getColumns()
	{
		return columns;
	}

	@Override
	public String toString()
	{
		return "CsvRow{" +
				"columns=" + columns +
				'}';
	}
}


