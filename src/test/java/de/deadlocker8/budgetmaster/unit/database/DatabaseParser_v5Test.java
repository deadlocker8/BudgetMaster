package de.deadlocker8.budgetmaster.unit.database;

import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.charts.ChartType;
import de.deadlocker8.budgetmaster.database.DatabaseParser_v4;
import de.deadlocker8.budgetmaster.database.DatabaseParser_v5;
import de.deadlocker8.budgetmaster.database.model.v4.*;
import de.deadlocker8.budgetmaster.database.model.v5.*;
import de.deadlocker8.budgetmaster.images.ImageFileExtension;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndAfterXTimes;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierDays;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class DatabaseParser_v5Test
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
	public void test_Charts()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v5Test.json").toURI())));
			DatabaseParser_v5 parser = new DatabaseParser_v5(json);
			BackupDatabase_v5 database = parser.parseDatabaseFromJSON();

			final BackupChart_v5 chart = new BackupChart_v5(9, "The best chart", "/* This list will be dynamically filled with all the transactions between\r\n* the start and and date you select on the \"Show Chart\" page\r\n* and filtered according to your specified filter.\r\n* An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:\r\n* https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts\r\n*/\r\nvar transactionData \u003d [];\r\n\r\n// Prepare your chart settings here (mandatory)\r\nvar plotlyData \u003d [{\r\n    x: [],\r\n    y: [],\r\n    type: \u0027bar\u0027\r\n}];\r\n\r\n// Add your Plotly layout settings here (optional)\r\nvar plotlyLayout \u003d {};\r\n\r\n// Add your Plotly configuration settings here (optional)\r\nvar plotlyConfig \u003d {\r\n    showSendToCloud: false,\r\n    displaylogo: false,\r\n    showLink: false,\r\n    responsive: true\r\n};\r\n\r\n// Don\u0027t touch this line\r\nPlotly.newPlot(\"containerID\", plotlyData, plotlyLayout, plotlyConfig);\r\n", ChartType.CUSTOM, 7);

			assertThat(database.getCharts()).hasSize(1)
					.contains(chart);
		}
		catch(IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void test_Categories()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v5Test.json").toURI())));
			DatabaseParser_v5 parser = new DatabaseParser_v5(json);
			BackupDatabase_v5 database = parser.parseDatabaseFromJSON();

			final BackupCategory_v5 category = new BackupCategory_v5(3, "0815", "#ffcc00", CategoryType.CUSTOM, "fas fa-icons");

			assertThat(database.getCategories()).hasSize(3)
					.contains(category);
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
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v5Test.json").toURI())));
			DatabaseParser_v5 parser = new DatabaseParser_v5(json);
			BackupDatabase_v5 database = parser.parseDatabaseFromJSON();

			final BackupImage_v5 accountImage = new BackupImage_v5(1, new Byte[0], "awesomeIcon.png", ImageFileExtension.PNG);
			final BackupAccount_v5 account = new BackupAccount_v5(3, "Second Account", AccountState.FULL_ACCESS, AccountType.CUSTOM, accountImage);

			assertThat(database.getAccounts()).hasSize(3)
					.contains(account);
			assertThat(database.getAccounts().get(2).getIcon().getImage())
					.isNotNull()
					.hasSizeGreaterThan(1);
		}
		catch(IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void test_Images()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v5Test.json").toURI())));
			DatabaseParser_v5 parser = new DatabaseParser_v5(json);
			BackupDatabase_v5 database = parser.parseDatabaseFromJSON();

			assertThat(database.getImages()).hasSize(1);
			assertThat(database.getImages().get(0)).hasFieldOrPropertyWithValue("fileExtension", ImageFileExtension.PNG);
			assertThat(database.getImages().get(0).getImage())
					.isNotNull()
					.hasSizeGreaterThan(1);
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
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v5Test.json").toURI())));
			DatabaseParser_v5 parser = new DatabaseParser_v5(json);
			BackupDatabase_v5 database = parser.parseDatabaseFromJSON();

			BackupImage_v5 templateImage = new BackupImage_v5(1, new Byte[0], "awesomeIcon.png", ImageFileExtension.PNG);

			BackupTemplate_v5 template = new BackupTemplate_v5();
			template.setTemplateName("Template with icon");
			template.setExpenditure(true);
			template.setIcon(templateImage);
			template.setTags(List.of());

			assertThat(database.getTemplates()).hasSize(4)
					.contains(template);
			assertThat(database.getTemplates().get(3).getIcon().getImage())
					.isNotNull()
					.hasSizeGreaterThan(1);
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
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v5Test.json").toURI())));
			DatabaseParser_v5 parser = new DatabaseParser_v5(json);
			BackupDatabase_v5 database = parser.parseDatabaseFromJSON();

			BackupAccount_v5 account1 = new BackupAccount_v5(2, "Default", AccountState.FULL_ACCESS, AccountType.CUSTOM, null);

			BackupImage_v5 image = new BackupImage_v5(1, new Byte[0], "awesomeIcon.png", ImageFileExtension.PNG);

			BackupAccount_v5 account2 = new BackupAccount_v5(3, "Second Account", AccountState.FULL_ACCESS, AccountType.CUSTOM, image);

			BackupCategory_v5 categoryNone = new BackupCategory_v5(1, "Keine Kategorie", "#FFFFFF", CategoryType.NONE, null);

			BackupCategory_v5 category3 = new BackupCategory_v5(3, "0815", "#ffcc00", CategoryType.CUSTOM, "fas fa-icons");

			BackupTransaction_v5 normalTransaction_1 = new BackupTransaction_v5();
			normalTransaction_1.setAmount(35000);
			normalTransaction_1.setDate("2018-03-13");
			normalTransaction_1.setCategory(categoryNone);
			normalTransaction_1.setName("Income");
			normalTransaction_1.setDescription("Lorem Ipsum");
			normalTransaction_1.setTags(new ArrayList<>());
			normalTransaction_1.setAccount(account1);
			normalTransaction_1.setExpenditure(false);

			BackupTransaction_v5 normalTransaction_2 = new BackupTransaction_v5();
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

			BackupTransaction_v5 repeatingTransaction_1 = new BackupTransaction_v5();
			repeatingTransaction_1.setAmount(-12300);
			String repeatingTransaction_1Date = "2018-03-13";
			repeatingTransaction_1.setDate(repeatingTransaction_1Date);
			repeatingTransaction_1.setCategory(categoryNone);
			repeatingTransaction_1.setName("Test");
			repeatingTransaction_1.setDescription("");
			repeatingTransaction_1.setAccount(account1);
			BackupRepeatingOption_v4 repeatingOption_1 = new BackupRepeatingOption_v4();
			repeatingOption_1.setModifier(new BackupRepeatingModifier_v4(10, new RepeatingModifierDays(10).getLocalizationKey()));
			repeatingOption_1.setStartDate(repeatingTransaction_1Date);
			BackupRepeatingEndOption_v4 endOption = new BackupRepeatingEndOption_v4();
			endOption.setTimes(2);
			endOption.setLocalizationKey(new RepeatingEndAfterXTimes(2).getLocalizationKey());
			repeatingOption_1.setEndOption(endOption);
			repeatingTransaction_1.setRepeatingOption(repeatingOption_1);
			repeatingTransaction_1.setTags(new ArrayList<>());
			repeatingTransaction_1.setExpenditure(true);

			BackupTransaction_v5 transferTransaction = new BackupTransaction_v5();
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
	public void test_convertToInternalShouldFail() throws URISyntaxException, IOException
	{
		String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v5Test.json").toURI())));
		DatabaseParser_v5 parser = new DatabaseParser_v5(json);
		BackupDatabase_v5 database = parser.parseDatabaseFromJSON();

		assertThatThrownBy(database::convertToInternal)
				.isInstanceOf(UnsupportedOperationException.class);
	}

}