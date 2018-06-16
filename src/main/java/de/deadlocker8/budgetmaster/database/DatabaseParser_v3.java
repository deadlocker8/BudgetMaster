package de.deadlocker8.budgetmaster.database;

import com.google.gson.*;
import de.deadlocker8.budgetmaster.entities.Account;
import de.deadlocker8.budgetmaster.entities.Category;
import de.deadlocker8.budgetmaster.entities.Payment;
import de.deadlocker8.budgetmaster.entities.Tag;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.endoption.*;
import de.deadlocker8.budgetmaster.repeating.modifier.*;
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
	private List<Category> categories;
	private List<Account> accounts;


	public DatabaseParser_v3(String json)
	{
		this.jsonString = json;
	}

	public Database parseDatabaseFromJSON() throws IllegalArgumentException
	{
		JsonObject root = new JsonParser().parse(jsonString).getAsJsonObject();
		categories = parseCategories(root);
		accounts = parseAccounts(root);
		List<Payment> payments = parsePayments(root);

		return new Database(categories, accounts, null);
	}

	private List<Category> parseCategories(JsonObject root)
	{
		List<Category> parsedCategories = new ArrayList<>();
		JsonArray categories = root.get("categories").getAsJsonArray();
		for(JsonElement currentCategory : categories)
		{
			parsedCategories.add(new Gson().fromJson(currentCategory, Category.class));
		}

		return parsedCategories;
	}

	private List<Account> parseAccounts(JsonObject root)
	{
		List<Account> parsedAccounts = new ArrayList<>();
		JsonArray accounts = root.get("accounts").getAsJsonArray();
		for(JsonElement currentAccount : accounts)
		{
			parsedAccounts.add(new Gson().fromJson(currentAccount, Account.class));
		}

		return parsedAccounts;
	}

	private List<Payment> parsePayments(JsonObject root)
	{
		List<Payment> parsedPayments = new ArrayList<>();
		JsonArray payments = root.get("payments").getAsJsonArray();
		for(JsonElement currentPayment : payments)
		{
			int amount = currentPayment.getAsJsonObject().get("amount").getAsInt();
			String name = currentPayment.getAsJsonObject().get("name").getAsString();
			String description = currentPayment.getAsJsonObject().get("description").getAsString();

			Payment payment = new Payment();
			payment.setAmount(amount);
			payment.setName(name);
			payment.setDescription(description);
			payment.setTags(parseTags(currentPayment.getAsJsonObject()));

			int categoryID = currentPayment.getAsJsonObject().get("category").getAsJsonObject().get("ID").getAsInt();
			payment.setCategory(getCategoryByID(categoryID));

			int accountID = currentPayment.getAsJsonObject().get("account").getAsJsonObject().get("ID").getAsInt();
			payment.setAccount(getAccountByID(accountID));

			String date = currentPayment.getAsJsonObject().get("date").getAsString();
			DateTime parsedDate = DateTime.parse(date, DateTimeFormat.forPattern("dd.MM.yyyy"));
			payment.setDate(parsedDate);

			payment.setRepeatingOption(parseRepeatingOption(currentPayment.getAsJsonObject(), parsedDate));

			parsedPayments.add(payment);
		}

		return parsedPayments;
	}

	private RepeatingOption parseRepeatingOption(JsonObject payment, DateTime startDate)
	{
		if(!payment.has("repeatingOption"))
		{
			return null;
		}

		JsonObject option = payment.get("repeatingOption").getAsJsonObject();

		JsonObject repeatingModifier = option.get("modifier").getAsJsonObject();
		String repeatingModifierType = repeatingModifier.get("localizationKey").getAsString();

		RepeatingModifierType type = RepeatingModifierType.getByLocalization(repeatingModifierType);
		RepeatingModifier modifier = RepeatingModifier.fromModifierType(type, repeatingModifier.get("quantity").getAsInt());

		JsonObject repeatingEnd = option.get("endOption").getAsJsonObject();
		String repeatingEndType = repeatingEnd.get("localizationKey").getAsString();

		RepeatingEnd endOption = null;
		RepeatingEndType endType = RepeatingEndType.getByLocalization(repeatingEndType);
		switch(endType)
		{
			case NEVER:
				endOption = new RepeatingEndNever();
				break;
			case AFTER_X_TIMES:
				endOption = new RepeatingEndAfterXTimes(repeatingEnd.get("times").getAsInt());
				break;
			case DATE:
				DateTime endDate = DateTime.parse(repeatingEnd.get("endDate").getAsString(), DateTimeFormat.forPattern("dd.MM.yyyy"));
				endOption = new RepeatingEndDate(endDate);
				break;
		}

		RepeatingOption repeatingOption = new RepeatingOption();
		repeatingOption.setStartDate(startDate);
		repeatingOption.setEndOption(endOption);
		repeatingOption.setModifier(modifier);

		return repeatingOption;
	}

	private List<Tag> parseTags(JsonObject payment)
	{
		List<Tag> parsedTags = new ArrayList<>();
		JsonArray tags = payment.get("tags").getAsJsonArray();
		for(JsonElement currentTag : tags)
		{
			parsedTags.add(new Gson().fromJson(currentTag, Tag.class));
		}

		return parsedTags;
	}

	private Category getCategoryByID(int ID)
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

	private Account getAccountByID(int ID)
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