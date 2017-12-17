package de.deadlocker8.budgetmasterserver.server.log;

import static spark.Spark.halt;

import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import logger.Logger;
import spark.Request;
import spark.Response;

public class LogDelete implements AdvancedRoute
{
	public LogDelete()
	{	
	
	}

	@Override
	public void before()
	{
	}

	@Override
	public Object handleRequest(Request req, Response res)
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

	@Override
	public void after()
	{
	}
}