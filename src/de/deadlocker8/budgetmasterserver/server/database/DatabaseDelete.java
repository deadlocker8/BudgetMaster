package de.deadlocker8.budgetmasterserver.server.database;

import static spark.Spark.halt;

import de.deadlocker8.budgetmasterserver.main.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.main.Settings;
import spark.Request;
import spark.Response;
import spark.Route;

public class DatabaseDelete implements Route
{
	private DatabaseHandler handler;
	private Settings settings;
	
	public DatabaseDelete(DatabaseHandler handler, Settings settings)
	{	
		this.handler = handler;
		this.settings = settings;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
	{		
		try
		{							
			handler.deleteDatabase();
			handler = new DatabaseHandler(settings);
	
			return "";
		}
		catch(IllegalStateException ex)
		{
			halt(500, "Internal Server Error");
		}
		
		return "EIMER";
	}
}