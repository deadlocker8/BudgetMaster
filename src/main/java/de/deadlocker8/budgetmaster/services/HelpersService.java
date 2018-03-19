package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import de.deadlocker8.budgetmaster.utils.Colors;
import de.deadlocker8.budgetmaster.utils.Strings;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.ConvertTo;
import tools.Localization;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;

@Service
public class HelpersService
{
	private final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.00");
	@Autowired
	private SettingsRepository settingsRepository;
	
	public String getCurrencyString(int amount)
	{
		return String.valueOf(NUMBER_FORMAT.format(amount / 100.0).replace(".", ",")) + " " + settingsRepository.findOne(0).getCurrency();
	}

	public String getCurrencyString(double amount)
	{
		return String.valueOf(NUMBER_FORMAT.format(amount).replace(".", ",")) + " " + settingsRepository.findOne(0).getCurrency();
	}
	
	public String getURLEncodedString(String input)
	{
		try
		{
			return URLEncoder.encode(input, "UTF-8");
		}
		catch(UnsupportedEncodingException e)
		{
			return input;
		}
	}

	public String getDateString(DateTime date)
	{
		return date.toString(DateTimeFormat.forPattern("dd.MM.yy").withLocale(settingsRepository.findOne(0).getLanguage().getLocale()));
	}

	public String getDateStringWithMonthAndYear(DateTime date)
	{
		return date.toString(DateTimeFormat.forPattern("MMMM yyyy").withLocale(settingsRepository.findOne(0).getLanguage().getLocale()));
	}

	public ArrayList<String> getWeekDays()
	{
		ArrayList<String> weekDays = new ArrayList<>();
		weekDays.add(Localization.getString(Strings.SUNDAY));
		weekDays.add(Localization.getString(Strings.MONDAY));
		weekDays.add(Localization.getString(Strings.TUESDAY));
		weekDays.add(Localization.getString(Strings.WEDNESDAY));
		weekDays.add(Localization.getString(Strings.THURSDAY));
		weekDays.add(Localization.getString(Strings.FRIDAY));
		weekDays.add(Localization.getString(Strings.SATURDAY));
		return weekDays;
	}

	public ArrayList<String> getMonthList()
	{
		ArrayList<String> monthNames = new ArrayList<>();
		monthNames.add(Localization.getString(Strings.MONTH_JANUARY));
		monthNames.add(Localization.getString(Strings.MONTH_FEBRUARY));
		monthNames.add(Localization.getString(Strings.MONTH_MARCH));
		monthNames.add(Localization.getString(Strings.MONTH_APRIL));
		monthNames.add(Localization.getString(Strings.MONTH_MAY));
		monthNames.add(Localization.getString(Strings.MONTH_JUNE));
		monthNames.add(Localization.getString(Strings.MONTH_JULY));
		monthNames.add(Localization.getString(Strings.MONTH_AUGUST));
		monthNames.add(Localization.getString(Strings.MONTH_SEPTEMBER));
		monthNames.add(Localization.getString(Strings.MONTH_OCTOBER));
		monthNames.add(Localization.getString(Strings.MONTH_NOVEMBER));
		monthNames.add(Localization.getString(Strings.MONTH_DECEMBER));
		return monthNames;
	}

	public ArrayList<Integer> getYearList()
	{
		ArrayList<Integer> years = new ArrayList<>();
		for(int i = 2000; i < 2100; i++)
		{
			years.add(i);
		}
		return years;
	}
	
	/**
	 * Replaces line breaks and tabs with spaces
	 * @param text
	 * @return String
	 */
	public String getFlatText(String text)
	{
		text = text.replace("\n", " ");
		text = text.replace("\t", " ");
		return text;
	}

	public ArrayList<String> getCategoryColorList()
	{
		ArrayList<String> categoryColors = new ArrayList<>();
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREY).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_GREY).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_DARK_GREY).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_YELLOW).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_YELLOW).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_ORANGE).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_RED).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_DARK_RED).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_PINK).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_PURPLE).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_DARK_PURPLE).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_BLUE).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_SOFT_BLUE).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_BLUE).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREEN).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIME_GREEN).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_DARK_GREEN).toLowerCase());

		return categoryColors;
	}
}