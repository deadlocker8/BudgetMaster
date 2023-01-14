package de.deadlocker8.budgetmaster.transactions.csvimport;

import java.util.Objects;

public final class CsvColumnSettings
{
	private final int columnDate;
	private String datePattern;
	private final int columnName;
	private final int columnAmount;
	private final int columnDescription;

	public CsvColumnSettings(int columnDate, String datePattern, int columnName, int columnAmount, int columnDescription)
	{
		this.columnDate = columnDate;
		this.datePattern = datePattern;
		this.columnName = columnName;
		this.columnAmount = columnAmount;
		this.columnDescription = columnDescription;
	}

	public int columnDate()
	{
		return columnDate;
	}

	public String getDatePattern()
	{
		return datePattern;
	}

	public void setDatePattern(String datePattern)
	{
		this.datePattern = datePattern;
	}

	public int columnName()
	{
		return columnName;
	}

	public int columnAmount()
	{
		return columnAmount;
	}

	public int columnDescription()
	{
		return columnDescription;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		CsvColumnSettings that = (CsvColumnSettings) o;
		return columnDate == that.columnDate && columnName == that.columnName && columnAmount == that.columnAmount && columnDescription == that.columnDescription && Objects.equals(datePattern, that.datePattern);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(columnDate, datePattern, columnName, columnAmount, columnDescription);
	}

	@Override
	public String toString()
	{
		return "CsvColumnSettings{" +
				"columnDate=" + columnDate +
				", datePattern='" + datePattern + '\'' +
				", columnName=" + columnName +
				", columnAmount=" + columnAmount +
				", columnDescription=" + columnDescription +
				'}';
	}
}
