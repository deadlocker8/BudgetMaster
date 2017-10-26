package de.deadlocker8.budgetmaster.logic.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.scene.paint.Color;
import tools.Localization;

public class Helpers
{
	public static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.00");
	public static final String SALT = "ny9/Y+G|WrJ,82|oIYQQ X %i-sq#4,uA-qKPtwFPnw+s(k2`rV)^-a1|t{D3Z>S";
	public static final String ROADMAP_URL = "https://deadlocker.thecodelabs.de/roadmap/php/index.php?id=1";
	
	public static String getCurrencyString(int amount, String currency)
	{
		return String.valueOf(NUMBER_FORMAT.format(amount / 100.0).replace(".", ",")) + " " + currency;
	}

	public static String getCurrencyString(double amount, String currency)
	{
		return String.valueOf(NUMBER_FORMAT.format(amount).replace(".", ",")) + " " + currency;
	}
	
	public static String getURLEncodedString(String input)
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

	public static String getDateString(LocalDate date)
	{
		if(date == null)
		{
			return "";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return date.format(formatter);
	}

	public static ArrayList<String> getMonthList()
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

	public static ArrayList<String> getYearList()
	{
		ArrayList<String> years = new ArrayList<>();
		for(int i = 2000; i < 2100; i++)
		{
			years.add(String.valueOf(i));
		}
		return years;
	}
	
	public static ArrayList<Color> getCategoryColorList()
	{
	    ArrayList<Color> colors = new ArrayList<>();       
        colors.add(Colors.CATEGORIES_LIGHT_GREY);        
        colors.add(Colors.CATEGORIES_GREY);
        colors.add(Colors.CATEGORIES_DARK_GREY);
        colors.add(Colors.CATEGORIES_LIGHT_YELLOW);
        colors.add(Colors.CATEGORIES_YELLOW);
        colors.add(Colors.CATEGORIES_ORANGE);
        colors.add(Colors.CATEGORIES_RED);
        colors.add(Colors.CATEGORIES_DARK_RED);
        colors.add(Colors.CATEGORIES_PINK);
        colors.add(Colors.CATEGORIES_PURPLE);
        colors.add(Colors.CATEGORIES_DARK_PURPLE);
        colors.add(Colors.CATEGORIES_BLUE);
        colors.add(Colors.CATEGORIES_LIGHT_BLUE);
        colors.add(Colors.CATEGORIES_LIGHT_GREEN);
        colors.add(Colors.CATEGORIES_DARK_GREEN);    
        
        return colors;
	}
	
	public static FontIcon getFontIcon(FontIconType type, int size, Color color)
	{
	    FontIcon icon = new FontIcon(type);
	    icon.setSize(size);
	    icon.setColor(color);
	    
	    return icon;
	}
	
	/**
	 * Replaces line breaks and tabs with spaces
	 * @param text
	 * @return String
	 */
	public static String getFlatText(String text)
	{
		text = text.replace("\n", " ");
		text = text.replace("\t", " ");
		return text;
	}
}