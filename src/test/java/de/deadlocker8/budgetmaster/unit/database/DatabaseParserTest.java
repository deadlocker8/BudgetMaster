package de.deadlocker8.budgetmaster.unit.database;

import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.database.Database;
import de.deadlocker8.budgetmaster.database.DatabaseParser;
import de.thecodelabs.utils.util.Localization;
import de.thecodelabs.utils.util.Localization.LocalizationDelegate;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;


public class DatabaseParserTest
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
	public void test_v5() throws URISyntaxException, IOException
	{
		String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v5Test.json").toURI())));
		final Category categoryNone = new Category("NONE", "#FFFFFF", CategoryType.NONE);
		categoryNone.setID(1);

		DatabaseParser importer = new DatabaseParser(json, categoryNone);
		final Database database = importer.parseDatabaseFromJSON();
		assertThat(database.getTransactions())
				.hasSize(6);
	}

	@Test
	public void test_v4() throws URISyntaxException, IOException
	{
		String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v4Test.json").toURI())));
		final Category categoryNone = new Category("NONE", "#FFFFFF", CategoryType.NONE);
		categoryNone.setID(1);

		DatabaseParser importer = new DatabaseParser(json, categoryNone);
		final Database database = importer.parseDatabaseFromJSON();
		assertThat(database.getTransactions())
				.hasSize(6);
	}

	@Test
	public void test_v3() throws URISyntaxException, IOException
	{
		String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v3Test.json").toURI())));
		final Category categoryNone = new Category("NONE", "#FFFFFF", CategoryType.NONE);
		categoryNone.setID(1);

		DatabaseParser importer = new DatabaseParser(json, categoryNone);
		final Database database = importer.parseDatabaseFromJSON();
		assertThat(database.getTransactions())
				.hasSize(6);
	}

	@Test
	public void test_v2() throws URISyntaxException, IOException
	{
		String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("LegacyParserTest.json").toURI())));
		final Category categoryNone = new Category("NONE", "#FFFFFF", CategoryType.NONE);
		categoryNone.setID(1);

		DatabaseParser importer = new DatabaseParser(json, categoryNone);
		final Database database = importer.parseDatabaseFromJSON();
		assertThat(database.getTransactions())
				.hasSize(4);
	}
}