package de.deadlocker8.budgetmaster.transactions.csvimport;

import java.util.List;
import java.util.Objects;

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
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		CsvRow csvRow = (CsvRow) o;
		return Objects.equals(columns, csvRow.columns);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(columns);
	}

	@Override
	public String toString()
	{
		return "CsvRow{" +
				"columns=" + columns +
				'}';
	}
}


