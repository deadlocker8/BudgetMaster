package de.deadlocker8.budgetmasterserver.main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import de.deadlocker8.budgetmaster.logic.updater.VersionInformation;
import de.deadlocker8.budgetmasterserver.logic.Settings;
import de.deadlocker8.budgetmasterserver.logic.Utils;
import de.deadlocker8.budgetmasterserver.server.SparkServer;
import logger.FileOutputMode;
import logger.LogLevel;
import logger.Logger;
import tools.Localization;

public class Main
{
	public static void main(String[] args)
	{
		//for category.none in class Category
		Localization.init("de/deadlocker8/budgetmasterserver/");
		Localization.loadLanguage(Locale.ENGLISH);
		
		Logger.setLevel(LogLevel.ALL);		
		Logger.appInfo(Localization.getString("app.name"), 
						Localization.getString("version.name"), 
						Localization.getString("version.code"), 
						Localization.getString("version.date"));
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
					Files.copy(SparkServer.class.getClassLoader().getResourceAsStream("de/deadlocker8/budgetmasterserver/settings.json"), settingsPath);
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
				VersionInformation versionInfo = new VersionInformation();
				versionInfo.setVersionCode(Integer.parseInt(Localization.getString("version.code")));
				versionInfo.setVersionName(Localization.getString("version.name"));
				versionInfo.setDate(Localization.getString("version.date"));
				
				new SparkServer(settings, versionInfo);
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