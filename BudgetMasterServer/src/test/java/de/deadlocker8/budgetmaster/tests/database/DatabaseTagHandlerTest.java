package de.deadlocker8.budgetmaster.tests.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmasterserver.logic.Settings;
import de.deadlocker8.budgetmasterserver.logic.Utils;
import de.deadlocker8.budgetmasterserver.logic.database.creator.DatabaseCreator;
import de.deadlocker8.budgetmasterserver.logic.database.handler.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.logic.database.taghandler.DatabaseTagHandler;
import tools.Localization;

public class DatabaseTagHandlerTest
{			
	private static DatabaseTagHandler tagHandler;
	
	@BeforeClass
	public static void init()
	{
		try
		{
			//init
			Settings settings = Utils.loadSettings();
			System.out.println(settings);
			DatabaseHandler handler = Utils.getDatabaseHandler(settings);
			handler.deleteDatabase();
			handler.closeConnection();
			Connection connection = Utils.getDatabaseConnection(settings);
			DatabaseCreator creator = Utils.getDatabaseCreator(connection, settings);
			creator.createTables();
			connection.close();
			tagHandler = Utils.getDatabaseTagHandler(settings);
			
			Localization.init("de/deadlocker8/budgetmaster/");
			Localization.loadLanguage(Locale.ENGLISH);
		}
		catch(IOException | URISyntaxException | SQLException e)
		{
			fail(e.getMessage());
		}		
	}
	
	@Before
	public void before()
	{
		tagHandler.connect();
	}
	
	@After
	public void after()
	{
		tagHandler.closeConnection();
	}
	
	@Test
	public void testTag()
	{
		//add
		Tag expected = new Tag(1, "sd836f4ds86f4sd86");
		tagHandler.addTag(expected.getName());
		ArrayList<Tag> tags = tagHandler.getAllTags();
		assertEquals(1, tags.size());
		
		//get
		Tag tag = tagHandler.getTagByID(1);
		assertEquals(expected.getName(), tag.getName());
		
		tag = tagHandler.getTagByName(expected.getName());
		assertEquals(1, tag.getID());
	}

	@Test
	public void testDeleteCategory()
	{
		//add
		Tag expected = new Tag(1, "115");
		tagHandler.addTag(expected.getName());
		
		int id = tagHandler.getLastInsertID();
		
		tagHandler.deleteTag(id);
		Tag tag = tagHandler.getTagByID(id);
		
		assertNull(tag);
	}
	
	@Test
	public void testMatchForNormalPayment()
	{		
		//add
		Tag expected = new Tag(1, "wqeolugjf");
		tagHandler.addTag(expected.getName());
		int id = tagHandler.getLastInsertID();
		
		tagHandler.addTagMatchForPayment(id, 1);
		ArrayList<Integer> tags = tagHandler.getAllTagsForPayment(1);
		assertEquals(1, tags.size());
		
		assertTrue(tagHandler.isMatchExistingForPaymentID(id, 1));
		assertFalse(tagHandler.isMatchExistingForPaymentID(id, -3));
		
		assertTrue(tagHandler.isTagUsedInMatches(id));
		
		tagHandler.deleteTagMatchForPayment(id, 1);
		assertFalse(tagHandler.isMatchExistingForPaymentID(id, 1));
	}
	
	@Test
	public void testMatchRepeatingPayment()
	{		
		//add
		Tag expected = new Tag(1, "as5d4s5a4d");
		tagHandler.addTag(expected.getName());
		int id = tagHandler.getLastInsertID();
		
		tagHandler.addTagMatchForRepeatingPayment(id, 1);
		ArrayList<Integer> tags = tagHandler.getAllTagsForRepeatingPayment(1);
		assertEquals(1, tags.size());
		
		assertTrue(tagHandler.isMatchExistingForRepeatingPaymentID(id, 1));
		assertFalse(tagHandler.isMatchExistingForRepeatingPaymentID(id, -3));
		
		assertTrue(tagHandler.isTagUsedInMatches(id));
		
		tagHandler.deleteTagMatchForRepeatingPayment(id, 1);
		assertFalse(tagHandler.isMatchExistingForRepeatingPaymentID(id, 1));
	}	
}