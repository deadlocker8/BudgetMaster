package de.deadlocker8.budgetmaster.database;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.deadlocker8.budgetmaster.database.model.v10.BackupCsvImportSettings_v10;
import de.deadlocker8.budgetmaster.database.model.v11.BackupAccount_v11;
import de.deadlocker8.budgetmaster.database.model.v11.BackupDatabase_v11;
import de.deadlocker8.budgetmaster.database.model.v5.BackupChart_v5;
import de.deadlocker8.budgetmaster.database.model.v5.BackupImage_v5;
import de.deadlocker8.budgetmaster.database.model.v6.BackupTransaction_v6;
import de.deadlocker8.budgetmaster.database.model.v7.BackupCategory_v7;
import de.deadlocker8.budgetmaster.database.model.v8.BackupIcon_v8;
import de.deadlocker8.budgetmaster.database.model.v8.BackupTemplateGroup_v8;
import de.deadlocker8.budgetmaster.database.model.v8.BackupTemplate_v8;
import de.deadlocker8.budgetmaster.database.model.v9.BackupTransactionNameKeyword_v9;

public class DatabaseParser_v11
{
	private final String jsonString;

	private BackupDatabase_v11 database;

	public DatabaseParser_v11(String json)
	{
		this.jsonString = json;
		this.database = new BackupDatabase_v11();
	}

	public BackupDatabase_v11 parseDatabaseFromJSON() throws IllegalArgumentException
	{
		database = new BackupDatabase_v11();

		final JsonObject root = JsonParser.parseString(jsonString).getAsJsonObject();
		database.setImages(BackupItemParser.parseItems(root.get("images").getAsJsonArray(), BackupImage_v5.class));
		database.setIcons(BackupItemParser.parseItems(root.get("icons").getAsJsonArray(), BackupIcon_v8.class));
		database.setAccounts(BackupItemParser.parseItems(root.get("accounts").getAsJsonArray(), BackupAccount_v11.class));
		database.setCategories(BackupItemParser.parseItems(root.get("categories").getAsJsonArray(), BackupCategory_v7.class));
		database.setTransactions(BackupItemParser.parseItems(root.get("transactions").getAsJsonArray(), BackupTransaction_v6.class));
		database.setTemplateGroups(BackupItemParser.parseItems(root.get("templateGroups").getAsJsonArray(), BackupTemplateGroup_v8.class));
		database.setTemplates(BackupItemParser.parseItems(root.get("templates").getAsJsonArray(), BackupTemplate_v8.class));
		database.setCharts(BackupItemParser.parseItems(root.get("charts").getAsJsonArray(), BackupChart_v5.class));
		database.setTransactionNameKeywords(BackupItemParser.parseItems(root.get("transactionNameKeywords").getAsJsonArray(), BackupTransactionNameKeyword_v9.class));
		database.setCsvImportSettings(BackupItemParser.parseItems(root.get("csvImportSettings").getAsJsonArray(), BackupCsvImportSettings_v10.class));

		return database;
	}
}