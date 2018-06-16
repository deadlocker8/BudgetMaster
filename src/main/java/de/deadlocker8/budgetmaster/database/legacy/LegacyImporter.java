package de.deadlocker8.budgetmaster.database.legacy;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.deadlocker8.budgetmaster.database.Database;
import de.deadlocker8.budgetmaster.entities.Account;
import de.deadlocker8.budgetmaster.entities.Category;
import de.deadlocker8.budgetmaster.entities.CategoryType;
import de.deadlocker8.budgetmaster.entities.Payment;
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
public class LegacyImporter
{
	private Account account;
	private String jsonString;
	private List<Category> categories;
	private List<Tag> tags;
	private List<TagMatch> tagMatches;

	public LegacyImporter(String json)
	{
		this.jsonString = json;
		this.account = new Account("LEGACY_IMPORT");
	}

	public Database parseDatabaseFromJSON()
	{
		JsonObject root = new JsonParser().parse(jsonString).getAsJsonObject();
		categories = new ArrayList<>(parseCategories(root));
		tags = parseTags(root);
		tagMatches = parseTagMatches(root);
		List<Payment> payments = parsePayments(root);

		List<Account> accounts = new ArrayList<>();
		accounts.add(account);

		return new Database(categories, accounts, payments);
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

	private List<Payment> parsePayments(JsonObject root)
	{
		List<Payment> parsedPayments = new ArrayList<>();
		parsedPayments.addAll(parseNormalPayments(root));
		parsedPayments.addAll(parseRepeatingPayments(root));

		return parsedPayments;
	}

	private List<Payment> parseNormalPayments(JsonObject root)
	{
		List<Payment> parsedPayments = new ArrayList<>();
		JsonArray payments = root.get("normalPayments").getAsJsonArray();
		for(JsonElement currentPayment : payments)
		{
			int ID = currentPayment.getAsJsonObject().get("ID").getAsInt();
			int amount = currentPayment.getAsJsonObject().get("amount").getAsInt();
			String date = currentPayment.getAsJsonObject().get("date").getAsString();
			int categoryID = currentPayment.getAsJsonObject().get("categoryID").getAsInt();
			String name = currentPayment.getAsJsonObject().get("name").getAsString();
			String description = currentPayment.getAsJsonObject().get("description").getAsString();

			Payment payment = new Payment();
			payment.setAmount(amount);
			payment.setName(name);
			payment.setDescription(description);
			payment.setCategory(getCategoryByID(categoryID));
			payment.setAccount(account);
			payment.setRepeatingOption(null);
			payment.setTags(getTagsByPaymentID(ID));

			DateTime parsedDate = DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd"));
			payment.setDate(parsedDate);

			parsedPayments.add(payment);
		}

		return parsedPayments;
	}

	private List<Payment> parseRepeatingPayments(JsonObject root)
	{
		List<Payment> parsedPayments = new ArrayList<>();
		JsonArray payments = root.get("repeatingPayments").getAsJsonArray();
		for(JsonElement currentPayment : payments)
		{
			int ID = currentPayment.getAsJsonObject().get("ID").getAsInt();
			int amount = currentPayment.getAsJsonObject().get("amount").getAsInt();
			String date = currentPayment.getAsJsonObject().get("date").getAsString();
			int categoryID = currentPayment.getAsJsonObject().get("categoryID").getAsInt();
			String name = currentPayment.getAsJsonObject().get("name").getAsString();
			String description = currentPayment.getAsJsonObject().get("description").getAsString();

			Payment payment = new Payment();
			payment.setAmount(amount);
			payment.setName(name);
			payment.setDescription(description);
			payment.setCategory(getCategoryByID(categoryID));
			payment.setTags(getTagsByPaymentID(ID));
			payment.setAccount(account);

			DateTime parsedDate = DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd"));
			payment.setDate(parsedDate);

			payment.setRepeatingOption(parseRepeatingOption(currentPayment.getAsJsonObject(), parsedDate));

			parsedPayments.add(payment);
		}

		return parsedPayments;
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
			repeatingOption.setModifier(new RepeatingModifierMonths(repeatMonthDay));
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

		return null;
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