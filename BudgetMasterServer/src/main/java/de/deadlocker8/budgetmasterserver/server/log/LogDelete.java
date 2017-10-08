package de.deadlocker8.budgetmasterserver.server.log;

import static spark.Spark.halt;

import logger.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogDelete implements Route
{
	public LogDelete()
	{	
	
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
	{		
		try
		{							
			Logger.clearLogFile();	
			return "";
		}
		catch(IllegalStateException ex)
		{
			halt(500, "Internal Server Error");
		}
		
		return "";
	}
}