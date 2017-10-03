package de.deadlocker8.budgetmaster.tests.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;

import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmaster.logic.payment.LatestRepeatingPayment;
import de.deadlocker8.budgetmaster.logic.payment.NormalPayment;
import de.deadlocker8.budgetmaster.logic.payment.RepeatingPayment;
import de.deadlocker8.budgetmasterserver.logic.Settings;
import de.deadlocker8.budgetmasterserver.logic.Utils;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseHandler;
import tools.Localization;

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
			
			Localization.init("de/deadlocker8/budgetmaster/");
			Localization.loadLanguage(Locale.ENGLISH);
		}
		catch(IOException | URISyntaxException e)
		{
			fail(e.getMessage());
		}		
	}	
	
	@Test
	public void testLastInsertID()
	{			
		Category expected = new Category("123 Tü+?est Category", "#FF0000");
		databaseHandler.addCategory(expected.getName(), expected.getColor());
		//3 because "NONE" and "Übertrag" has already been inserted at database creation	
		assertEquals(3, databaseHandler.getLastInsertID());		
	}
	
	@Test
	public void testCategory()
	{
		//add
		Category expected = new Category("123 Tü+?est Category", "#FF0000");
		databaseHandler.addCategory(expected.getName(), expected.getColor());
		ArrayList<Category> categories = databaseHandler.getCategories();	
		
		//get
		Category category = databaseHandler.getCategory(categories.get(categories.size()-1).getID());
		assertEquals(expected.getName(), category.getName());
		assertEquals(expected.getColor(), category.getColor());
		
		//update
		Category expectedUpdated = new Category(category.getID(), "456", "#00FF00");
		databaseHandler.updateCategory(expectedUpdated.getID(), expectedUpdated.getName(), expectedUpdated.getColor());
		category = databaseHandler.getCategory(expectedUpdated.getID());
		assertEquals(expectedUpdated.getName(), category.getName());
		assertEquals(expectedUpdated.getColor(), category.getColor());
		
		//misc
		category = databaseHandler.getCategory("NONE", "#FFFFFF");
		assertEquals(1, category.getID());
		
		assertTrue(databaseHandler.categoryExists(1));
	}

	@Test
	public void testDeleteCategory()
	{
		//add
		Category expected = new Category("123 Tü+?est Category", "#FF0000");
		databaseHandler.addCategory(expected.getName(), expected.getColor());		
		
		int id = databaseHandler.getLastInsertID();
		
		databaseHandler.deleteCategory(id);
		Category category = databaseHandler.getCategory(id);
		
		assertNull(category);
	}
	
	@Test
	public void testNormalPayment()
	{		
		//add
		NormalPayment expectedPayment = new NormalPayment(1, 1000, "2017-03-01", 2, "Buchung", "Lorem Ipsum");
		
		databaseHandler.addNormalPayment(expectedPayment.getAmount(),
										 expectedPayment.getDate(),
										 expectedPayment.getCategoryID(),
										 expectedPayment.getName(),
										 expectedPayment.getDescription());
		
		int id = databaseHandler.getLastInsertID();
		
		//get
		NormalPayment payment = databaseHandler.getPayment(id);
		
		assertEquals(expectedPayment.getAmount(), payment.getAmount());		
		assertEquals(expectedPayment.getDate(), payment.getDate());
		assertEquals(expectedPayment.getCategoryID(), payment.getCategoryID());
		assertEquals(expectedPayment.getName(), payment.getName());
		assertEquals(expectedPayment.getDescription(), payment.getDescription());
		
		//update
		NormalPayment expectedUpdated = new NormalPayment(id, 2000, "2017-03-02", 1, "Buchung 2", "Lorem Ipsum");
		databaseHandler.updateNormalPayment(expectedUpdated.getID(),
											expectedUpdated.getAmount(),
											expectedUpdated.getDate(),
											expectedUpdated.getCategoryID(),
											expectedUpdated.getName(),
											expectedUpdated.getDescription());
		
		payment = databaseHandler.getPayment(id);
		
		assertEquals(expectedUpdated.getAmount(), payment.getAmount());		
		assertEquals(expectedUpdated.getDate(), payment.getDate());
		assertEquals(expectedUpdated.getCategoryID(), payment.getCategoryID());
		assertEquals(expectedUpdated.getName(), payment.getName());
		assertEquals(expectedUpdated.getDescription(), payment.getDescription());		
		
		//misc
		assertEquals(1, databaseHandler.getPayments(2017, 03).size());
		assertEquals(0, databaseHandler.getPayments(2015, 03).size());
		
		assertEquals(1, databaseHandler.getPaymentsBetween("2016-01-01", "2018-01-01").size());
		assertEquals(0, databaseHandler.getPaymentsBetween("2018-01-01", "2019-01-01").size());
	}
	
	@Test
	public void testDeleteNormalPayment()
	{
		//add
		NormalPayment expectedPayment = new NormalPayment(1, 1000, "2017-03-01", 2, "Buchung", "Lorem Ipsum");
		
		databaseHandler.addNormalPayment(expectedPayment.getAmount(),
										 expectedPayment.getDate(),
										 expectedPayment.getCategoryID(),
										 expectedPayment.getName(),
										 expectedPayment.getDescription());
		
		int id = databaseHandler.getLastInsertID();
		
		databaseHandler.deletePayment(id);
		NormalPayment payment = databaseHandler.getPayment(id);
		
		assertNull(payment);
	}
	
	@Test
	public void testRepeatingPayment()
	{	
		//add
		RepeatingPayment expectedPayment = new RepeatingPayment(1, 1000, "2017-03-01", 2, "Buchung", "Lorem Ipsum", 0, null, 15);
		
		databaseHandler.addRepeatingPayment(expectedPayment.getAmount(),
											expectedPayment.getDate(),
											expectedPayment.getCategoryID(),
											expectedPayment.getName(),
											expectedPayment.getDescription(),
											expectedPayment.getRepeatInterval(),
											expectedPayment.getRepeatEndDate(),
											expectedPayment.getRepeatMonthDay());
		//get
		RepeatingPayment payment = databaseHandler.getRepeatingPayment(databaseHandler.getLastInsertID());
		
		assertEquals(expectedPayment.getAmount(), payment.getAmount());		
		assertEquals(expectedPayment.getDate(), payment.getDate());
		assertEquals(expectedPayment.getCategoryID(), payment.getCategoryID());
		assertEquals(expectedPayment.getName(), payment.getName());
		assertEquals(expectedPayment.getDescription(), payment.getDescription());
		assertEquals(expectedPayment.getRepeatInterval(), payment.getRepeatInterval());
		assertEquals(expectedPayment.getRepeatEndDate(), payment.getRepeatEndDate());
		assertEquals(expectedPayment.getRepeatMonthDay(), payment.getRepeatMonthDay());
		
		//RepeatingPaymentEntry
		databaseHandler.addRepeatingPaymentEntry(expectedPayment.getID(), "2017-03-15");
		ArrayList<LatestRepeatingPayment> latestPayments = databaseHandler.getLatestRepeatingPaymentEntries();
		assertEquals(1, latestPayments.size());
		assertEquals(expectedPayment.getID(), latestPayments.get(0).getRepeatingPaymentID());
		assertEquals("2017-03-15", latestPayments.get(0).getLastDate());		
		
		//misc
		assertEquals(1, databaseHandler.getRepeatingPayments(2017, 03).size());
		assertEquals(0, databaseHandler.getRepeatingPayments(2015, 03).size());
		
		assertEquals(1, databaseHandler.getRepeatingPaymentsBetween("2016-01-01", "2018-01-01").size());
		assertEquals(0, databaseHandler.getRepeatingPaymentsBetween("2018-01-01", "2019-01-01").size());
		
		assertEquals(1, databaseHandler.getAllRepeatingPayments().size());		
	}
	
	@Test
	public void testDeleteRepeatingPayment()
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
		
		int id = databaseHandler.getLastInsertID();
		
		databaseHandler.deleteRepeatingPayment(id);
		RepeatingPayment payment = databaseHandler.getRepeatingPayment(id);
		
		assertNull(payment);
	}
	
	@Test
	public void testRest()
	{
		//add payments for previous months
		NormalPayment expectedPayment = new NormalPayment(1, 1000, "2017-03-01", 2, "Buchung", "Lorem Ipsum");		
		databaseHandler.addNormalPayment(expectedPayment.getAmount(),
										 expectedPayment.getDate(),
										 expectedPayment.getCategoryID(),
										 expectedPayment.getName(),
										 expectedPayment.getDescription());		
		int idPayment1 = databaseHandler.getLastInsertID();
		
		expectedPayment = new NormalPayment(2, -800, "2017-02-01", 2, "Buchung", "Lorem Ipsum");		
		databaseHandler.addNormalPayment(expectedPayment.getAmount(),
										 expectedPayment.getDate(),
										 expectedPayment.getCategoryID(),
										 expectedPayment.getName(),
										 expectedPayment.getDescription());
		int idPayment2 = databaseHandler.getLastInsertID();
		
		assertEquals(1000, databaseHandler.getRest(2017, 3));		
		assertEquals(200, databaseHandler.getRestForAllPreviousMonths(2017, 4));
		
		databaseHandler.deletePayment(idPayment1);
		databaseHandler.deletePayment(idPayment2);
	}
}