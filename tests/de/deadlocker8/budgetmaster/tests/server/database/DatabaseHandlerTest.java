package de.deadlocker8.budgetmaster.tests.server.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;

import de.deadlocker8.budgetmaster.logic.NormalPayment;
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
	public void testNormalPayment()
	{		
		NormalPayment expectedPayment = new NormalPayment(1, 1000, "2017-03-01", 2, "Buchung", "Lorem Ipsum");	
		
		databaseHandler.addNormalPayment(expectedPayment.getAmount(),
										 expectedPayment.getDate(),
										 expectedPayment.getCategoryID(),
										 expectedPayment.getName(),
										 expectedPayment.getDescription());
		NormalPayment payment = databaseHandler.getPayment(1);
		
		assertEquals(expectedPayment.getAmount(), payment.getAmount());		
		assertEquals(expectedPayment.getDate(), payment.getDate());
		assertEquals(expectedPayment.getCategoryID(), payment.getCategoryID());
		assertEquals(expectedPayment.getName(), payment.getName());
		assertEquals(expectedPayment.getDescription(), payment.getDescription());
	}
	
	@Test
	public void testDeleteNormalPayment()
	{
		databaseHandler.deletePayment(1);
		NormalPayment payment = databaseHandler.getPayment(1);
		
		assertNull(payment);
	}
}