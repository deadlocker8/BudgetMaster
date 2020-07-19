package de.deadlocker8.budgetmaster.database;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.deadlocker8.budgetmaster.database.legacy.LegacyParser;
import de.deadlocker8.budgetmaster.categories.Category;
import de.thecodelabs.utils.util.Localization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class DatabaseParser
{
	final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private String jsonString;
	private Category categoryNone;

	public DatabaseParser(String json, Category categoryNone)
	{
		this.jsonString = json;
		this.categoryNone = categoryNone;
	}

	public Database parseDatabaseFromJSON() throws IllegalArgumentException
	{
		try
		{
			final JsonObject root = JsonParser.parseString(jsonString).getAsJsonObject();
			final String type = root.get("TYPE").getAsString();
			if(!type.equals(JSONIdentifier.BUDGETMASTER_DATABASE.toString()))
			{
				throw new IllegalArgumentException("JSON is not of type BUDGETMASTER_DATABASE");
			}

			int version = root.get("VERSION").getAsInt();
			LOGGER.info(MessageFormat.format("Parsing BudgetMaster database with version {0}", version));

			if(version == 2)
			{
				final Database database = new LegacyParser(jsonString, categoryNone).parseDatabaseFromJSON();
				LOGGER.debug(MessageFormat.format("Parsed database with {0} transactions, {1} categories and {2} accounts", database.getTransactions().size(), database.getCategories().size(), database.getAccounts().size()));
				return database;
			}

			if(version == 3)
			{
				final Database database = new DatabaseParser_v3(jsonString).parseDatabaseFromJSON();
				LOGGER.debug(MessageFormat.format("Parsed database with {0} transactions, {1} categories and {2} accounts", database.getTransactions().size(), database.getCategories().size(), database.getAccounts().size()));
				return database;
			}

			if(version == 4)
			{
				final Database database = new DatabaseParser_v4(jsonString).parseDatabaseFromJSON();
				LOGGER.debug(MessageFormat.format("Parsed database with {0} transactions, {1} categories and {2} accounts and {3} templates", database.getTransactions().size(), database.getCategories().size(), database.getAccounts().size(), database.getTemplates().size()));
				return database;
			}

			throw new IllegalArgumentException(Localization.getString("error.database.import.unknown.version"));
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException(Localization.getString("error.database.import.invalid.json"), e);
		}
	}
}