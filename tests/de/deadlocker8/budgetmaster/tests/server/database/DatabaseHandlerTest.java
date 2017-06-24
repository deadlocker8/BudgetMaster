package de.deadlocker8.budgetmaster.tests.server.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.NormalPayment;
import de.deadlocker8.budgetmaster.logic.RepeatingPayment;
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
			System.out.println(settings);
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
		Category expected = new Category("123 Tü+?est Category", Color.ALICEBLUE);
		databaseHandler.addCategory(expected.getName(), expected.getColor());
		//3 because "NONE" and "Übertrag" has already been inserted at database creation	
		assertEquals(3, databaseHandler.getLastInsertID());		
	}
	
	@Test
	public void testCategory()
	{
		Category expected = new Category("123 Tü+?est Category", Color.ALICEBLUE);
		databaseHandler.addCategory(expected.getName(), expected.getColor());
		ArrayList<Category> categories = databaseHandler.getCategories();	
		
		Category category = databaseHandler.getCategory(categories.get(categories.size()-1).getID());
		assertEquals(expected.getName(), category.getName());
		assertEquals(expected.getColor(), category.getColor());
	}

	@Test
	public void testDeleteCategory()
	{
		databaseHandler.deleteCategory(1);
		Category category = databaseHandler.getCategory(1);
		
		assertNull(category);
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
	
	@Test
	public void testRepeatingPayment()
	{	
		RepeatingPayment expectedPayment = new RepeatingPayment(1, 1000, "2017-03-01", 2, "Buchung", "Lorem Ipsum", 0, null, 15);
		
		databaseHandler.addRepeatingPayment(expectedPayment.getAmount(),
											expectedPayment.getDate(),
											expectedPayment.getCategoryID(),
											expectedPayment.getName(),
											expectedPayment.getDescription(),
											expectedPayment.getRepeatInterval(),
											expectedPayment.getRepeatEndDate(),
											expectedPayment.getRepeatMonthDay());		
		
		RepeatingPayment payment = databaseHandler.getRepeatingPayment(1);
		
		assertEquals(expectedPayment.getAmount(), payment.getAmount());		
		assertEquals(expectedPayment.getDate(), payment.getDate());
		assertEquals(expectedPayment.getCategoryID(), payment.getCategoryID());
		assertEquals(expectedPayment.getName(), payment.getName());
		assertEquals(expectedPayment.getDescription(), payment.getDescription());
		assertEquals(expectedPayment.getRepeatInterval(), payment.getRepeatInterval());
		assertEquals(expectedPayment.getRepeatEndDate(), payment.getRepeatEndDate());
		assertEquals(expectedPayment.getRepeatMonthDay(), payment.getRepeatMonthDay());
	}
	
	@Test
	public void testDeleteRepeatingPayment()
	{
		databaseHandler.deleteRepeatingPayment(1);
		RepeatingPayment payment = databaseHandler.getRepeatingPayment(1);
		
		assertNull(payment);
	}
}