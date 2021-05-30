package de.deadlocker8.budgetmaster.database;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.deadlocker8.budgetmaster.database.model.v5.*;

public class DatabaseParser_v5
{
	private final String jsonString;

	private BackupDatabase_v5 database;

	public DatabaseParser_v5(String json)
	{
		this.jsonString = json;
		this.database = new BackupDatabase_v5();
	}

	public BackupDatabase_v5 parseDatabaseFromJSON() throws IllegalArgumentException
	{
		database = new BackupDatabase_v5();

		final JsonObject root = JsonParser.parseString(jsonString).getAsJsonObject();
		database.setImages(BackupItemParser.parseItems(root.get("images").getAsJsonArray(), BackupImage_v5.class));
		database.setAccounts(BackupItemParser.parseItems(root.get("accounts").getAsJsonArray(), BackupAccount_v5.class));
		database.setCategories(BackupItemParser.parseItems(root.get("categories").getAsJsonArray(), BackupCategory_v5.class));
		database.setTransactions(BackupItemParser.parseItems(root.get("transactions").getAsJsonArray(), BackupTransaction_v5.class));
		database.setTemplates(BackupItemParser.parseItems(root.get("templates").getAsJsonArray(), BackupTemplate_v5.class));
		database.setCharts(BackupItemParser.parseItems(root.get("charts").getAsJsonArray(), BackupChart_v5.class));

		return database;
	}
}