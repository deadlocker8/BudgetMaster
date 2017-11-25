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
				Logger.warning("No settings file found! Creating default settings file...");
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
				boolean settingsChanged = false;
				if(settings.getDatabaseType() == null)
				{
					settings.setDatabaseType("mysql");
					settingsChanged = true;
				}
				
				if(!settings.getDatabaseType().equals("mysql") && !settings.getDatabaseType().equals("sqlite"))
				{
					Logger.error(settings.getDatabaseType() + " is no valid database type! (allowed types are: mysql and sqlite)");
					return;
				}
				
				if(settings.getDatabaseType().equals("sqlite") && (settings.getDatabaseUrl() == null || settings.getDatabaseUrl().equals("")))
				{
					Logger.warning("There is no save path  specified for the sqlite database file. It will be saved as \"BudgetMaster.db\" in current directory.");
					settings.setDatabaseUrl(System.getProperty("user.dir").replace("\\", "/") + "/BudgetMaster.db");
					settingsChanged = true;
				}
				
				if(settings.getDatabaseUrl().contains("jdbc"))
				{
					settings.setDatabaseUrl(settings.getDatabaseUrl().replace("jdbc:mysql://", ""));
					settingsChanged = true;
				}
				
				
				if(settingsChanged)
				{
					Logger.warning("Settings file is not up to date! Updated settings to new version.");
					Utils.saveSettings(settings);
				}
				
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