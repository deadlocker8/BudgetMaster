package de.deadlocker8.budgetmaster;

import de.deadlocker8.budgetmaster.utils.Strings;
import de.thecodelabs.utils.util.Localization;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum WeekDay
{
	SUNDAY("sunday"),
	MONDAY("monday"),
	TUESDAY("tuesday"),
	WEDNESDAY("wednesday"),
	THURSDAY("thursday"),
	FRIDAY("friday"),
	SATURDAY("saturday");

	private static List<String> localized;
	private String key;

	WeekDay(String key)
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
			localized = Stream.of(WeekDay.values())
					.map(weekDay -> Localization.getString(weekDay.getKey()))
					.collect(Collectors.toList());
		}

		return localized;
	}
}
