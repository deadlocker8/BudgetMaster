package de.deadlocker8.budgetmaster.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

public class DateHelper
{
	private DateHelper()
	{
	}

	public static DateTime getCurrentDate()
	{
		return DateTime.now(DateTimeZone.forTimeZone(TimeZone.getDefault()));
	}
}
