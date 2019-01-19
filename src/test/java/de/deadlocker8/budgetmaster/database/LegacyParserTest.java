package de.deadlocker8.budgetmaster.database;

import de.deadlocker8.budgetmaster.database.legacy.LegacyParser;
import de.deadlocker8.budgetmaster.entities.*;
import de.deadlocker8.budgetmaster.entities.account.Account;
import de.deadlocker8.budgetmaster.entities.account.AccountType;
import de.deadlocker8.budgetmaster.entities.category.Category;
import de.deadlocker8.budgetmaster.entities.category.CategoryType;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
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
	private static final Category categoryNone = new Category("NONE", "#CCCCCC", CategoryType.NONE);

	@Test
	public void test_Categories()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("LegacyParserTest.json").toURI())));

			LegacyParser importer = new LegacyParser(json, categoryNone);
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
			LegacyParser importer = new LegacyParser(json, categoryNone);
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
			Account account = new Account("LEGACY_IMPORT", AccountType.CUSTOM);

			List<Tag> tags = new ArrayList<>();
			tags.add(new Tag("0815"));

			Category category3 = new Category("Salary", "#4CD964", CategoryType.CUSTOM);
			category3.setID(3);

			Category category4 = new Category("Stuff", "#9B59B6", CategoryType.CUSTOM);
			category4.setID(4);

			LegacyParser importer = new LegacyParser(json, categoryNone);
			Database database = importer.parseDatabaseFromJSON();

			assertEquals(4, database.getTransactions().size());

			Transaction normalTransaction_1 = new Transaction();
			normalTransaction_1.setAmount(-2323);
			normalTransaction_1.setDate(DateTime.parse("2018-01-21", DateTimeFormat.forPattern("yyyy-MM-dd")));
			normalTransaction_1.setCategory(category4);
			normalTransaction_1.setName("Fuel");
			normalTransaction_1.setDescription("Lorem Ipsum");
			normalTransaction_1.setTags(tags);
			normalTransaction_1.setAccount(account);
			assertTrue(database.getTransactions().contains(normalTransaction_1));

			Transaction normalTransaction_2 = new Transaction();
			normalTransaction_2.setAmount(100);
			normalTransaction_2.setDate(DateTime.parse("2018-01-01", DateTimeFormat.forPattern("yyyy-MM-dd")));
			normalTransaction_2.setName("no category");
			normalTransaction_2.setDescription("");
			normalTransaction_2.setAccount(account);
			normalTransaction_2.setCategory(categoryNone);
			normalTransaction_2.setTags(new ArrayList<>());
			assertTrue(database.getTransactions().contains(normalTransaction_2));

			Transaction repeatingTransaction_1 = new Transaction();
			repeatingTransaction_1.setAmount(500000);
			DateTime repeatingTransactionDate_1 = DateTime.parse("2017-03-01", DateTimeFormat.forPattern("yyyy-MM-dd"));
			repeatingTransaction_1.setDate(repeatingTransactionDate_1);
			repeatingTransaction_1.setCategory(category3);
			repeatingTransaction_1.setName("Salary");
			repeatingTransaction_1.setDescription("Monthly cash");
			repeatingTransaction_1.setAccount(account);
			RepeatingOption repeatingOption_1 = new RepeatingOption();
			repeatingOption_1.setModifier(new RepeatingModifierMonths(1));
			repeatingOption_1.setStartDate(repeatingTransactionDate_1);
			repeatingOption_1.setEndOption(new RepeatingEndNever());
			repeatingTransaction_1.setRepeatingOption(repeatingOption_1);
			repeatingTransaction_1.setTags(tags);
			assertTrue(database.getTransactions().contains(repeatingTransaction_1));

			Transaction repeatingTransaction_2 = new Transaction();
			repeatingTransaction_2.setAmount(-2500);
			DateTime repeatingTransactionDate_2 = DateTime.parse("2017-03-15", DateTimeFormat.forPattern("yyyy-MM-dd"));
			repeatingTransaction_2.setDate(repeatingTransactionDate_2);
			repeatingTransaction_2.setCategory(category4);
			repeatingTransaction_2.setName("Rent");
			repeatingTransaction_2.setDescription("Repeating every 7 days");
			repeatingTransaction_2.setAccount(account);
			RepeatingOption repeatingOption_2= new RepeatingOption();
			repeatingOption_2.setModifier(new RepeatingModifierDays(7));
			repeatingOption_2.setStartDate(repeatingTransactionDate_2);
			repeatingOption_2.setEndOption(new RepeatingEndDate(DateTime.parse("2017-06-15", DateTimeFormat.forPattern("yyyy-MM-dd"))));
			repeatingTransaction_2.setDate(repeatingTransactionDate_2);
			repeatingTransaction_2.setRepeatingOption(repeatingOption_2);
			repeatingTransaction_2.setTags(new ArrayList<>());
			assertTrue(database.getTransactions().contains(repeatingTransaction_2));
		}
		catch(IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}
	}
}