package de.deadlocker8.budgetmasterserver.server.database;

import static spark.Spark.halt;

import com.google.gson.Gson;

import de.deadlocker8.budgetmasterserver.logic.database.Database;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseImporter;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseTagHandler;
import logger.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

public class DatabaseImport implements Route
{
	private DatabaseHandler handler;
	private DatabaseTagHandler tagHandler; 
	private Gson gson;

	public DatabaseImport(DatabaseHandler handler, DatabaseTagHandler tagHandler, Gson gson)
	{
		this.handler = handler;
		this.tagHandler = tagHandler;
		this.gson = gson;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
	{	
		String databaseJSON = req.body();		
		
		try
		{
			Database database = gson.fromJson(databaseJSON, Database.class);

			DatabaseImporter importer = new DatabaseImporter(handler, tagHandler);
			importer.importDatabase(database);
			return "";
		}
		catch(Exception e)
		{
			Logger.error(e);
			halt(500, "Internal Server Error");
		}
		
		return "";
	}
}