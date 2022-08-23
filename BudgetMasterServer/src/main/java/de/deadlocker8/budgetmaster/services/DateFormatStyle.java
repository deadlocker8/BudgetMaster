package de.deadlocker8.budgetmaster.services;

public enum DateFormatStyle
{
	NORMAL("dd.MM.yy"),
	NO_YEAR("dd.MM."),
	LONG("dd.MM.yyyy"),
	LONG_MONTH_AND_YEAR("MMMM yyyy"),
	DATE_TIME("dd.MM.yyyy HH:mm"),
	LONG_WITH_MONTH_NAME("dd. MMMM yyyy");

	private final String key;

	DateFormatStyle(String key)
	{
		this.key = key;
	}

	public String getKey()
	{
		return key;
	}
}
