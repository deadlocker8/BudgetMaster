package de.deadlocker8.budgetmasterserver.server.database;

import static spark.Spark.halt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.Settings;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseCreator;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseHandler;
import logger.Logger;
import spark.Request;
import spark.Response;

public class DatabaseDelete implements AdvancedRoute
{
	private DatabaseHandler handler;
	private Settings settings;
	
	public DatabaseDelete(DatabaseHandler handler, Settings settings)
	{	
		this.handler = handler;
		this.settings = settings;
	}

	@Override
	public void before()
	{
		handler.connect();
	}

	@Override
	public Object handleRequest(Request req, Response res)
	{
		try
		{							
			handler.deleteDatabase();
			Connection connection = DriverManager.getConnection(settings.getDatabaseUrl() + settings.getDatabaseName() + "?useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin&autoReconnect=true&wait_timeout=86400", settings.getDatabaseUsername(), settings.getDatabasePassword());
			new DatabaseCreator(connection, settings);
			Logger.info("Successfully initialized database (" + settings.getDatabaseUrl() + settings.getDatabaseName() + ")");
	
			return "";
		}
		catch(IllegalStateException | SQLException ex)
		{
			halt(500, "Internal Server Error");
		}
		
		return "";
	}

	@Override
	public void after()
	{
		handler.closeConnection();
	}
}