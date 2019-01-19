package de.deadlocker8.budgetmaster.database;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.deadlocker8.budgetmaster.database.legacy.LegacyParser;
import de.deadlocker8.budgetmaster.entities.category.Category;
import de.thecodelabs.utils.util.Localization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			JsonObject root = new JsonParser().parse(jsonString).getAsJsonObject();
			String type = root.get("TYPE").getAsString();
			if(!type.equals(JSONIdentifier.BUDGETMASTER_DATABASE.toString()))
			{
				throw new IllegalArgumentException("JSON is not of type BUDGETMASTER_DATABASE");
			}

			int version = root.get("VERSION").getAsInt();
			LOGGER.info("Parsing Budgetmaster database with version " + version);

			if(version == 2)
			{
				Database database = new LegacyParser(jsonString, categoryNone).parseDatabaseFromJSON();
				LOGGER.debug("Parsed database with " + database.getTransactions().size() + " transactions, " + database.getCategories().size() + " categories and " + database.getAccounts().size() + " accounts");
				return database;
			}

			if(version == 3)
			{
				Database database = new DatabaseParser_v3(jsonString).parseDatabaseFromJSON();
				LOGGER.debug("Parsed database with " + database.getTransactions().size() + " transactions, " + database.getCategories().size() + " categories and " + database.getAccounts().size() + " accounts");
				return database;
			}

			throw new IllegalArgumentException(Localization.getString("error.database.import.unknown.version"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException(Localization.getString("error.database.import.invalid.json"));
		}
	}
}