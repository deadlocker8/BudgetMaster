package de.deadlocker8.budgetmaster.database;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.deadlocker8.budgetmaster.database.model.v5.BackupChart_v5;
import de.deadlocker8.budgetmaster.database.model.v5.BackupImage_v5;
import de.deadlocker8.budgetmaster.database.model.v6.BackupTransaction_v6;
import de.deadlocker8.budgetmaster.database.model.v7.*;

public class DatabaseParser_v7
{
	private final String jsonString;

	private BackupDatabase_v7 database;

	public DatabaseParser_v7(String json)
	{
		this.jsonString = json;
		this.database = new BackupDatabase_v7();
	}

	public BackupDatabase_v7 parseDatabaseFromJSON() throws IllegalArgumentException
	{
		database = new BackupDatabase_v7();

		final JsonObject root = JsonParser.parseString(jsonString).getAsJsonObject();
		database.setImages(BackupItemParser.parseItems(root.get("images").getAsJsonArray(), BackupImage_v5.class));
		database.setIcons(BackupItemParser.parseItems(root.get("icons").getAsJsonArray(), BackupIcon_v7.class));
		database.setAccounts(BackupItemParser.parseItems(root.get("accounts").getAsJsonArray(), BackupAccount_v7.class));
		database.setCategories(BackupItemParser.parseItems(root.get("categories").getAsJsonArray(), BackupCategory_v7.class));
		database.setTransactions(BackupItemParser.parseItems(root.get("transactions").getAsJsonArray(), BackupTransaction_v6.class));
		database.setTemplates(BackupItemParser.parseItems(root.get("templates").getAsJsonArray(), BackupTemplate_v7.class));
		database.setCharts(BackupItemParser.parseItems(root.get("charts").getAsJsonArray(), BackupChart_v5.class));

		return database;
	}
}