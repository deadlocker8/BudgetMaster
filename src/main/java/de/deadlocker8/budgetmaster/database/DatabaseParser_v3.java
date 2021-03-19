package de.deadlocker8.budgetmaster.database;

import com.google.gson.*;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.endoption.*;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifier;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierType;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.thecodelabs.utils.util.Localization;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DatabaseParser_v3
{
	final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private String jsonString;
	protected List<Category> categories;
	protected List<Account> accounts;

	public DatabaseParser_v3(String json)
	{
		this.jsonString = json;
	}

	public Database parseDatabaseFromJSON() throws IllegalArgumentException
	{
		JsonObject root = JsonParser.parseString(jsonString).getAsJsonObject();
		categories = parseCategories(root);
		accounts = parseAccounts(root);
		List<Transaction> transactions = parseTransactions(root);

		return new Database(categories, accounts, transactions, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
	}

	protected List<Category> parseCategories(JsonObject root)
	{
		List<Category> parsedCategories = new ArrayList<>();
		JsonArray categories = root.get("categories").getAsJsonArray();
		for(JsonElement currentCategory : categories)
		{
			Category parsedCategory = new Gson().fromJson(currentCategory, Category.class);
			parsedCategories.add(parsedCategory);
		}

		return parsedCategories;
	}

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

			Account parsedAccount = new Account(name, accountType, null);
			parsedAccount.setID(ID);
			parsedAccount.setAccountState(accountState);

			parsedAccounts.add(parsedAccount);
		}

		return parsedAccounts;
	}

	protected List<Transaction> parseTransactions(JsonObject root)
	{
		List<Transaction> parsedTransactions = new ArrayList<>();
		JsonArray transactions = root.get("transactions").getAsJsonArray();
		for(JsonElement currentTransaction : transactions)
		{
			int amount = currentTransaction.getAsJsonObject().get("amount").getAsInt();
			String name = currentTransaction.getAsJsonObject().get("name").getAsString();
			String description = currentTransaction.getAsJsonObject().get("description").getAsString();

			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			transaction.setName(name);
			transaction.setDescription(description);
			transaction.setTags(parseTags(currentTransaction.getAsJsonObject()));

			int categoryID = currentTransaction.getAsJsonObject().get("category").getAsJsonObject().get("ID").getAsInt();
			transaction.setCategory(getCategoryByID(categoryID));

			int accountID = currentTransaction.getAsJsonObject().get("account").getAsJsonObject().get("ID").getAsInt();
			transaction.setAccount(getAccountByID(accountID));

			JsonElement transferAccount = currentTransaction.getAsJsonObject().get("transferAccount");
			if(transferAccount != null)
			{
				int transferAccountID = transferAccount.getAsJsonObject().get("ID").getAsInt();
				transaction.setTransferAccount(getAccountByID(transferAccountID));
			}

			String date = currentTransaction.getAsJsonObject().get("date").getAsString();
			DateTime parsedDate = DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd"));
			transaction.setDate(parsedDate);

			transaction.setRepeatingOption(parseRepeatingOption(currentTransaction.getAsJsonObject(), parsedDate));

			parsedTransactions.add(transaction);
		}

		return parsedTransactions;
	}

	protected RepeatingOption parseRepeatingOption(JsonObject transaction, DateTime startDate)
	{
		if(!transaction.has("repeatingOption"))
		{
			return null;
		}

		JsonObject option = transaction.get("repeatingOption").getAsJsonObject();

		JsonObject repeatingModifier = option.get("modifier").getAsJsonObject();
		String repeatingModifierType = repeatingModifier.get("localizationKey").getAsString();

		RepeatingModifierType type = RepeatingModifierType.getByLocalization(Localization.getString(repeatingModifierType));
		RepeatingModifier modifier = RepeatingModifier.fromModifierType(type, repeatingModifier.get("quantity").getAsInt());

		JsonObject repeatingEnd = option.get("endOption").getAsJsonObject();
		String repeatingEndType = repeatingEnd.get("localizationKey").getAsString();

		RepeatingEnd endOption = null;
		RepeatingEndType endType = RepeatingEndType.getByLocalization(Localization.getString(repeatingEndType));
		switch(endType)
		{
			case NEVER:
				endOption = new RepeatingEndNever();
				break;
			case AFTER_X_TIMES:
				endOption = new RepeatingEndAfterXTimes(repeatingEnd.get("times").getAsInt());
				break;
			case DATE:
				DateTime endDate = DateTime.parse(repeatingEnd.get("endDate").getAsString(), DateTimeFormat.forPattern("yyyy-MM-dd"));
				endOption = new RepeatingEndDate(endDate);
				break;
		}

		RepeatingOption repeatingOption = new RepeatingOption();
		repeatingOption.setStartDate(startDate);
		repeatingOption.setEndOption(endOption);
		repeatingOption.setModifier(modifier);

		return repeatingOption;
	}

	protected List<Tag> parseTags(JsonObject transaction)
	{
		List<Tag> parsedTags = new ArrayList<>();
		JsonArray tags = transaction.get("tags").getAsJsonArray();
		for(JsonElement currentTag : tags)
		{
			parsedTags.add(new Gson().fromJson(currentTag, Tag.class));
		}

		return parsedTags;
	}

	protected Category getCategoryByID(int ID)
	{
		for(Category category : categories)
		{
			if(category.getID() == ID)
			{
				return category;
			}
		}

		return null;
	}

	protected Account getAccountByID(int ID)
	{
		for(Account account : accounts)
		{
			if(account.getID() == ID)
			{
				return account;
			}
		}

		return null;
	}
}