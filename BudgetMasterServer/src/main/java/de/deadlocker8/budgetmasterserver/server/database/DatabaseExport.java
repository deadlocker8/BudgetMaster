package de.deadlocker8.budgetmasterserver.server.database;

import static spark.Spark.halt;

import com.google.gson.Gson;

import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.Settings;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseExporter;
import logger.Logger;
import spark.Request;
import spark.Response;

public class DatabaseExport implements AdvancedRoute
{
	private Settings settings;
	private Gson gson;

	public DatabaseExport(Settings settings, Gson gson)
	{
		this.settings = settings;
		this.gson = gson;
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
		    DatabaseExporter exporter = new DatabaseExporter(settings);	
			return gson.toJson(exporter.exportDatabase());
		}
		catch(Exception e)
		{
		    Logger.error(e);
			halt(500, "Internal Server Error");
		}		
		
		return "";
	}

	@Override
	public void after()
	{
	}
}