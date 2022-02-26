package de.deadlocker8.budgetmaster.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateHelper
{
	private DateHelper()
	{
	}

	public static LocalDate getCurrentDate()
	{
		return LocalDate.now();
	}

	public static LocalDateTime getCurrentDateTime()
	{
		return LocalDateTime.now();
	}
}
