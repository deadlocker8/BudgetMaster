package de.deadlocker8.budgetmasterserver.server.rest;

import static spark.Spark.halt;

import com.google.gson.Gson;

import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.database.handler.DatabaseHandler;
import spark.Request;
import spark.Response;

public class RestGet implements AdvancedRoute
{
	private DatabaseHandler handler;
	private Gson gson;

	public RestGet(DatabaseHandler handler, Gson gson)
	{
		this.handler = handler;
		this.gson = gson;
	}

	@Override
	public void before()
	{
		handler.connect();
	}

	@Override
	public Object handleRequest(Request req, Response res)
	{
		if(!req.queryParams().contains("year") || !req.queryParams().contains("month"))
		{
			halt(400, "Bad Request");
		}
		
		int year = 0;
		int month = 0;
		
		try
		{				
			year = Integer.parseInt(req.queryMap("year").value());
			month = Integer.parseInt(req.queryMap("month").value());
			
			if(year < 0 || month < 1 || month > 12)
			{
				halt(400, "Bad Request");
			}
			
			try
			{			
				int rest = handler.getRestForAllPreviousMonths(year, month);	
				
				return gson.toJson(rest);
			}
			catch(IllegalStateException ex)
			{
				halt(500, "Internal Server Error");
			}
		}
		catch(Exception e)
		{
			halt(400, "Bad Request");
		}
		
		return null;
	}

	@Override
	public void after()
	{
		handler.closeConnection();		
	}
}