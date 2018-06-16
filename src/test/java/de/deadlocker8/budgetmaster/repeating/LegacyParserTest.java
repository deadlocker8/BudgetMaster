package de.deadlocker8.budgetmaster.repeating;

import de.deadlocker8.budgetmaster.database.Database;
import de.deadlocker8.budgetmaster.database.legacy.LegacyParser;
import de.deadlocker8.budgetmaster.entities.*;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndDate;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndNever;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierDays;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierMonths;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LegacyParserTest
{
	@Test
	public void test_Categories()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("LegacyParserTest.json").toURI())));

			LegacyParser importer = new LegacyParser(json);
			Database database = importer.parseDatabaseFromJSON();

			assertEquals(2, database.getCategories().size());
			Category category3 = new Category("Salary", "#4CD964", CategoryType.CUSTOM);
			category3.setID(3);
			assertTrue(database.getCategories().contains(category3));

			Category category4 = new Category("Stuff", "#9B59B6", CategoryType.CUSTOM);
			category4.setID(4);
			assertTrue(database.getCategories().contains(category4));
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
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("LegacyParserTest.json").toURI())));
			LegacyParser importer = new LegacyParser(json);
			Database database = importer.parseDatabaseFromJSON();

			assertEquals(1, database.getAccounts().size());
			assertEquals("LEGACY_IMPORT", database.getAccounts().get(0).getName());
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
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("LegacyParserTest.json").toURI())));
			Account account = new Account("LEGACY_IMPORT");

			List<Tag> tags = new ArrayList<>();
			tags.add(new Tag("0815"));

			Category category3 = new Category("Salary", "#4CD964", CategoryType.CUSTOM);
			category3.setID(3);

			Category category4 = new Category("Stuff", "#9B59B6", CategoryType.CUSTOM);
			category4.setID(4);

			LegacyParser importer = new LegacyParser(json);
			Database database = importer.parseDatabaseFromJSON();

			assertEquals(4, database.getPayments().size());

			Payment normalPayment_1 = new Payment();
			normalPayment_1.setAmount(-2323);
			normalPayment_1.setDate(DateTime.parse("2018-01-21", DateTimeFormat.forPattern("yyyy-MM-dd")));
			normalPayment_1.setCategory(category4);
			normalPayment_1.setName("Fuel");
			normalPayment_1.setDescription("Lorem Ipsum");
			normalPayment_1.setTags(tags);
			normalPayment_1.setAccount(account);
			assertTrue(database.getPayments().contains(normalPayment_1));

			Payment normalPayment_2 = new Payment();
			normalPayment_2.setAmount(100);
			normalPayment_2.setDate(DateTime.parse("2018-01-01", DateTimeFormat.forPattern("yyyy-MM-dd")));
			normalPayment_2.setName("no category");
			normalPayment_2.setDescription("");
			normalPayment_2.setAccount(account);
			normalPayment_2.setTags(new ArrayList<>());
			assertTrue(database.getPayments().contains(normalPayment_2));

			Payment repeatingPayment_1 = new Payment();
			repeatingPayment_1.setAmount(500000);
			DateTime repeatingPaymentDate_1 = DateTime.parse("2017-03-01", DateTimeFormat.forPattern("yyyy-MM-dd"));
			repeatingPayment_1.setDate(repeatingPaymentDate_1);
			repeatingPayment_1.setCategory(category3);
			repeatingPayment_1.setName("Salary");
			repeatingPayment_1.setDescription("Monthly cash");
			repeatingPayment_1.setAccount(account);
			RepeatingOption repeatingOption_1 = new RepeatingOption();
			repeatingOption_1.setModifier(new RepeatingModifierMonths(1));
			repeatingOption_1.setStartDate(repeatingPaymentDate_1);
			repeatingOption_1.setEndOption(new RepeatingEndNever());
			repeatingPayment_1.setRepeatingOption(repeatingOption_1);
			repeatingPayment_1.setTags(tags);
			assertTrue(database.getPayments().contains(repeatingPayment_1));

			Payment repeatingPayment_2 = new Payment();
			repeatingPayment_2.setAmount(-2500);
			DateTime repeatingPaymentDate_2 = DateTime.parse("2017-03-15", DateTimeFormat.forPattern("yyyy-MM-dd"));
			repeatingPayment_2.setDate(repeatingPaymentDate_2);
			repeatingPayment_2.setCategory(category4);
			repeatingPayment_2.setName("Rent");
			repeatingPayment_2.setDescription("Repeating every 7 days");
			repeatingPayment_2.setAccount(account);
			RepeatingOption repeatingOption_2= new RepeatingOption();
			repeatingOption_2.setModifier(new RepeatingModifierDays(7));
			repeatingOption_2.setStartDate(repeatingPaymentDate_2);
			repeatingOption_2.setEndOption(new RepeatingEndDate(DateTime.parse("2017-06-15", DateTimeFormat.forPattern("yyyy-MM-dd"))));
			repeatingPayment_2.setDate(repeatingPaymentDate_2);
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