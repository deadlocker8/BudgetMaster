package de.deadlocker8.budgetmaster.database.model.v10;

import de.deadlocker8.budgetmaster.database.model.BackupInfo;

import java.util.Objects;

public class BackupCsvImportSettings_v10 implements BackupInfo
{
	private String separator;
	private String encoding;
	private int numberOfLinesToSkip;

	// column settings
	private Integer columnDate;
	private String datePattern;
	private Integer columnName;
	private Integer columnAmount;
	private String decimalSeparator;
	private String groupingSeparator;
	private Integer columnDescription;

	public BackupCsvImportSettings_v10()
	{
		// for GSON
	}

	public String getSeparator()
	{
		return separator;
	}

	public void setSeparator(String separator)
	{
		this.separator = separator;
	}

	public String getEncoding()
	{
		return encoding;
	}

	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}

	public int getNumberOfLinesToSkip()
	{
		return numberOfLinesToSkip;
	}

	public void setNumberOfLinesToSkip(int numberOfLinesToSkip)
	{
		this.numberOfLinesToSkip = numberOfLinesToSkip;
	}

	public Integer getColumnDate()
	{
		return columnDate;
	}

	public void setColumnDate(Integer columnDate)
	{
		this.columnDate = columnDate;
	}

	public String getDatePattern()
	{
		return datePattern;
	}

	public void setDatePattern(String datePattern)
	{
		this.datePattern = datePattern;
	}

	public Integer getColumnName()
	{
		return columnName;
	}

	public void setColumnName(Integer columnName)
	{
		this.columnName = columnName;
	}

	public Integer getColumnAmount()
	{
		return columnAmount;
	}

	public void setColumnAmount(Integer columnAmount)
	{
		this.columnAmount = columnAmount;
	}

	public String getDecimalSeparator()
	{
		return decimalSeparator;
	}

	public void setDecimalSeparator(String decimalSeparator)
	{
		this.decimalSeparator = decimalSeparator;
	}

	public String getGroupingSeparator()
	{
		return groupingSeparator;
	}

	public void setGroupingSeparator(String groupingSeparator)
	{
		this.groupingSeparator = groupingSeparator;
	}

	public Integer getColumnDescription()
	{
		return columnDescription;
	}

	public void setColumnDescription(Integer columnDescription)
	{
		this.columnDescription = columnDescription;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupCsvImportSettings_v10 that = (BackupCsvImportSettings_v10) o;
		return numberOfLinesToSkip == that.numberOfLinesToSkip && Objects.equals(separator, that.separator) && Objects.equals(encoding, that.encoding) && Objects.equals(columnDate, that.columnDate) && Objects.equals(datePattern, that.datePattern) && Objects.equals(columnName, that.columnName) && Objects.equals(columnAmount, that.columnAmount) && Objects.equals(decimalSeparator, that.decimalSeparator) && Objects.equals(groupingSeparator, that.groupingSeparator) && Objects.equals(columnDescription, that.columnDescription);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(separator, encoding, numberOfLinesToSkip, columnDate, datePattern, columnName, columnAmount, decimalSeparator, groupingSeparator, columnDescription);
	}

	@Override
	public String toString()
	{
		return "BackupCsvImportSettings_v10{" +
				"separator='" + separator + '\'' +
				", encoding='" + encoding + '\'' +
				", numberOfLinesToSkip=" + numberOfLinesToSkip +
				", columnDate=" + columnDate +
				", datePattern='" + datePattern + '\'' +
				", columnName=" + columnName +
				", columnAmount=" + columnAmount +
				", decimalSeparator='" + decimalSeparator + '\'' +
				", groupingSeparator='" + groupingSeparator + '\'' +
				", columnDescription=" + columnDescription +
				'}';
	}
}
