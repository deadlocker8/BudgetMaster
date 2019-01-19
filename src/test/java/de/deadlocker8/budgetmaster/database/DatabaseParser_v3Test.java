package de.deadlocker8.budgetmaster.database;

import de.deadlocker8.budgetmaster.entities.*;
import de.deadlocker8.budgetmaster.entities.account.Account;
import de.deadlocker8.budgetmaster.entities.account.AccountType;
import de.deadlocker8.budgetmaster.entities.category.Category;
import de.deadlocker8.budgetmaster.entities.category.CategoryType;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndAfterXTimes;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierDays;
import de.thecodelabs.utils.util.Localization;
import de.thecodelabs.utils.util.Localization.LocalizationDelegate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;

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
		Localization.setDelegate(new LocalizationDelegate()
		{
			@Override
			public Locale getLocale()
			{
				return Locale.ENGLISH;
			}

			@Override
			public String getBaseResource()
			{
				return "languages/";
			}
		});
		Localization.load();
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

			assertEquals(3, database.getAccounts().size());
			assertEquals("Placeholder", database.getAccounts().get(0).getName());
			assertEquals("Default", database.getAccounts().get(1).getName());
			assertEquals("Second Account", database.getAccounts().get(2).getName());
		}
		catch(IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void test_Transactions()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v3Test.json").toURI())));
			DatabaseParser_v3 importer = new DatabaseParser_v3(json);
			Database database = importer.parseDatabaseFromJSON();

			Account account1 = new Account("Default", AccountType.CUSTOM);
			account1.setID(2);

			Account account2 = new Account("Second Account", AccountType.CUSTOM);
			account2.setID(3);

			Category categoryNone = new Category("Keine Kategorie", "#FFFFFF", CategoryType.NONE);
			categoryNone.setID(1);

			Category category3 = new Category("0815", "#ffcc00", CategoryType.CUSTOM);
			category3.setID(3);

			assertEquals(5, database.getTransactions().size());

			Transaction normalTransaction_1 = new Transaction();
			normalTransaction_1.setAmount(35000);
			normalTransaction_1.setDate(DateTime.parse("2018-03-13", DateTimeFormat.forPattern("yyyy-MM-dd")));
			normalTransaction_1.setCategory(categoryNone);
			normalTransaction_1.setName("Income");
			normalTransaction_1.setDescription("Lorem Ipsum");
			normalTransaction_1.setTags(new ArrayList<>());
			normalTransaction_1.setAccount(account1);
			assertTrue(database.getTransactions().contains(normalTransaction_1));

			Transaction normalTransaction_2 = new Transaction();
			normalTransaction_2.setAmount(-2000);
			normalTransaction_2.setDate(DateTime.parse("2018-06-15", DateTimeFormat.forPattern("yyyy-MM-dd")));
			normalTransaction_2.setName("Simple");
			normalTransaction_2.setDescription("");
			normalTransaction_2.setAccount(account2);
			normalTransaction_2.setCategory(category3);

			List<Tag> tags = new ArrayList<>();
			Tag tag = new Tag("0815");
			tag.setID(1);
			tags.add(tag);
			normalTransaction_2.setTags(tags);
			assertTrue(database.getTransactions().contains(normalTransaction_2));

			Transaction repeatingTransaction_1 = new Transaction();
			repeatingTransaction_1.setAmount(-12300);
			DateTime repeatingTransactionDate_1 = DateTime.parse("2018-03-13", DateTimeFormat.forPattern("yyyy-MM-dd"));
			repeatingTransaction_1.setDate(repeatingTransactionDate_1);
			repeatingTransaction_1.setCategory(categoryNone);
			repeatingTransaction_1.setName("Test");
			repeatingTransaction_1.setDescription("");
			repeatingTransaction_1.setAccount(account1);
			RepeatingOption repeatingOption_1 = new RepeatingOption();
			repeatingOption_1.setModifier(new RepeatingModifierDays(10));
			repeatingOption_1.setStartDate(repeatingTransactionDate_1);
			repeatingOption_1.setEndOption(new RepeatingEndAfterXTimes(2));
			repeatingTransaction_1.setRepeatingOption(repeatingOption_1);
			repeatingTransaction_1.setTags(new ArrayList<>());
			assertTrue(database.getTransactions().contains(repeatingTransaction_1));

			Transaction repeatingTransaction_2 = new Transaction();
			repeatingTransaction_2.setAmount(-12300);
			DateTime repeatingTransactionDate_2 = DateTime.parse("2018-03-23", DateTimeFormat.forPattern("yyyy-MM-dd"));
			repeatingTransaction_2.setDate(repeatingTransactionDate_2);
			repeatingTransaction_2.setCategory(categoryNone);
			repeatingTransaction_2.setName("Test");
			repeatingTransaction_2.setDescription("");
			repeatingTransaction_2.setAccount(account1);
			RepeatingOption repeatingOption_2 = new RepeatingOption();
			repeatingOption_2.setModifier(new RepeatingModifierDays(10));
			repeatingOption_2.setStartDate(repeatingTransactionDate_2);
			repeatingOption_2.setEndOption(new RepeatingEndAfterXTimes(2));
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