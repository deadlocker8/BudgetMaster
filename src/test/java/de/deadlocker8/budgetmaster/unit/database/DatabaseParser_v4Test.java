package de.deadlocker8.budgetmaster.unit.database;

import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.database.DatabaseParser_v4;
import de.deadlocker8.budgetmaster.database.model.v4.*;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndAfterXTimes;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierDays;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.thecodelabs.utils.util.Localization;
import de.thecodelabs.utils.util.Localization.LocalizationDelegate;
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


public class DatabaseParser_v4Test
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
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v4Test.json").toURI())));
			DatabaseParser_v4 parser = new DatabaseParser_v4(json);
			BackupDatabase_v4 database = parser.parseDatabaseFromJSON();

			final BackupCategory_v4 categoryNone = new BackupCategory_v4(1, "Keine Kategorie", "#FFFFFF", CategoryType.NONE);
			final BackupCategory_v4 categoryRest = new BackupCategory_v4(2, "Übertrag", "#FFFF00", CategoryType.REST);
			final BackupCategory_v4 category3 = new BackupCategory_v4(3, "0815", "#ffcc00", CategoryType.CUSTOM);

			assertThat(database.getCategories()).hasSize(3)
					.contains(categoryNone, categoryRest, category3);
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
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v4Test.json").toURI())));
			DatabaseParser_v4 parser = new DatabaseParser_v4(json);
			BackupDatabase_v4 database = parser.parseDatabaseFromJSON();

			assertThat(database.getAccounts()).hasSize(3);
			assertThat(database.getAccounts().get(0)).hasFieldOrPropertyWithValue("name", "Placeholder");
			assertThat(database.getAccounts().get(1)).hasFieldOrPropertyWithValue("name", "Default");
			assertThat(database.getAccounts().get(2)).hasFieldOrPropertyWithValue("name", "Second Account");
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
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v4Test.json").toURI())));
			DatabaseParser_v4 parser = new DatabaseParser_v4(json);
			BackupDatabase_v4 database = parser.parseDatabaseFromJSON();

			BackupAccount_v4 account1 = new BackupAccount_v4(2, "Default", AccountType.CUSTOM);
			BackupAccount_v4 account2 = new BackupAccount_v4(3, "Second Account", AccountType.CUSTOM);

			BackupCategory_v4 categoryNone = new BackupCategory_v4(1, "Keine Kategorie", "#FFFFFF", CategoryType.NONE);
			BackupCategory_v4 category3 = new BackupCategory_v4(3, "0815", "#ffcc00", CategoryType.CUSTOM);

			BackupTransaction_v4 normalTransaction_1 = new BackupTransaction_v4();
			normalTransaction_1.setAmount(35000);
			normalTransaction_1.setDate("2018-03-13");
			normalTransaction_1.setCategory(categoryNone);
			normalTransaction_1.setName("Income");
			normalTransaction_1.setDescription("Lorem Ipsum");
			normalTransaction_1.setTags(new ArrayList<>());
			normalTransaction_1.setAccount(account1);
			normalTransaction_1.setExpenditure(false);

			BackupTransaction_v4 normalTransaction_2 = new BackupTransaction_v4();
			normalTransaction_2.setAmount(-2000);
			normalTransaction_2.setDate("2018-06-15");
			normalTransaction_2.setName("Simple");
			normalTransaction_2.setDescription("");
			normalTransaction_2.setAccount(account2);
			normalTransaction_2.setCategory(category3);
			normalTransaction_2.setExpenditure(true);

			List<BackupTag_v4> tags = new ArrayList<>();
			BackupTag_v4 tag = new BackupTag_v4("0815");
			tags.add(tag);
			normalTransaction_2.setTags(tags);

			BackupTransaction_v4 repeatingTransaction_1 = new BackupTransaction_v4();
			repeatingTransaction_1.setAmount(-12300);
			String repeatingTransactionDate_1 = "2018-03-13";
			repeatingTransaction_1.setDate(repeatingTransactionDate_1);
			repeatingTransaction_1.setCategory(categoryNone);
			repeatingTransaction_1.setName("Test");
			repeatingTransaction_1.setDescription("");
			repeatingTransaction_1.setAccount(account1);
			BackupRepeatingOption_v4 repeatingOption_1 = new BackupRepeatingOption_v4();
			repeatingOption_1.setModifier(new BackupRepeatingModifier_v4(10, new RepeatingModifierDays(10).getLocalizationKey()));
			repeatingOption_1.setStartDate(repeatingTransactionDate_1);
			BackupRepeatingEndOption_v4 repeatingEndOption = new BackupRepeatingEndOption_v4();
			repeatingEndOption.setTimes(2);
			repeatingEndOption.setLocalizationKey(new RepeatingEndAfterXTimes(10).getLocalizationKey());
			repeatingOption_1.setEndOption(repeatingEndOption);
			repeatingTransaction_1.setRepeatingOption(repeatingOption_1);
			repeatingTransaction_1.setTags(new ArrayList<>());
			repeatingTransaction_1.setExpenditure(true);

			BackupTransaction_v4 transferTransaction = new BackupTransaction_v4();
			transferTransaction.setAmount(-250);
			transferTransaction.setDate("2018-06-15");
			transferTransaction.setName("Transfer");
			transferTransaction.setDescription("");
			transferTransaction.setAccount(account2);
			transferTransaction.setTransferAccount(account1);
			transferTransaction.setCategory(category3);
			transferTransaction.setTags(new ArrayList<>());
			transferTransaction.setExpenditure(true);

			assertThat(database.getTransactions()).hasSize(4)
					.contains(normalTransaction_1,
							normalTransaction_2,
							repeatingTransaction_1,
							transferTransaction);

		}
		catch(IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void test_Templates()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v4Test.json").toURI())));
			DatabaseParser_v4 parser = new DatabaseParser_v4(json);
			BackupDatabase_v4 database = parser.parseDatabaseFromJSON();

			BackupAccount_v4 account1 = new BackupAccount_v4(2, "Default", AccountType.CUSTOM);
			BackupAccount_v4 account2 = new BackupAccount_v4(3, "Second Account", AccountType.CUSTOM);

			BackupCategory_v4 categoryNone = new BackupCategory_v4(1, "Keine Kategorie", "#FFFFFF", CategoryType.NONE);
			BackupCategory_v4 category3 = new BackupCategory_v4(3, "0815", "#ffcc00", CategoryType.CUSTOM);

			BackupTemplate_v4 normalTemplate = new BackupTemplate_v4();
			normalTemplate.setAmount(1500);
			normalTemplate.setName("Income");
			normalTemplate.setTemplateName("My Simple Template");
			normalTemplate.setDescription("Lorem Ipsum");
			normalTemplate.setAccount(account1);
			normalTemplate.setCategory(categoryNone);
			normalTemplate.setExpenditure(false);

			List<BackupTag_v4> tags = new ArrayList<>();
			BackupTag_v4 tag = new BackupTag_v4("0815");
			tags.add(tag);
			normalTemplate.setTags(tags);

			BackupTemplate_v4 minimalTemplate = new BackupTemplate_v4();
			minimalTemplate.setTemplateName("My Minimal Template");
			minimalTemplate.setTags(new ArrayList<>());
			minimalTemplate.setExpenditure(true);

			BackupTemplate_v4 transferTemplate = new BackupTemplate_v4();
			transferTemplate.setTemplateName("My Transfer Template");
			transferTemplate.setAmount(-35000);
			transferTemplate.setAccount(account2);
			transferTemplate.setTransferAccount(account1);
			transferTemplate.setName("Income");
			transferTemplate.setDescription("Lorem Ipsum");
			transferTemplate.setCategory(category3);
			transferTemplate.setTags(tags);
			transferTemplate.setExpenditure(true);

			assertThat(database.getTemplates()).hasSize(3)
					.contains(normalTemplate, minimalTemplate, transferTemplate);

		}
		catch(IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}
	}
}