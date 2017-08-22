package de.deadlocker8.budgetmasterserver.server.database;

import static spark.Spark.halt;

import com.google.gson.Gson;

import de.deadlocker8.budgetmasterserver.logic.Settings;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseExporter;
import logger.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

public class DatabaseExport implements Route
{
	private Settings settings;
	private Gson gson;

	public DatabaseExport(Settings settings, Gson gson)
	{
		this.settings = settings;
		this.gson = gson;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
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
}