package de.deadlocker8.budgetmaster.services;

public enum DateFormatStyle
{
	NORMAL("dd.MM.yy"),
	NO_YEAR("dd.MM."),
	LONG("dd.MM.yyyy"),
	LONG_MONTH_AND_YEAR("MMMM yyyy");

	private String key;

	DateFormatStyle(String key)
	{
		this.key = key;
	}

	public String getKey()
	{
		return key;
	}
}
