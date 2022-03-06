package de.deadlocker8.budgetmaster.services;

public enum DateFormatStyle
{
	NORMAL("dd.MM.yy"),
	NO_YEAR("dd.MM."),
	LONG("dd.MM.yyyy"),
	LONG_MONTH_AND_YEAR("MMMM yyyy"),
	DATE_TIME("dd.MM.yyyy HH:mm");

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
