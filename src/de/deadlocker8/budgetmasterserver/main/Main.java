package de.deadlocker8.budgetmasterserver.main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;

import de.deadlocker8.budgetmasterserver.logic.Settings;
import de.deadlocker8.budgetmasterserver.logic.Utils;
import de.deadlocker8.budgetmasterserver.server.SparkServer;
import logger.FileOutputMode;
import logger.LogLevel;
import logger.Logger;
import tools.Localization;

public class Main
{
	//for server specific version information
	private static ResourceBundle bundle = ResourceBundle.getBundle("de/deadlocker8/budgetmasterserver/main/", Locale.GERMAN);

	public static void main(String[] args)
	{
		//for category.none in class Category
		Localization.init("de/deadlocker8/budgetmaster/resources/languages/");
		Localization.loadLanguage(Locale.ENGLISH);
		
		Logger.setLevel(LogLevel.ALL);		
		Logger.appInfo(bundle.getString("app.name"), bundle.getString("version.name"), bundle.getString("version.code"), bundle.getString("version.date"));
		try
		{
			File logFolder = Paths.get(SparkServer.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().toFile();
			Logger.enableFileOutput(logFolder, System.out, System.err, FileOutputMode.COMBINED);
		}
		catch(URISyntaxException e1)
		{
			Logger.error(e1);
		}	
		
		try
		{
			Path settingsPath = Paths.get(Settings.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().resolve("settings.json");
			
			if(!Files.exists(settingsPath))
			{
				try
				{
					Files.copy(SparkServer.class.getClassLoader().getResourceAsStream("de/deadlocker8/budgetmasterserver/resources/settings.json"), settingsPath);
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
				new SparkServer(settings, bundle.getString("version.code"));
			}
			catch(IOException | URISyntaxException e)
			{
				Logger.error(e);
			}
		}
		catch(URISyntaxException e1)
		{			
			Logger.error(e1);			
		}		
	}
}