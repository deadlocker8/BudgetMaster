package de.deadlocker8.budgetmaster.transactions.csvimport;

import java.util.Objects;

public final class CsvColumnSettings
{
	private final int columnDate;
	private final String datePattern;
	private final int columnName;
	private final int columnAmount;
	private final String decimalSeparator;
	private final String groupingSeparator;
	private final int columnDescription;

	public CsvColumnSettings(int columnDate, String datePattern, int columnName, int columnAmount, String decimalSeparator, String groupingSeparator, int columnDescription)
	{
		this.columnDate = columnDate;
		this.datePattern = datePattern;
		this.columnName = columnName;
		this.columnAmount = columnAmount;
		this.decimalSeparator = decimalSeparator;
		this.groupingSeparator = groupingSeparator;
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

	public int columnName()
	{
		return columnName;
	}

	public int columnAmount()
	{
		return columnAmount;
	}

	public String getDecimalSeparator()
	{
		return decimalSeparator;
	}

	public String getGroupingSeparator()
	{
		return groupingSeparator;
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
		return columnDate == that.columnDate && columnName == that.columnName && columnAmount == that.columnAmount && columnDescription == that.columnDescription && Objects.equals(datePattern, that.datePattern) && Objects.equals(decimalSeparator, that.decimalSeparator) && Objects.equals(groupingSeparator, that.groupingSeparator);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(columnDate, datePattern, columnName, columnAmount, decimalSeparator, groupingSeparator, columnDescription);
	}

	@Override
	public String toString()
	{
		return "CsvColumnSettings{" +
				"columnDate=" + columnDate +
				", datePattern='" + datePattern + '\'' +
				", columnName=" + columnName +
				", columnAmount=" + columnAmount +
				", decimalSeparator='" + decimalSeparator + '\'' +
				", groupingSeparator='" + groupingSeparator + '\'' +
				", columnDescription=" + columnDescription +
				'}';
	}
}
