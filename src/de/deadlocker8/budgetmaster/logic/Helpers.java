package de.deadlocker8.budgetmaster.logic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Helpers
{
	public static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.00");
	public static final String SALT = "ny9/Y+G|WrJ,82|oIYQQ X %i-sq#4,uA-qKPtwFPnw+s(k2`rV)^-a1|t{D3Z>S";
	
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
}