package de.deadlocker8.budgetmaster.utils.types;

import de.thecodelabs.utils.util.Localization;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum MonthNames
{
	MONTH_JANUARY("month.january"),
	MONTH_FEBRUARY("month.february"),
	MONTH_MARCH("month.march"),
	MONTH_APRIL("month.april"),
	MONTH_MAY("month.may"),
	MONTH_JUNE("month.june"),
	MONTH_JULY("month.july"),
	MONTH_AUGUST("month.august"),
	MONTH_SEPTEMBER("month.september"),
	MONTH_OCTOBER("month.october"),
	MONTH_NOVEMBER("month.november"),
	MONTH_DECEMBER("month.december");

	private static List<String> localized;
	private String key;

	MonthNames(String key)
	{
		this.key = key;
	}

	public String getKey()
	{
		return key;
	}

	public static List<String> getLocalizedStrings()
	{
		if(localized == null)
		{
			localized = Stream.of(MonthNames.values())
					.map(monthName -> Localization.getString(monthName.getKey()))
					.collect(Collectors.toList());
		}

		return localized;
	}
}
