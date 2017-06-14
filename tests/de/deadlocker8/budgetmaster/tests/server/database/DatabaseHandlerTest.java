package de.deadlocker8.budgetmaster.tests.server.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;

import de.deadlocker8.budgetmasterserver.logic.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.logic.Settings;
import de.deadlocker8.budgetmasterserver.logic.Utils;
import javafx.scene.paint.Color;

public class DatabaseHandlerTest
{			
	private static DatabaseHandler databaseHandler;
	
	@BeforeClass
	public static void init()
	{
		try
		{
			//init
			Settings settings = Utils.loadSettings();
			DatabaseHandler handler = new DatabaseHandler(settings);
			handler.deleteDatabase();
			handler = new DatabaseHandler(settings);			
			databaseHandler = handler;
		}
		catch(IOException | URISyntaxException e)
		{
			fail(e.getMessage());
		}		
	}	
	
	@Test
	public void testLastInsertID()
	{		
		databaseHandler.addCategory("123 Tü+?est Category", Color.ALICEBLUE);			
		//3 because "NONE" and "Übertrag" has already been inserted at database creation
		assertEquals(3, databaseHandler.getLastInsertID());		
	}
	
	@Test
	public void test()
	{		
		assert(true);
	}
}