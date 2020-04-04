package de.deadlocker8.budgetmaster.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseParser_v4 extends DatabaseParser_v3
{
	final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private String jsonString;

	public DatabaseParser_v4(String json)
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
		final List<Transaction> transactions = parseTransactions(root);
		final List<Template> templates = parseTemplates(root);

		return new Database(categories, accounts, transactions, templates);
	}

	protected List<Template> parseTemplates(JsonObject root)
	{
		final List<Template> parsedTemplates = new ArrayList<>();
		final JsonArray templates = root.get("templates").getAsJsonArray();
		for(JsonElement currentTemplate : templates)
		{
			final JsonObject templateObject = currentTemplate.getAsJsonObject();

			final String templateName = templateObject.get("templateName").getAsString();

			final Template template = new Template();
			template.setTemplateName(templateName);
			template.setTags(super.parseTags(templateObject));

			final JsonElement element = templateObject.get("amount");
			if(element != null)
			{
				template.setAmount(element.getAsInt());
			}

			final JsonElement name = templateObject.get("name");
			if(name != null)
			{
				template.setName(name.getAsString());
			}

			final JsonElement description = templateObject.get("description");
			if(description != null)
			{
				template.setDescription(description.getAsString());
			}

			final Optional<Integer> categoryOptional = parseIDOfElementIfExists(templateObject, "category");
			categoryOptional.ifPresent(integer -> template.setCategory(super.getCategoryByID(integer)));

			final Optional<Integer> accountOptional = parseIDOfElementIfExists(templateObject, "account");
			accountOptional.ifPresent(integer -> template.setAccount(super.getAccountByID(integer)));

			final Optional<Integer> transferAccountOptional = parseIDOfElementIfExists(templateObject, "transferAccount");
			transferAccountOptional.ifPresent(integer -> template.setTransferAccount(super.getAccountByID(integer)));

			parsedTemplates.add(template);
		}

		return parsedTemplates;
	}

	private Optional<Integer> parseIDOfElementIfExists(JsonObject jsonObject, String elementName)
	{
		final JsonElement element = jsonObject.get(elementName);
		if(element != null)
		{
			return Optional.of(element.getAsJsonObject().get("ID").getAsInt());
		}
		return  Optional.empty();
	}
}