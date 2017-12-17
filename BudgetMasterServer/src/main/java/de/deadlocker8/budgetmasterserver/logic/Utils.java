package de.deadlocker8.budgetmasterserver.logic;

import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.deadlocker8.budgetmasterserver.logic.database.creator.DatabaseCreator;
import de.deadlocker8.budgetmasterserver.logic.database.creator.MysqlDatabaseCreator;
import de.deadlocker8.budgetmasterserver.logic.database.creator.SqliteDatabaseCreator;
import de.deadlocker8.budgetmasterserver.logic.database.handler.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.logic.database.handler.MysqlDatabaseHandler;
import de.deadlocker8.budgetmasterserver.logic.database.handler.SqliteDatabaseHandler;
import de.deadlocker8.budgetmasterserver.logic.database.taghandler.DatabaseTagHandler;
import de.deadlocker8.budgetmasterserver.logic.database.taghandler.MysqlDatabaseTagHandler;
import de.deadlocker8.budgetmasterserver.logic.database.taghandler.SqliteDatabaseTagHandler;

public class Utils
{
	public static Settings loadSettings() throws IOException, URISyntaxException
	{
		String settingsJSON;
		Settings settings;

		Gson gson = new Gson();

		settingsJSON = new String(Files.readAllBytes(Paths.get(Settings.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().resolve("settings.json")));
		settings = gson.fromJson(settingsJSON, Settings.class);
		return settings;
	}

	public static void saveSettings(Settings settings) throws IOException, URISyntaxException
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonString = gson.toJson(settings);
		Writer writer = Files.newBufferedWriter(Paths.get(Settings.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().resolve("settings.json"));
		writer.write(jsonString);
		writer.close();
	}

	public static Connection getDatabaseConnection(Settings settings) throws SQLException, ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC");
		
		if(settings.getDatabaseType().equals("mysql"))
		{
			return DriverManager.getConnection("jdbc:mysql://" + settings.getDatabaseUrl() + settings.getDatabaseName() + "?useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin&autoReconnect=true&wait_timeout=86400", settings.getDatabaseUsername(), settings.getDatabasePassword());
		}
		else		
		{
			return DriverManager.getConnection("jdbc:sqlite://" + settings.getDatabaseUrl());
		}
	}

	public static DatabaseCreator getDatabaseCreator(Connection connection, Settings settings)
	{
		if(settings.getDatabaseType().equals("mysql"))
		{
			return new MysqlDatabaseCreator(connection, settings);
		}
		else
		{
			return new SqliteDatabaseCreator(connection, settings);
		}
	}

	public static DatabaseHandler getDatabaseHandler(Settings settings) throws ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC");
		
		if(settings.getDatabaseType().equals("mysql"))
		{
			return new MysqlDatabaseHandler(settings);
		}
		else
		{
			return new SqliteDatabaseHandler(settings);
		}
	}

	public static DatabaseTagHandler getDatabaseTagHandler(Settings settings) throws ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC");
		
		if(settings.getDatabaseType().equals("mysql"))
		{
			return new MysqlDatabaseTagHandler(settings);
		}
		else
		{
			return new SqliteDatabaseTagHandler(settings);
		}
	}
}