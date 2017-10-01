package de.deadlocker8.budgetmaster.logic.serverconnection;

import java.net.UnknownHostException;

import de.deadlocker8.budgetmaster.logic.utils.Strings;
import tools.Localization;

public class ExceptionHandler
{
	public static String getMessageForException(Exception e)
	{
		if(e instanceof ServerConnectionException)
		{
			return handleServerConnectionException(e);
		}			
		
		if(e instanceof UnknownHostException)
		{
			return Localization.getString(Strings.ERROR_UNKNOWN_HOST);
		}
		
		if(e.getMessage() == null)
		{
		    return Localization.getString(Strings.ERROR_UNKNOWN_ERROR, e.getClass());
		}
				
		if(e.getMessage().contains("Connection refused"))
		{
			return Localization.getString(Strings.ERROR_CONNECTION_REFUSED);
		}
		else if(e.getMessage().contains("HTTPS hostname wrong"))
		{
		    return Localization.getString(Strings.ERROR_HTTPS_HOSTNAME_WRONG);
		}
		return e.getMessage();
	}
	
	private static String handleServerConnectionException(Exception e)
	{
		switch(e.getMessage())
		{
			case "400": return Localization.getString(Strings.ERROR_400);
			case "401": return Localization.getString(Strings.ERROR_401);
			case "500": return Localization.getString(Strings.ERROR_500);
			default: return e.getMessage();
		}
	}
}