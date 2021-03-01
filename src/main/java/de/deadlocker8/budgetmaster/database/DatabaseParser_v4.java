package de.deadlocker8.budgetmaster.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionBase;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
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

		return new Database(categories, accounts, transactions, templates, new ArrayList<>());
	}

	@Override
	protected List<Transaction> parseTransactions(JsonObject root)
	{
		List<Transaction> parsedTransactions = new ArrayList<>();
		JsonArray transactions = root.get("transactions").getAsJsonArray();
		for(JsonElement currentTransaction : transactions)
		{
			final JsonObject transactionObject = currentTransaction.getAsJsonObject();


			int amount = transactionObject.get("amount").getAsInt();
			String name = transactionObject.get("name").getAsString();
			String description = transactionObject.get("description").getAsString();

			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			transaction.setName(name);
			transaction.setDescription(description);
			transaction.setTags(parseTags(transactionObject));

			int categoryID = transactionObject.get("category").getAsJsonObject().get("ID").getAsInt();
			transaction.setCategory(getCategoryByID(categoryID));

			int accountID = transactionObject.get("account").getAsJsonObject().get("ID").getAsInt();
			transaction.setAccount(getAccountByID(accountID));

			JsonElement transferAccount = transactionObject.get("transferAccount");
			if(transferAccount != null)
			{
				int transferAccountID = transferAccount.getAsJsonObject().get("ID").getAsInt();
				transaction.setTransferAccount(getAccountByID(transferAccountID));
			}

			String date = transactionObject.get("date").getAsString();
			DateTime parsedDate = DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd"));
			transaction.setDate(parsedDate);

			transaction.setRepeatingOption(super.parseRepeatingOption(transactionObject, parsedDate));

			handleIsExpenditure(transactionObject, transaction);

			parsedTransactions.add(transaction);
		}

		return parsedTransactions;
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

			handleIsExpenditure(templateObject, template);

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
		return Optional.empty();
	}

	private void handleIsExpenditure(JsonObject jsonObject, TransactionBase transactionBase)
	{
		final JsonElement isExpenditure = jsonObject.get("isExpenditure");
		if(isExpenditure == null)
		{
			if(transactionBase.getAmount() == null)
			{
				transactionBase.setIsExpenditure(true);
			}
			else
			{
				transactionBase.setIsExpenditure(transactionBase.getAmount() <= 0);
			}
		}
		else
		{
			transactionBase.setIsExpenditure(isExpenditure.getAsBoolean());
		}
	}

}