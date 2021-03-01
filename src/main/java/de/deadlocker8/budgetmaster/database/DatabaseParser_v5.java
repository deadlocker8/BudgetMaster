package de.deadlocker8.budgetmaster.database;

import com.google.gson.*;
import de.deadlocker8.budgetmaster.charts.Chart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DatabaseParser_v5 extends DatabaseParser_v4
{
	final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private String jsonString;

	protected List<Chart> charts;

	public DatabaseParser_v5(String json)
	{
		super(json);
		this.jsonString = json;
	}

	@Override
	public Database parseDatabaseFromJSON() throws IllegalArgumentException
	{
		final JsonObject root = JsonParser.parseString(jsonString).getAsJsonObject();
		super.categories = super.parseCategories(root);
		super.accounts = super.parseAccounts(root);
		super.transactions = super.parseTransactions(root);
		super.templates = super.parseTemplates(root);

		this.charts = parseCharts(root);

		return new Database(categories, accounts, transactions, templates, charts);
	}

	protected List<Chart> parseCharts(JsonObject root)
	{
		List<Chart> parsedCharts = new ArrayList<>();

		JsonArray chartsToImport = root.get("charts").getAsJsonArray();
		for(JsonElement currentChart : chartsToImport)
		{
			parsedCharts.add(new Gson().fromJson(currentChart, Chart.class));
		}

		return parsedCharts;
	}
}