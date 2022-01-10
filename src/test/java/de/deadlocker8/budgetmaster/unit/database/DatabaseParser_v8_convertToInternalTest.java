package de.deadlocker8.budgetmaster.unit.database;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.charts.ChartDisplayType;
import de.deadlocker8.budgetmaster.charts.ChartGroupType;
import de.deadlocker8.budgetmaster.charts.ChartType;
import de.deadlocker8.budgetmaster.database.DatabaseParser_v8;
import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageFileExtension;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndAfterXTimes;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierDays;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.thecodelabs.utils.util.Localization;
import de.thecodelabs.utils.util.Localization.LocalizationDelegate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;


class DatabaseParser_v8_convertToInternalTest
{
	@BeforeEach
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
	void test_Charts()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v8Test.json").toURI())));
			DatabaseParser_v8 importer = new DatabaseParser_v8(json);
			InternalDatabase database = importer.parseDatabaseFromJSON().convertToInternal();

			final Chart chart = new Chart("The best chart", "/* This list will be dynamically filled with all the transactions between\r\n* the start and and date you select on the \"Show Chart\" page\r\n* and filtered according to your specified filter.\r\n* An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:\r\n* https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts\r\n*/\r\nvar transactionData \u003d [];\r\n\r\n// Prepare your chart settings here (mandatory)\r\nvar plotlyData \u003d [{\r\n    x: [],\r\n    y: [],\r\n    type: \u0027bar\u0027\r\n}];\r\n\r\n// Add your Plotly layout settings here (optional)\r\nvar plotlyLayout \u003d {};\r\n\r\n// Add your Plotly configuration settings here (optional)\r\nvar plotlyConfig \u003d {\r\n    showSendToCloud: false,\r\n    displaylogo: false,\r\n    showLink: false,\r\n    responsive: true\r\n};\r\n\r\n// Don\u0027t touch this line\r\nPlotly.newPlot(\"containerID\", plotlyData, plotlyLayout, plotlyConfig);\r\n", ChartType.CUSTOM, 7, ChartDisplayType.CUSTOM, ChartGroupType.NONE, null);
			chart.setID(9);

