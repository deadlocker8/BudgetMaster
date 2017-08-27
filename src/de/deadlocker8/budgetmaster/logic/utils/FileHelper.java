package de.deadlocker8.budgetmaster.logic.utils;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.Settings;
import de.deadlocker8.budgetmasterserver.logic.database.Database;
import tools.Localization;
import tools.PathUtils;

public class FileHelper
{	
	public static Settings loadSettings()
	{
		Settings settings;
		try
		{
			Gson gson = new Gson();			
			Reader reader = Files.newBufferedReader(Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/settings.json"), Charset.forName("UTF-8"));
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
		PathUtils.checkFolder(new File(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER)));
		Writer writer = Files.newBufferedWriter(Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/settings.json"), Charset.forName("UTF-8"));
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
	
	public static Object loadObjectFromJSON(String fileName, Object objectype)
	{		
		try
		{
			Gson gson = new Gson();			
			Reader reader = Files.newBufferedReader(Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/" + fileName + ".json"), Charset.forName("UTF-8"));
			Object preferences = gson.fromJson(reader, objectype.getClass());
			reader.close();
			return preferences;
		}
		catch(IOException e)
		{
			return null;
		}
	}
	
	public static void saveObjectToJSON(String fileName, Object objectToSave) throws IOException
	{		
		Gson gson = new Gson();
		String jsonString = gson.toJson(objectToSave);
		PathUtils.checkFolder(new File(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER)));
		Writer writer = Files.newBufferedWriter(Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/" + fileName + ".json"), Charset.forName("UTF-8"));
		writer.write(jsonString);
		writer.close();
	}
}