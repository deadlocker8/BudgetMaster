package de.deadlocker8.budgetmaster.tests.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmaster.logic.database.Database;
import de.deadlocker8.budgetmaster.logic.payment.NormalPayment;
import de.deadlocker8.budgetmaster.logic.payment.RepeatingPayment;
import de.deadlocker8.budgetmaster.logic.utils.FileHelper;
import de.deadlocker8.budgetmasterserver.logic.Settings;
import de.deadlocker8.budgetmasterserver.logic.Utils;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseExporter;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseImporter;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseTagHandler;
import tools.Localization;

public class DatabaseImportExportTest
{			
	private static Settings settings;
	private static DatabaseHandler databaseHandler;
	private static DatabaseTagHandler tagHandler;
	
	@BeforeClass
	public static void init()
	{
		try
		{
			//init
			settings = Utils.loadSettings();			
			DatabaseHandler handler = new DatabaseHandler(settings);
			handler.deleteDatabase();
			handler = new DatabaseHandler(settings);			
			databaseHandler = handler;
			tagHandler = new DatabaseTagHandler(settings);
			
			Localization.init("de/deadlocker8/budgetmaster/");
			Localization.loadLanguage(Locale.ENGLISH);
		}
		catch(IOException | URISyntaxException e)
		{
			fail(e.getMessage());
		}		
	}	
	
	@Test
	public void testImport()
	{	
		try
		{
			File file = Paths.get("src/test/resources/de/deadlocker8/budgetmaster/import.json").toFile();
			Database database = FileHelper.loadDatabaseJSON(file);			
			
			DatabaseImporter importer = new DatabaseImporter(databaseHandler, tagHandler);
			importer.importDatabase(database);
			
			//test category
			Category expectedCategory = new Category(3, "123 Tü+?est Category", "#FF9500");			
			ArrayList<Category> categories = databaseHandler.getCategories();	
			
			Category category = databaseHandler.getCategory(categories.get(categories.size()-1).getID());
			assertEquals(expectedCategory.getName(), category.getName());
			assertEquals(expectedCategory.getColor(), category.getColor());
			
			//test normal payment
			NormalPayment expectedPayment = new NormalPayment(1, 23, "2017-06-02", 3, "Test Normal", "Lorem Ipsum");			
			NormalPayment payment = databaseHandler.getPayment(1);			
			assertEquals(expectedPayment.getAmount(), payment.getAmount());		
			assertEquals(expectedPayment.getDate(), payment.getDate());
			assertEquals(expectedPayment.getCategoryID(), payment.getCategoryID());
			assertEquals(expectedPayment.getName(), payment.getName());
			assertEquals(expectedPayment.getDescription(), payment.getDescription());
			
			//test repeating payment
			RepeatingPayment expectedRepeatingPayment = new RepeatingPayment(1, -10012, "2017-06-01", 1, "Test Repeating", "Lorem Ipsum", 7, "2017-06-30", 0);			
			RepeatingPayment repeatingPayment = databaseHandler.getRepeatingPayment(1);
			assertEquals(expectedRepeatingPayment.getAmount(), repeatingPayment.getAmount());
			assertEquals(expectedRepeatingPayment.getDate(), repeatingPayment.getDate());
			assertEquals(expectedRepeatingPayment.getCategoryID(), repeatingPayment.getCategoryID());
			assertEquals(expectedRepeatingPayment.getName(), repeatingPayment.getName());
			assertEquals(expectedRepeatingPayment.getDescription(), repeatingPayment.getDescription());
			assertEquals(expectedRepeatingPayment.getRepeatInterval(), repeatingPayment.getRepeatInterval());
			assertEquals(expectedRepeatingPayment.getRepeatEndDate(), repeatingPayment.getRepeatEndDate());
			assertEquals(expectedRepeatingPayment.getRepeatMonthDay(), repeatingPayment.getRepeatMonthDay());	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}		
	}
	
	@Test
	public void testExport()
	{	
		try
		{
			databaseHandler.deleteDatabase();
			databaseHandler = new DatabaseHandler(settings);
			
			File file = Paths.get("src/test/resources/de/deadlocker8/budgetmaster/import.json").toFile();
			Database database = FileHelper.loadDatabaseJSON(file);			
			
			DatabaseImporter importer = new DatabaseImporter(databaseHandler, tagHandler);
			importer.importDatabase(database);			
			
			file = Paths.get("src/test/resources/de/deadlocker8/budgetmaster/export.json").toFile();
			DatabaseExporter exporter = new DatabaseExporter(settings);	
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String databaseJSON = gson.toJson(exporter.exportDatabase()).replaceAll("\n", "");
			FileHelper.saveDatabaseJSON(file, databaseJSON);
			FileHelper.saveDatabaseJSON(new File("C:/Users/ROGO2/Desktop/123.json"), databaseJSON);
			
			String expectedJSON = new String(Files.readAllBytes(Paths.get("src/test/resources/de/deadlocker8/budgetmaster/import.json")));
			String exportedJSON = new String(Files.readAllBytes(Paths.get("src/test/resources/de/deadlocker8/budgetmaster/export.json")));		
			
			assertEquals(expectedJSON, exportedJSON);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}		
	}
}