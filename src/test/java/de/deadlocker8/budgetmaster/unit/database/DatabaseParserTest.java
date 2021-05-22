package de.deadlocker8.budgetmaster.unit.database;

import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.DatabaseParser;
import de.thecodelabs.utils.util.Localization;
import de.thecodelabs.utils.util.Localization.LocalizationDelegate;
import de.thecodelabs.utils.util.localization.LocalizationMessageFormatter;
import de.thecodelabs.utils.util.localization.formatter.JavaMessageFormatter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


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
			public LocalizationMessageFormatter messageFormatter()
			{
				return new JavaMessageFormatter();
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
		DatabaseParser importer = new DatabaseParser(json);
		final InternalDatabase database = importer.parseDatabaseFromJSON();
		assertThat(database.getTransactions())
				.hasSize(4);
	}

	@Test
	public void test_v4() throws URISyntaxException, IOException
	{
		String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v4Test.json").toURI())));
		DatabaseParser importer = new DatabaseParser(json);
		final InternalDatabase database = importer.parseDatabaseFromJSON();
		assertThat(database.getTransactions())
				.hasSize(4);
	}

	@Test
	public void test_v3() throws URISyntaxException, IOException
	{
		String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("DatabaseParser_v3Test.json").toURI())));
		DatabaseParser importer = new DatabaseParser(json);
		assertThatThrownBy(importer::parseDatabaseFromJSON)
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("too old");
	}

	@Test
	public void test_v2() throws URISyntaxException, IOException
	{
		String json = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("LegacyParserTest.json").toURI())));
		DatabaseParser importer = new DatabaseParser(json);
		assertThatThrownBy(importer::parseDatabaseFromJSON)
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("too old");
	}
}