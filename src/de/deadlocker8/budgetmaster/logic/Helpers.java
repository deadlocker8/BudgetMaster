package de.deadlocker8.budgetmaster.logic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Helpers
{
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
}