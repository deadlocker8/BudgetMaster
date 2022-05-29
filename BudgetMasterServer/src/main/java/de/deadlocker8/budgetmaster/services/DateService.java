package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateService
{
	private final SettingsService settingsService;

	@Autowired
	public DateService(SettingsService settingsService)
	{
		this.settingsService = settingsService;
	}

	public String getDateStringNormal(LocalDate date)
	{
		return getDateString(date, DateFormatStyle.NORMAL);
	}

	public String getDateStringWithoutYear(LocalDate date)
	{
		return getDateString(date, DateFormatStyle.NO_YEAR);
	}

	public String getLongDateString(LocalDate date)
	{
		return getDateString(date, DateFormatStyle.LONG);
	}

	public String getDateStringWithMonthAndYear(LocalDate date)
	{
		return getDateString(date, DateFormatStyle.LONG_MONTH_AND_YEAR);
	}

	public String getDateTimeString(LocalDate date)
	{
		return getDateString(date, DateFormatStyle.DATE_TIME);
	}

	private String getDateString(LocalDate date, DateFormatStyle formatStyle)
	{
		return date.format(DateTimeFormatter.ofPattern(formatStyle.getKey()).withLocale(settingsService.getSettings().getLanguage().getLocale()));
	}

	public LocalDate getDateTimeFromCookie(String cookieDate)
	{
		if(cookieDate == null)
		{
			return LocalDate.now();
		}
		else
		{
			return LocalDate.parse(cookieDate, DateTimeFormatter.ofPattern(DateFormatStyle.NORMAL.getKey()).withLocale(settingsService.getSettings().getLanguage().getLocale()));
		}
	}

	public LocalDate getCurrentDate()
	{
		return LocalDate.now();
	}

	public String getDateTimeString(LocalDateTime localDateTime)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateFormatStyle.DATE_TIME.getKey()).withLocale(settingsService.getSettings().getLanguage().getLocale());
		return localDateTime.format(formatter);
	}
}
