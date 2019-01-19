package de.deadlocker8.budgetmaster.database.legacy;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.deadlocker8.budgetmaster.database.Database;
import de.deadlocker8.budgetmaster.entities.*;
import de.deadlocker8.budgetmaster.entities.account.Account;
import de.deadlocker8.budgetmaster.entities.account.AccountType;
import de.deadlocker8.budgetmaster.entities.category.Category;
import de.deadlocker8.budgetmaster.entities.category.CategoryType;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndDate;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndNever;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierDays;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierMonths;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("deprecation")
public class LegacyParser
{
	private Account account;
	private String jsonString;
	private List<Category> categories;
	private List<Tag> tags;
	private List<TagMatch> tagMatches;
	private Category categoryNone;

	public LegacyParser(String json, Category categoryNone)
	{
		this.jsonString = json;
		this.account = new Account("LEGACY_IMPORT", AccountType.CUSTOM);
		this.categoryNone = categoryNone;
	}

	public Database parseDatabaseFromJSON()
	{
		JsonObject root = new JsonParser().parse(jsonString).getAsJsonObject();
		categories = new ArrayList<>(parseCategories(root));
		tags = parseTags(root);
		tagMatches = parseTagMatches(root);
		List<Transaction> transactions = parsePayments(root);

		List<Account> accounts = new ArrayList<>();
		accounts.add(account);

		return new Database(categories, accounts, transactions);
	}

	private Set<Category> parseCategories(JsonObject root)
	{
		Set<Category> parsedCategories = new HashSet<>();
		JsonArray categories = root.get("categories").getAsJsonArray();
		for(JsonElement currentCategory : categories)
		{
			int ID = currentCategory.getAsJsonObject().get("ID").getAsInt();
			String name = currentCategory.getAsJsonObject().get("name").getAsString();
			String color = currentCategory.getAsJsonObject().get("color").getAsString();

			if(name.equals("NONE") || name.equals("Übertrag")|| name.equals("Rest"))
			{
				continue;
			}

			Category category = new Category(name, color, CategoryType.CUSTOM);
			category.setID(ID);
			parsedCategories.add(category);
		}

		return parsedCategories;
	}

	private List<Tag> parseTags(JsonObject root)
	{
		List<Tag> parsedTags = new ArrayList<>();
		JsonArray tags = root.get("tags").getAsJsonArray();
		for(JsonElement currentTag : tags)
		{
			int ID = currentTag.getAsJsonObject().get("ID").getAsInt();
			String name = currentTag.getAsJsonObject().get("name").getAsString();

			parsedTags.add(new Tag(ID, name));
		}

		return parsedTags;
	}

	private List<TagMatch> parseTagMatches(JsonObject root)
	{
		List<TagMatch> parsedTagMatches = new ArrayList<>();
		JsonArray tagMatches = root.get("tagMatches").getAsJsonArray();
		for(JsonElement currentTagMatch : tagMatches)
		{
			int tagID = currentTagMatch.getAsJsonObject().get("tagID").getAsInt();
			int paymentID = currentTagMatch.getAsJsonObject().get("paymentID").getAsInt();
			int repeatingPaymentID = currentTagMatch.getAsJsonObject().get("repeatingPaymentID").getAsInt();

			parsedTagMatches.add(new TagMatch(getTagByID(tagID).getName(), paymentID, repeatingPaymentID));
		}

		return parsedTagMatches;
	}

	private List<Transaction> parsePayments(JsonObject root)
	{
		List<Transaction> parsedTransactions = new ArrayList<>();
		parsedTransactions.addAll(parseNormalPayments(root));
		parsedTransactions.addAll(parseRepeatingPayments(root));

		return parsedTransactions;
	}

