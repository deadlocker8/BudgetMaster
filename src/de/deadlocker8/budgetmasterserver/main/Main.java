package de.deadlocker8.budgetmasterserver.main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import de.deadlocker8.budgetmasterserver.server.SparkServer;
import logger.LogLevel;
import logger.Logger;

public class Main
{
	public static void main(String[] args)
	{
		Logger.setLevel(LogLevel.ALL);		
		try
		{
			File logFile = new File(Paths.get(SparkServer.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().toFile() + "/error.log");
			Logger.enableFileOutput(logFile);
		}
		catch(URISyntaxException e1)
		{
			Logger.error(e1);
		}		
		
		if(!Files.exists(Paths.get("settings.properties")))
		{
			try
			{
				Files.copy(SparkServer.class.getClassLoader().getResourceAsStream("de/deadlocker8/budgetmasterserver/resources/settings.properties"), Paths.get("settings.properties"));
			}
			catch(IOException e)
			{
				Logger.error(e);
			}
		}

		Settings settings;
		try
		{
			settings = Utils.loadSettings();
			new SparkServer(settings);
		}
		catch(IOException e)
		{
			Logger.error(e);
		}
	}
}
