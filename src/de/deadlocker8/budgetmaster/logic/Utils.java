package de.deadlocker8.budgetmaster.logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;

import com.google.gson.Gson;

import tools.PathUtils;

public class Utils
{
	private static final ResourceBundle bundle = ResourceBundle.getBundle("de/deadlocker8/budgetmaster/main/", Locale.GERMANY);
	
	public static Settings loadSettings()
	{
		String settingsJSON;
		Settings settings;
		try
		{
			Gson gson = new Gson();
			PathUtils.checkFolder(new File(PathUtils.getOSindependentPath() + bundle.getString("folder")));
			settingsJSON = new String(Files.readAllBytes(Paths.get(PathUtils.getOSindependentPath() + bundle.getString("folder") + "/settings.json")));				
			settings = gson.fromJson(settingsJSON, Settings.class);	
			return settings;
		}
		catch(IOException e)
		{
			return null;
		}
	}
	
	public static void saveSettings(Settings settings)
	{
		try
		{
			Gson gson = new Gson();
			String jsonString = gson.toJson(settings);
			
			Files.write(Paths.get(PathUtils.getOSindependentPath() + bundle.getString("folder")  + "/settings.json"), jsonString.getBytes());				
		}
		catch(IOException e)
		{
			//ERRORHANDLING
			e.printStackTrace();			
		}
	}
}