			assertThat(database.getCharts()).hasSize(1)
					.contains(chart);
		}
		catch(IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	void test_Categories()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v8Test.json").toURI())));
			DatabaseParser_v8 importer = new DatabaseParser_v8(json);
			InternalDatabase database = importer.parseDatabaseFromJSON().convertToInternal();

			final Icon icon = new Icon("fas fa-icons");
			icon.setID(2);
			icon.setFontColor("#FF0000");

			final Category category = new Category("0815", "#ffcc00", CategoryType.CUSTOM, icon);
			category.setID(3);

			assertThat(database.getCategories()).hasSize(3)
					.contains(category);
		}
		catch(IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	void test_Accounts()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v8Test.json").toURI())));
			DatabaseParser_v8 importer = new DatabaseParser_v8(json);
			InternalDatabase database = importer.parseDatabaseFromJSON().convertToInternal();

			final Image accountImage = new Image(new Byte[0], "awesomeIcon.png", ImageFileExtension.PNG);
			accountImage.setID(1);

			final Icon icon = new Icon(accountImage);
			icon.setID(1);

			final Account account = new Account("Second Account", AccountType.CUSTOM, icon);
			account.setID(3);

			assertThat(database.getAccounts()).hasSize(3)
					.contains(account);
			assertThat(database.getAccounts().get(2).getIconReference())
					.isEqualTo(icon);
			assertThat(database.getAccounts().get(2).getIconReference().getImage().getImage())
					.isNotNull()
					.hasSizeGreaterThan(1);
		}
		catch(IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	void test_Images()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v8Test.json").toURI())));
			DatabaseParser_v8 importer = new DatabaseParser_v8(json);
			InternalDatabase database = importer.parseDatabaseFromJSON().convertToInternal();

			final Image image = new Image(new Byte[0], "awesomeIcon.png", ImageFileExtension.PNG);
			image.setID(1);

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
	void test_Templates()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v8Test.json").toURI())));
			DatabaseParser_v8 importer = new DatabaseParser_v8(json);
			InternalDatabase database = importer.parseDatabaseFromJSON().convertToInternal();

			final Image templateImage = new Image(new Byte[0], "awesomeIcon.png", ImageFileExtension.PNG);
			templateImage.setID(1);

			final Icon icon = new Icon(templateImage);
			icon.setID(1);

			final Template template = new Template();
			template.setTemplateName("Template with icon");
			template.setIsExpenditure(true);
			template.setIconReference(icon);
			template.setTags(List.of());

			assertThat(database.getTemplates()).hasSize(4)
					.contains(template);
			assertThat(database.getTemplates().get(3).getIconReference())
					.isEqualTo(icon);
			assertThat(database.getTemplates().get(3).getIconReference().getImage().getImage())
					.isNotNull()
					.hasSizeGreaterThan(1);
		}
		catch(IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	void test_Transactions()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v8Test.json").toURI())));
			DatabaseParser_v8 importer = new DatabaseParser_v8(json);
			InternalDatabase database = importer.parseDatabaseFromJSON().convertToInternal();

			Account account1 = new Account("Default", AccountType.CUSTOM);
			account1.setID(2);

			Image image = new Image(new Byte[0], "awesomeIcon.png", ImageFileExtension.PNG);
			image.setID(1);

			Icon accountIcon = new Icon(image);
			accountIcon.setID(1);

			Account account2 = new Account("Second Account", AccountType.CUSTOM);
			account2.setIconReference(accountIcon);
			account2.setID(3);

			Category categoryNone = new Category("Keine Kategorie", "#FFFFFF", CategoryType.NONE);
			categoryNone.setID(1);

			Icon categoryIcon = new Icon("fas fa-icons");
			categoryIcon.setID(2);
			categoryIcon.setFontColor("#FF0000");

			Category category3 = new Category("0815", "#ffcc00", CategoryType.CUSTOM);
			category3.setIconReference(categoryIcon);
			category3.setID(3);

			Transaction normalTransaction_1 = new Transaction();
			normalTransaction_1.setAmount(35000);
			DateTime normalTransactionDate = DateTime.parse("2018-03-13", DateTimeFormat.forPattern("yyyy-MM-dd"));
			normalTransactionDate = normalTransactionDate.withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0);
			normalTransaction_1.setDate(normalTransactionDate);
			normalTransaction_1.setCategory(categoryNone);
			normalTransaction_1.setName("Income");
			normalTransaction_1.setDescription("Lorem Ipsum");
			normalTransaction_1.setTags(new ArrayList<>());
			normalTransaction_1.setAccount(account1);
			normalTransaction_1.setIsExpenditure(false);

			Transaction normalTransaction_2 = new Transaction();
			normalTransaction_2.setAmount(-2000);
			DateTime normalTransaction_2Date = DateTime.parse("2018-06-15", DateTimeFormat.forPattern("yyyy-MM-dd"));
			normalTransaction_2Date = normalTransaction_2Date.withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0);
			normalTransaction_2.setDate(normalTransaction_2Date);
			normalTransaction_2.setName("Simple");
			normalTransaction_2.setDescription("");
			normalTransaction_2.setAccount(account2);
			normalTransaction_2.setCategory(category3);
			normalTransaction_2.setIsExpenditure(true);

			List<Tag> tags = new ArrayList<>();
			Tag tag = new Tag("0815");
			tags.add(tag);
			normalTransaction_2.setTags(tags);

			Transaction repeatingTransaction_1 = new Transaction();
			repeatingTransaction_1.setAmount(-12300);
			DateTime repeatingTransaction_1Date = DateTime.parse("2018-03-13", DateTimeFormat.forPattern("yyyy-MM-dd"));
			repeatingTransaction_1Date = repeatingTransaction_1Date.withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0);
			repeatingTransaction_1.setDate(repeatingTransaction_1Date);
			repeatingTransaction_1.setCategory(categoryNone);
			repeatingTransaction_1.setName("Test");
			repeatingTransaction_1.setDescription("");
			repeatingTransaction_1.setAccount(account1);
			RepeatingOption repeatingOption_1 = new RepeatingOption();
			repeatingOption_1.setModifier(new RepeatingModifierDays(10));
			repeatingOption_1.setStartDate(repeatingTransaction_1Date);
			repeatingOption_1.setEndOption(new RepeatingEndAfterXTimes(2));
			repeatingTransaction_1.setRepeatingOption(repeatingOption_1);
			repeatingTransaction_1.setTags(new ArrayList<>());
			repeatingTransaction_1.setIsExpenditure(true);

			Transaction transferTransaction = new Transaction();
			transferTransaction.setAmount(-250);
			DateTime transferTransactionDate = DateTime.parse("2018-06-15", DateTimeFormat.forPattern("yyyy-MM-dd"));
			transferTransactionDate = transferTransactionDate.withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0);
			transferTransaction.setDate(transferTransactionDate);
			transferTransaction.setName("Transfer");
			transferTransaction.setDescription("");
			transferTransaction.setAccount(account2);
			transferTransaction.setTransferAccount(account1);
			transferTransaction.setCategory(category3);
			transferTransaction.setTags(new ArrayList<>());
			transferTransaction.setIsExpenditure(true);

			Transaction repeatingTransferTransaction = new Transaction();
			repeatingTransferTransaction.setAmount(-6000);
			DateTime transferTransactionDate_2 = DateTime.parse("2018-03-15", DateTimeFormat.forPattern("yyyy-MM-dd"));
			transferTransactionDate_2 = transferTransactionDate_2.withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0);
			repeatingTransferTransaction.setDate(transferTransactionDate_2);
			repeatingTransferTransaction.setCategory(categoryNone);
			repeatingTransferTransaction.setName("repeat my transfer");
			repeatingTransferTransaction.setDescription("");
			repeatingTransferTransaction.setAccount(account1);
			RepeatingOption repeatingOption_2 = new RepeatingOption();
			repeatingOption_2.setModifier(new RepeatingModifierDays(10));
			repeatingOption_2.setStartDate(transferTransactionDate_2);
			repeatingOption_2.setEndOption(new RepeatingEndAfterXTimes(2));
			repeatingTransferTransaction.setRepeatingOption(repeatingOption_2);
			repeatingTransferTransaction.setTags(new ArrayList<>());
			repeatingTransferTransaction.setIsExpenditure(true);
			repeatingTransferTransaction.setTransferAccount(account1);

			assertThat(database.getTransactions()).hasSize(5)
					.contains(normalTransaction_1,
							normalTransaction_2,
							repeatingTransaction_1,
							transferTransaction,
							repeatingTransferTransaction);

		}
		catch(IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	void test_Icons() throws IOException, URISyntaxException
	{
		String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v8Test.json").toURI())));
		DatabaseParser_v8 importer = new DatabaseParser_v8(json);
		InternalDatabase database = importer.parseDatabaseFromJSON().convertToInternal();

		final Image image = new Image(new Byte[0], "awesomeIcon.png", ImageFileExtension.PNG);
		image.setID(1);

		final Icon icon1 = new Icon(image);
		icon1.setID(1);

		final Icon icon2 = new Icon("fas fa-icons");
		icon2.setID(2);
		icon2.setFontColor("#FF0000");

		assertThat(database.getIcons()).hasSize(2)
				.containsExactly(icon1, icon2);

		assertThat(database.getImages().get(0).getImage())
				.isNotNull()
				.hasSizeGreaterThan(1);
	}
}