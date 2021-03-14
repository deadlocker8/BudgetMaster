package de.deadlocker8.budgetmaster.unit.database;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.charts.ChartType;
import de.deadlocker8.budgetmaster.database.Database;
import de.deadlocker8.budgetmaster.database.DatabaseParser_v5;
import de.deadlocker8.budgetmaster.images.Image;
import de.thecodelabs.utils.util.Localization;
import de.thecodelabs.utils.util.Localization.LocalizationDelegate;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;


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
			DatabaseParser_v5 importer = new DatabaseParser_v5(json);
			Database database = importer.parseDatabaseFromJSON();

			final Chart chart = new Chart("The best chart", "/* This list will be dynamically filled with all the transactions between\r\n* the start and and date you select on the \"Show Chart\" page\r\n* and filtered according to your specified filter.\r\n* An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:\r\n* https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts\r\n*/\r\nvar transactionData \u003d [];\r\n\r\n// Prepare your chart settings here (mandatory)\r\nvar plotlyData \u003d [{\r\n    x: [],\r\n    y: [],\r\n    type: \u0027bar\u0027\r\n}];\r\n\r\n// Add your Plotly layout settings here (optional)\r\nvar plotlyLayout \u003d {};\r\n\r\n// Add your Plotly configuration settings here (optional)\r\nvar plotlyConfig \u003d {\r\n    showSendToCloud: false,\r\n    displaylogo: false,\r\n    showLink: false,\r\n    responsive: true\r\n};\r\n\r\n// Don\u0027t touch this line\r\nPlotly.newPlot(\"containerID\", plotlyData, plotlyLayout, plotlyConfig);\r\n", ChartType.CUSTOM, 7);
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
	public void test_Categories()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v5Test.json").toURI())));
			DatabaseParser_v5 importer = new DatabaseParser_v5(json);
			Database database = importer.parseDatabaseFromJSON();

			final Category category = new Category("0815", "#ffcc00", CategoryType.CUSTOM, "fas fa-icons");
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
	public void test_Accounts()
	{
		try
		{
			String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v5Test.json").toURI())));
			DatabaseParser_v5 importer = new DatabaseParser_v5(json);
			Database database = importer.parseDatabaseFromJSON();

			final Image accountImage = new Image(new Byte[0], "png");
			accountImage.setID(1);
			final Account account = new Account("Second Account", AccountType.CUSTOM, accountImage);
			account.setID(3);

			assertThat(database.getAccounts()).hasSize(3)
					.contains(account);
			assertThat(database.getAccounts().get(2).getIcon().getImage())
					.hasSizeGreaterThan(1);
		}
		catch(IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}
	}
}