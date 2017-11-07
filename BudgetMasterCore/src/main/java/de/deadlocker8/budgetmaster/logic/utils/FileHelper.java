package de.deadlocker8.budgetmaster.logic.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.Settings;
import de.deadlocker8.budgetmaster.logic.database.Database;
import de.deadlocker8.budgetmaster.logic.database.OldDatabase;
import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmaster.logic.tag.TagMatch;
import tools.Localization;
import tools.PathUtils;

@SuppressWarnings("deprecation")
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
		catch(Exception e)
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
		BufferedReader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()), Charset.forName("UTF-8"));

		StringBuilder sb = new StringBuilder();
		String line;
		while((line = reader.readLine()) != null)
		{
			sb.append(line);
		}

		reader.close();
		String jsonString = sb.toString();
		if(jsonString.contains("BUDGETMASTER_DATABASE"))
		{
			if(jsonString.contains("VERSION"))
			{
				int start = jsonString.indexOf("\"VERSION\": ");
				start = start + 11;
				int version = Integer.parseInt(jsonString.substring(start, start + 1));
				Database database;
				
				switch(version)
				{
					case 2: database = gson.fromJson(jsonString, Database.class);							
							break;
					default: return loadOldDatabase(gson, jsonString);
				}
				return database;
			}
		}

		return loadOldDatabase(gson, jsonString);
	}
	
	private static Database loadOldDatabase(Gson gson, String jsonString) throws IOException
	{
		// database version = 1 (prior to BudgetMaster 1.6.0)
		OldDatabase olDatabase = gson.fromJson(jsonString, OldDatabase.class);
		return new Database(olDatabase.getCategories(), 
							olDatabase.getNormalPayments(), 
							olDatabase.getRepeatingPayments(), 
							new ArrayList<Tag>(), 
							new ArrayList<TagMatch>());
	}

	public static void saveDatabaseJSON(File file, String databaseJSON) throws IOException
	{
		Writer writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()), Charset.forName("UTF-8"));
		writer.write(databaseJSON);
		writer.close();
	}	
}