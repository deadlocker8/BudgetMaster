package de.deadlocker8.budgetmasterserver.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;

public class Utils
{
	public static Settings loadSettings() throws IOException
	{
		String settingsJSON;
		Settings settings;
		
		Gson gson = new Gson();
		settingsJSON = new String(Files.readAllBytes(Paths.get("settings.json")));		
		settings = gson.fromJson(settingsJSON, Settings.class);	
		return settings;		
	}
}