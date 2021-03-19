package de.deadlocker8.budgetmaster.database;

import com.google.gson.*;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.images.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DatabaseParser_v5 extends DatabaseParser_v4
{
	final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private String jsonString;

	protected List<Chart> charts;
	protected List<Image> images;

	public DatabaseParser_v5(String json)
	{
		super(json);
		this.jsonString = json;
	}

	@Override
	public Database parseDatabaseFromJSON() throws IllegalArgumentException
	{
		final JsonObject root = JsonParser.parseString(jsonString).getAsJsonObject();

		this.images = parseImages(root);
		super.accounts = parseAccounts(root);

		super.categories = super.parseCategories(root);
		super.transactions = super.parseTransactions(root);
		super.templates = super.parseTemplates(root);

		this.charts = parseCharts(root);

		return new Database(categories, accounts, transactions, templates, charts, images);
	}

	@Override
	protected List<Account> parseAccounts(JsonObject root)
	{
		List<Account> parsedAccounts = new ArrayList<>();
		JsonArray accounts = root.get("accounts").getAsJsonArray();
		for(JsonElement currentAccount : accounts)
		{
			final JsonObject accountObject = currentAccount.getAsJsonObject();
			Integer ID = accountObject.get("ID").getAsInt();
			String name = accountObject.get("name").getAsString();
			AccountType accountType = AccountType.valueOf(accountObject.get("type").getAsString());

			AccountState accountState = AccountState.FULL_ACCESS;
			if(accountObject.has("accountState"))
			{
				accountState = AccountState.valueOf(accountObject.get("accountState").getAsString());
			}

			Image icon = null;
			if(accountObject.has("icon"))
			{
				final Integer iconID = accountObject.get("icon").getAsJsonObject().get("ID").getAsInt();
				icon = this.images.stream().filter(image -> image.getID().equals(iconID)).findFirst().orElseThrow();
			}

			Account parsedAccount = new Account(name, accountType, icon);
			parsedAccount.setID(ID);
			parsedAccount.setAccountState(accountState);

			parsedAccounts.add(parsedAccount);
		}

		return parsedAccounts;
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

	protected List<Image> parseImages(JsonObject root)
	{
		List<Image> parsedImages = new ArrayList<>();

		JsonArray imagesToImport = root.get("images").getAsJsonArray();
		for(JsonElement currentImage : imagesToImport)
		{
			parsedImages.add(new Gson().fromJson(currentImage, Image.class));
		}

		return parsedImages;
	}
}