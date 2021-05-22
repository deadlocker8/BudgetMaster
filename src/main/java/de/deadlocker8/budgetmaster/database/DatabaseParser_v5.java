package de.deadlocker8.budgetmaster.database;

import com.google.gson.*;
import de.deadlocker8.budgetmaster.database.model.v5.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DatabaseParser_v5
{
	final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
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
		database.setImages(parseImages(root));
		database.setAccounts(parseAccounts(root));
		database.setCategories(parseCategories(root));
		database.setTransactions(parseTransactions(root));
		database.setTemplates(parseTemplates(root));
		database.setCharts(parseCharts(root));

		return database;
	}

	private List<BackupAccount_v5> parseAccounts(JsonObject root)
	{
		List<BackupAccount_v5> parsedAccounts = new ArrayList<>();
		JsonArray accounts = root.get("accounts").getAsJsonArray();
		for(JsonElement currentAccount : accounts)
		{
			parsedAccounts.add(new Gson().fromJson(currentAccount, BackupAccount_v5.class));
		}

		return parsedAccounts;
	}

	private List<BackupCategory_v5> parseCategories(JsonObject root)
	{
		List<BackupCategory_v5> parsedCategories = new ArrayList<>();
		JsonArray jsonCategories = root.get("categories").getAsJsonArray();
		for(JsonElement currentCategory : jsonCategories)
		{
			parsedCategories.add(new Gson().fromJson(currentCategory, BackupCategory_v5.class));
		}

		return parsedCategories;
	}

	private List<BackupTransaction_v5> parseTransactions(JsonObject root)
	{
		List<BackupTransaction_v5> parsedTransactions = new ArrayList<>();
		JsonArray transactionsToImport = root.get("transactions").getAsJsonArray();
		for(JsonElement currentTransaction : transactionsToImport)
		{
			parsedTransactions.add(new Gson().fromJson(currentTransaction, BackupTransaction_v5.class));
		}

		return parsedTransactions;
	}

	private List<BackupChart_v5> parseCharts(JsonObject root)
	{
		List<BackupChart_v5> parsedCharts = new ArrayList<>();

		JsonArray chartsToImport = root.get("charts").getAsJsonArray();
		for(JsonElement currentChart : chartsToImport)
		{
			parsedCharts.add(new Gson().fromJson(currentChart, BackupChart_v5.class));
		}

		return parsedCharts;
	}

	private List<BackupImage_v5> parseImages(JsonObject root)
	{
		List<BackupImage_v5> parsedImages = new ArrayList<>();

		JsonArray imagesToImport = root.get("images").getAsJsonArray();
		for(JsonElement currentImage : imagesToImport)
		{
			parsedImages.add(new Gson().fromJson(currentImage, BackupImage_v5.class));
		}

		return parsedImages;
	}

	private List<BackupTemplate_v5> parseTemplates(JsonObject root)
	{
		final List<BackupTemplate_v5> parsedTemplates = new ArrayList<>();
		final JsonArray templatesToImport = root.get("templates").getAsJsonArray();
		for(JsonElement currentTemplate : templatesToImport)
		{
			parsedTemplates.add(new Gson().fromJson(currentTemplate, BackupTemplate_v5.class));
		}

		return parsedTemplates;
	}
}