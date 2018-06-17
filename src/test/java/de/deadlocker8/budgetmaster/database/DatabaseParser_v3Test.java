package de.deadlocker8.budgetmaster.database;

import de.deadlocker8.budgetmaster.entities.*;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndAfterXTimes;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierDays;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import tools.Localization;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseParser_v3Test
{
	@Before
	public void before()
	{
		Localization.init("languages/");
		Localization.loadLanguage(Locale.ENGLISH);
	}

	@Test
	public void test_Categories()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v3Test.json").toURI())));
			DatabaseParser_v3 importer = new DatabaseParser_v3(json);
			Database database = importer.parseDatabaseFromJSON();

			assertEquals(3, database.getCategories().size());
			Category categoryNone = new Category("Keine Kategorie", "#FFFFFF", CategoryType.NONE);
			categoryNone.setID(1);
			assertTrue(database.getCategories().contains(categoryNone));

			Category categoryRest = new Category("Übertrag", "#FFFF00", CategoryType.REST);
			categoryRest.setID(2);
			assertTrue(database.getCategories().contains(categoryRest));

			Category category3 = new Category("0815", "#ffcc00", CategoryType.CUSTOM);
			category3.setID(3);
			assertTrue(database.getCategories().contains(category3));
		}
		catch(IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void test_Accounts()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v3Test.json").toURI())));
			DatabaseParser_v3 importer = new DatabaseParser_v3(json);
			Database database = importer.parseDatabaseFromJSON();

			assertEquals(2, database.getAccounts().size());
			assertEquals("Default", database.getAccounts().get(0).getName());
			assertEquals("Second Account", database.getAccounts().get(1).getName());
		}
		catch(IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void test_Payments()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v3Test.json").toURI())));
			DatabaseParser_v3 importer = new DatabaseParser_v3(json);
			Database database = importer.parseDatabaseFromJSON();

			Account account1 = new Account("Default");
			account1.setID(1);

			Account account2 = new Account("Second Account");
			account2.setID(2);

			Category categoryNone = new Category("Keine Kategorie", "#FFFFFF", CategoryType.NONE);
			categoryNone.setID(1);

			Category category3 = new Category("0815", "#ffcc00", CategoryType.CUSTOM);
			category3.setID(3);

			assertEquals(5, database.getPayments().size());

			Payment normalPayment_1 = new Payment();
			normalPayment_1.setAmount(35000);
			normalPayment_1.setDate(DateTime.parse("2018-03-13", DateTimeFormat.forPattern("yyyy-MM-dd")));
			normalPayment_1.setCategory(categoryNone);
			normalPayment_1.setName("Income");
			normalPayment_1.setDescription("Lorem Ipsum");
			normalPayment_1.setTags(new ArrayList<>());
			normalPayment_1.setAccount(account1);
			assertTrue(database.getPayments().contains(normalPayment_1));

			Payment normalPayment_2 = new Payment();
			normalPayment_2.setAmount(-2000);
			normalPayment_2.setDate(DateTime.parse("2018-06-15", DateTimeFormat.forPattern("yyyy-MM-dd")));
			normalPayment_2.setName("Simple");
			normalPayment_2.setDescription("");
			normalPayment_2.setAccount(account2);
			normalPayment_2.setCategory(category3);

			List<Tag> tags = new ArrayList<>();
			Tag tag = new Tag("0815");
			tag.setID(1);
			tags.add(tag);
			normalPayment_2.setTags(tags);
			assertTrue(database.getPayments().contains(normalPayment_2));

			Payment repeatingPayment_1 = new Payment();
			repeatingPayment_1.setAmount(-12300);
			DateTime repeatingPaymentDate_1 = DateTime.parse("2018-03-13", DateTimeFormat.forPattern("yyyy-MM-dd"));
			repeatingPayment_1.setDate(repeatingPaymentDate_1);
			repeatingPayment_1.setCategory(categoryNone);
			repeatingPayment_1.setName("Test");
			repeatingPayment_1.setDescription("");
			repeatingPayment_1.setAccount(account1);
			RepeatingOption repeatingOption_1 = new RepeatingOption();
			repeatingOption_1.setModifier(new RepeatingModifierDays(10));
			repeatingOption_1.setStartDate(repeatingPaymentDate_1);
			repeatingOption_1.setEndOption(new RepeatingEndAfterXTimes(2));
			repeatingPayment_1.setRepeatingOption(repeatingOption_1);
			repeatingPayment_1.setTags(new ArrayList<>());
			assertTrue(database.getPayments().contains(repeatingPayment_1));

			Payment repeatingPayment_2 = new Payment();
			repeatingPayment_2.setAmount(-12300);
			DateTime repeatingPaymentDate_2 = DateTime.parse("2018-03-23", DateTimeFormat.forPattern("yyyy-MM-dd"));
			repeatingPayment_2.setDate(repeatingPaymentDate_2);
			repeatingPayment_2.setCategory(categoryNone);
			repeatingPayment_2.setName("Test");
			repeatingPayment_2.setDescription("");
			repeatingPayment_2.setAccount(account1);
			RepeatingOption repeatingOption_2 = new RepeatingOption();
			repeatingOption_2.setModifier(new RepeatingModifierDays(10));
			repeatingOption_2.setStartDate(repeatingPaymentDate_2);
			repeatingOption_2.setEndOption(new RepeatingEndAfterXTimes(2));
			repeatingPayment_2.setRepeatingOption(repeatingOption_2);
			repeatingPayment_2.setTags(new ArrayList<>());
			assertTrue(database.getPayments().contains(repeatingPayment_2));
		}
		catch(IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}
	}
}