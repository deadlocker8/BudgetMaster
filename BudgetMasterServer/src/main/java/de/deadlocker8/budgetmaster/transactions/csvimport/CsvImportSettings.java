package de.deadlocker8.budgetmaster.transactions.csvimport;


import de.deadlocker8.budgetmaster.utils.ProvidesID;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class CsvImportSettings implements ProvidesID
{
	@Id
	private Integer ID = 1;
	private String separatorChar;
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

	public CsvImportSettings()
	{
		// empty
	}

	public static CsvImportSettings getDefault()
	{
		CsvImportSettings defaultSettings = new CsvImportSettings();
		defaultSettings.setSeparatorChar(";");
		defaultSettings.setEncoding("UTF-8");
		defaultSettings.setNumberOfLinesToSkip(0);

		defaultSettings.setColumnDate(null);
		defaultSettings.setDatePattern("dd.MM.yyyy");
		defaultSettings.setColumnName(null);
		defaultSettings.setColumnAmount(null);
		defaultSettings.setDecimalSeparator(".");
		defaultSettings.setDecimalSeparator(",");
		defaultSettings.setColumnDescription(null);

		return defaultSettings;
	}

	@Override
	public Integer getID()
	{
		return null;
	}

	@Override
	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	public String getSeparatorChar()
	{
		return separatorChar;
	}

	public void setSeparatorChar(String separator)
	{
		this.separatorChar = separator;
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
		CsvImportSettings settings = (CsvImportSettings) o;
		return ID == settings.ID && numberOfLinesToSkip == settings.numberOfLinesToSkip && Objects.equals(separatorChar, settings.separatorChar) && Objects.equals(encoding, settings.encoding) && Objects.equals(columnDate, settings.columnDate) && Objects.equals(datePattern, settings.datePattern) && Objects.equals(columnName, settings.columnName) && Objects.equals(columnAmount, settings.columnAmount) && Objects.equals(decimalSeparator, settings.decimalSeparator) && Objects.equals(groupingSeparator, settings.groupingSeparator) && Objects.equals(columnDescription, settings.columnDescription);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, separatorChar, encoding, numberOfLinesToSkip, columnDate, datePattern, columnName, columnAmount, decimalSeparator, groupingSeparator, columnDescription);
	}

	@Override
	public String toString()
	{
		return "CsvImportSettings{" +
				"ID=" + ID +
				", separator='" + separatorChar + '\'' +
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
