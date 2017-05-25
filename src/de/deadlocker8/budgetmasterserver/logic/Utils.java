package de.deadlocker8.budgetmasterserver.logic;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;

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
}