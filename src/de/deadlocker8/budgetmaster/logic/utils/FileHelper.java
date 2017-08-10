package de.deadlocker8.budgetmaster.logic.utils;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.Settings;
import de.deadlocker8.budgetmasterserver.logic.Database;
import tools.PathUtils;

public class FileHelper
{
	private static final ResourceBundle bundle = ResourceBundle.getBundle("de/deadlocker8/budgetmaster/main/", Locale.GERMANY);
	
	public static Settings loadSettings()
	{
		Settings settings;
		try
		{
			Gson gson = new Gson();			
			Reader reader = Files.newBufferedReader(Paths.get(PathUtils.getOSindependentPath() + bundle.getString("folder") + "/settings.json"), Charset.forName("UTF-8"));
			settings = gson.fromJson(reader, Settings.class);	
			reader.close();
			return settings;
		}
		catch(IOException e)
		{
			return null;
		}
	}
	
	public static void saveSettings(Settings settings) throws IOException
	{		
		Gson gson = new Gson();
		String jsonString = gson.toJson(settings);
		PathUtils.checkFolder(new File(PathUtils.getOSindependentPath() + bundle.getString("folder")));
		Writer writer = Files.newBufferedWriter(Paths.get(PathUtils.getOSindependentPath() + bundle.getString("folder")  + "/settings.json"), Charset.forName("UTF-8"));
		writer.write(jsonString);
		writer.close();
	}
	
	public static Database loadDatabaseJSON(File file) throws IOException
	{	
		Gson gson = new Gson();
		Reader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()), Charset.forName("UTF-8"));		
		Database database = gson.fromJson(reader, Database.class);	
		reader.close();
		return database;		
	}
	
	public static void saveDatabaseJSON(File file, String databaseJSON) throws IOException
	{		
		Writer writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()), Charset.forName("UTF-8"));
		writer.write(databaseJSON);
		writer.close();
	}
}