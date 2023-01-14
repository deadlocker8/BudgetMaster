package de.deadlocker8.budgetmaster.transactions.csvimport;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Optional;

public class DateParser
{

	private DateParser()
	{
	}

	public static Optional<LocalDate> parse(String dateString, String pattern, Locale locale)
	{
		if(dateString == null || pattern == null || locale == null)
		{
			return Optional.empty();
		}

		try
		{
			return Optional.of(LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern).withLocale(locale)));
		}
		catch(DateTimeParseException e)
		{
			return Optional.empty();
		}
	}
}
