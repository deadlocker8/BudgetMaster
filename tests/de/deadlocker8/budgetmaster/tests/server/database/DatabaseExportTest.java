package de.deadlocker8.budgetmaster.tests.server.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import de.deadlocker8.budgetmasterserver.logic.DatabaseExporter;
import de.deadlocker8.budgetmasterserver.logic.Settings;
import de.deadlocker8.budgetmasterserver.logic.Utils;

public class DatabaseExportTest
{			
	private static Settings settings; 
	
	@BeforeClass
	public static void init()
	{
		try
		{
			//init
			settings = Utils.loadSettings();	
		}
		catch(IOException | URISyntaxException e)
		{
			fail(e.getMessage());
		}		
	}	
	
	@Test
	public void testExport()
	{	
		try
		{
			File file = Paths.get("tests/de/deadlocker8/budgetmaster/tests/resources/export.json").toFile();
			DatabaseExporter exporter = new DatabaseExporter(settings);	
			Gson gson = new Gson();
			String databaseJSON = gson.toJson(exporter.exportDatabase());
			de.deadlocker8.budgetmaster.logic.Utils.saveDatabaseJSON(file, databaseJSON);		
			
			String expectedJSON = new String(Files.readAllBytes(Paths.get("tests/de/deadlocker8/budgetmaster/tests/resources/import.json")));
			String exportedJSON = new String(Files.readAllBytes(Paths.get("tests/de/deadlocker8/budgetmaster/tests/resources/export.json")));
			assertEquals(expectedJSON, exportedJSON);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}		
	}
}