package de.deadlocker8.budgetmasterserver.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;

public class Utils
{
	public static Settings loadSettings()
	{
		String settingsJSON;
		Settings settings;
		try
		{
			Gson gson = new Gson();
			settingsJSON = new String(Files.readAllBytes(Paths.get("settings.properties")));				
			settings = gson.fromJson(settingsJSON, Settings.class);	
			return settings;
		}
		catch(IOException e)
		{
			//ERRORHANDLING
			e.printStackTrace();
			return null;
		}
	}
}