	private List<Transaction> parseNormalPayments(JsonObject root)
	{
		List<Transaction> parsedTransactions = new ArrayList<>();
		JsonArray payments = root.get("normalPayments").getAsJsonArray();
		for(JsonElement currentPayment : payments)
		{
			int ID = currentPayment.getAsJsonObject().get("ID").getAsInt();
			int amount = currentPayment.getAsJsonObject().get("amount").getAsInt();
			String date = currentPayment.getAsJsonObject().get("date").getAsString();
			int categoryID = currentPayment.getAsJsonObject().get("categoryID").getAsInt();
			String name = currentPayment.getAsJsonObject().get("name").getAsString();
			String description = currentPayment.getAsJsonObject().get("description").getAsString();

			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			transaction.setName(name);
			transaction.setDescription(description);
			transaction.setCategory(getCategoryByID(categoryID));
			transaction.setAccount(account);
			transaction.setRepeatingOption(null);
			transaction.setTags(getTagsByPaymentID(ID));

			DateTime parsedDate = DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd"));
			transaction.setDate(parsedDate);

			parsedTransactions.add(transaction);
		}

		return parsedTransactions;
	}

	private List<Transaction> parseRepeatingPayments(JsonObject root)
	{
		List<Transaction> parsedTransactions = new ArrayList<>();
		JsonArray payments = root.get("repeatingPayments").getAsJsonArray();
		for(JsonElement currentPayment : payments)
		{
			int ID = currentPayment.getAsJsonObject().get("ID").getAsInt();
			int amount = currentPayment.getAsJsonObject().get("amount").getAsInt();
			String date = currentPayment.getAsJsonObject().get("date").getAsString();
			int categoryID = currentPayment.getAsJsonObject().get("categoryID").getAsInt();
			String name = currentPayment.getAsJsonObject().get("name").getAsString();
			String description = currentPayment.getAsJsonObject().get("description").getAsString();

			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			transaction.setName(name);
			transaction.setDescription(description);
			transaction.setCategory(getCategoryByID(categoryID));
			transaction.setTags(getTagsByPaymentID(ID));
			transaction.setAccount(account);

			DateTime parsedDate = DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd"));
			transaction.setDate(parsedDate);

			transaction.setRepeatingOption(parseRepeatingOption(currentPayment.getAsJsonObject(), parsedDate));

			parsedTransactions.add(transaction);
		}

		return parsedTransactions;
	}

	private RepeatingOption parseRepeatingOption(JsonObject repeatingPayment, DateTime startDate)
	{
		RepeatingOption repeatingOption = new RepeatingOption();
		repeatingOption.setStartDate(startDate);

		// end option
		if(repeatingPayment.has("repeatEndDate"))
		{
			String repeatEndDate = repeatingPayment.get("repeatEndDate").getAsString();
			repeatingOption.setEndOption(new RepeatingEndDate(DateTime.parse(repeatEndDate, DateTimeFormat.forPattern("yyyy-MM-dd"))));
		}
		else
		{
			repeatingOption.setEndOption(new RepeatingEndNever());
		}

		// modifier
		int repeatMonthDay = repeatingPayment.get("repeatMonthDay").getAsInt();
		if(repeatMonthDay == 0)
		{
			int repeatInterval = repeatingPayment.get("repeatInterval").getAsInt();
			repeatingOption.setModifier(new RepeatingModifierDays(repeatInterval));
		}
		else
		{
			repeatingOption.setModifier(new RepeatingModifierMonths(1));
		}

		return repeatingOption;
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

		return categoryNone;
	}

	private Tag getTagByID(int ID)
	{
		for(Tag tag : tags)
		{
			if(tag.getID() == ID)
			{
				return tag;
			}
		}

		return null;
	}

	private List<de.deadlocker8.budgetmaster.entities.Tag> getTagsByPaymentID(int paymentID)
	{
		List<de.deadlocker8.budgetmaster.entities.Tag> tags = new ArrayList<>();
		for(TagMatch tagMatch : tagMatches)
		{
			if(tagMatch.getPaymentID() == paymentID)
			{
				tags.add(new de.deadlocker8.budgetmaster.entities.Tag(tagMatch.getTagName()));
			}
		}

		return tags;
	}
}