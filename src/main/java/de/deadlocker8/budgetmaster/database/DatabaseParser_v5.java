package de.deadlocker8.budgetmaster.database;

import com.google.gson.*;
import de.deadlocker8.budgetmaster.database.model.v5.*;

import java.util.ArrayList;
import java.util.List;

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
		database.setImages(parseItems(root.get("images").getAsJsonArray(), BackupImage_v5.class));
		database.setAccounts(parseItems(root.get("accounts").getAsJsonArray(), BackupAccount_v5.class));
		database.setCategories(parseItems(root.get("categories").getAsJsonArray(), BackupCategory_v5.class));
		database.setTransactions(parseItems(root.get("transactions").getAsJsonArray(), BackupTransaction_v5.class));
		database.setTemplates(parseItems(root.get("templates").getAsJsonArray(), BackupTemplate_v5.class));
		database.setCharts(parseItems(root.get("charts").getAsJsonArray(), BackupChart_v5.class));

		return database;
	}

	private <T> List<T> parseItems(JsonArray jsonArray, Class<T> itemType)
	{
		List<T> parsedItems = new ArrayList<>();
		for(JsonElement currentItem : jsonArray)
		{
			parsedItems.add(new Gson().fromJson(currentItem, itemType));
		}

		return parsedItems;
	}
}