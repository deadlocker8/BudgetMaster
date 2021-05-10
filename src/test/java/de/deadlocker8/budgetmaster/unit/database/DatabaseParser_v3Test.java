package de.deadlocker8.budgetmaster.unit.database;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.database.Database;
import de.deadlocker8.budgetmaster.database.DatabaseParser_v3;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndAfterXTimes;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierDays;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.transactions.Transaction;
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

import static org.assertj.core.api.Assertions.assertThat;

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
				return "languages/base";
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

			final Category categoryNone = new Category("Keine Kategorie", "#FFFFFF", CategoryType.NONE);
			categoryNone.setID(1);

			final Category categoryRest = new Category("Übertrag", "#FFFF00", CategoryType.REST);
			categoryRest.setID(2);

			final Category category3 = new Category("0815", "#ffcc00", CategoryType.CUSTOM);
			category3.setID(3);

			assertThat(database.getCategories()).hasSize(3)
					.containsExactlyInAnyOrder(categoryNone, categoryRest, category3);
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

			final Account expectedAccount1 = new Account("Placeholder", AccountType.ALL);
			expectedAccount1.setID(1);

			final Account expectedAccount2 = new Account("Default", AccountType.CUSTOM);
			expectedAccount2.setID(2);

			final Account expectedAccount3 = new Account("Second Account", AccountType.CUSTOM);
			expectedAccount3.setID(3);

			assertThat(database.getAccounts()).hasSize(3)
					.containsExactlyInAnyOrder(expectedAccount1, expectedAccount2, expectedAccount3);
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

			Transaction normalTransaction_1 = new Transaction();
			normalTransaction_1.setAmount(35000);
			normalTransaction_1.setDate(DateTime.parse("2018-03-13", DateTimeFormat.forPattern("yyyy-MM-dd")));
			normalTransaction_1.setCategory(categoryNone);
			normalTransaction_1.setName("Income");
			normalTransaction_1.setDescription("Lorem Ipsum");
			normalTransaction_1.setTags(new ArrayList<>());
			normalTransaction_1.setAccount(account1);

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

			Transaction transferTransaction = new Transaction();
			transferTransaction.setAmount(-250);
			transferTransaction.setDate(DateTime.parse("2018-06-15", DateTimeFormat.forPattern("yyyy-MM-dd")));
			transferTransaction.setName("Transfer");
			transferTransaction.setDescription("");
			transferTransaction.setAccount(account2);
			transferTransaction.setTransferAccount(account1);
			transferTransaction.setCategory(category3);
			transferTransaction.setTags(new ArrayList<>());

			assertThat(database.getTransactions()).hasSize(6)
					.contains(normalTransaction_1,
							normalTransaction_2,
							repeatingTransaction_1,
							repeatingTransaction_2,
							transferTransaction);

		}
		catch(IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}
	}
}
