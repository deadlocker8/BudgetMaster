package de.deadlocker8.budgetmaster.database;

import com.google.gson.*;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.database.model.v5.*;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.endoption.*;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifier;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierType;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.thecodelabs.utils.util.Localization;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
			final JsonObject accountObject = currentAccount.getAsJsonObject();
			Integer ID = accountObject.get("ID").getAsInt();
			String name = accountObject.get("name").getAsString();
			AccountType accountType = AccountType.valueOf(accountObject.get("type").getAsString());

			AccountState accountState = AccountState.FULL_ACCESS;
			if(accountObject.has("accountState"))
			{
				accountState = AccountState.valueOf(accountObject.get("accountState").getAsString());
			}

			BackupImage_v5 icon = null;
			if(accountObject.has("icon"))
			{
				final Integer iconID = accountObject.get("icon").getAsJsonObject().get("ID").getAsInt();
				icon = database.getImages().stream().filter(image -> image.getID().equals(iconID)).findFirst().orElseThrow();
			}

			parsedAccounts.add(new BackupAccount_v5(ID, name, accountState, accountType, icon));
		}

		return parsedAccounts;
	}

	private List<BackupCategory_v5> parseCategories(JsonObject root)
	{
		List<BackupCategory_v5> parsedCategories = new ArrayList<>();
		JsonArray jsonCategories = root.get("categories").getAsJsonArray();
		for(JsonElement currentCategory : jsonCategories)
		{
			BackupCategory_v5 parsedCategory = new Gson().fromJson(currentCategory, BackupCategory_v5.class);
			parsedCategories.add(parsedCategory);
		}

		return parsedCategories;
	}

	private List<BackupTransaction_v5> parseTransactions(JsonObject root)
	{
		List<BackupTransaction_v5> parsedTransactions = new ArrayList<>();
		JsonArray transactionsToImport = root.get("transactions").getAsJsonArray();
		for(JsonElement currentTransaction : transactionsToImport)
		{
			final JsonObject transactionObject = currentTransaction.getAsJsonObject();

			int amount = transactionObject.get("amount").getAsInt();
			String name = transactionObject.get("name").getAsString();
			String description = transactionObject.get("description").getAsString();

			BackupTransaction_v5 transaction = new BackupTransaction_v5();
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

			transaction.setRepeatingOption(parseRepeatingOption(transactionObject, parsedDate));

			handleIsExpenditureForTransactions(transactionObject, transaction);

			parsedTransactions.add(transaction);
		}

		return parsedTransactions;
	}

	private RepeatingOption parseRepeatingOption(JsonObject transaction, DateTime startDate)
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
			final JsonObject templateObject = currentTemplate.getAsJsonObject();

			final String templateName = templateObject.get("templateName").getAsString();

			final BackupTemplate_v5 template = new BackupTemplate_v5();
			template.setTemplateName(templateName);
			template.setTags(parseTags(templateObject));

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

			if(templateObject.has("icon"))
			{
				final Integer iconID = templateObject.get("icon").getAsJsonObject().get("ID").getAsInt();
				template.setIcon(database.getImages().stream().filter(image -> image.getID().equals(iconID)).findFirst().orElseThrow());
			}

			final Optional<Integer> categoryOptional = parseIDOfElementIfExists(templateObject, "category");
			categoryOptional.ifPresent(integer -> template.setCategory(getCategoryByID(integer)));

			final Optional<Integer> accountOptional = parseIDOfElementIfExists(templateObject, "account");
			accountOptional.ifPresent(integer -> template.setAccount(getAccountByID(integer)));

			final Optional<Integer> transferAccountOptional = parseIDOfElementIfExists(templateObject, "transferAccount");
			transferAccountOptional.ifPresent(integer -> template.setTransferAccount(getAccountByID(integer)));

			handleIsExpenditure(templateObject, template);

			parsedTemplates.add(template);
		}

		return parsedTemplates;
	}

	private void handleIsExpenditure(JsonObject jsonObject, BackupTemplate_v5 template)
	{
		final JsonElement isExpenditure = jsonObject.get("isExpenditure");
		if(isExpenditure == null)
		{
			if(template.getAmount() == null)
			{
				template.setExpenditure(true);
			}
			else
			{
				template.setExpenditure(template.getAmount() <= 0);
			}
		}
		else
		{
			template.setExpenditure(isExpenditure.getAsBoolean());
		}
	}

	private void handleIsExpenditureForTransactions(JsonObject jsonObject, BackupTransaction_v5 transaction)
	{
		final JsonElement isExpenditure = jsonObject.get("isExpenditure");
		if(isExpenditure == null)
		{
			if(transaction.getAmount() == null)
			{
				transaction.setExpenditure(true);
			}
			else
			{
				transaction.setExpenditure(transaction.getAmount() <= 0);
			}
		}
		else
		{
			transaction.setExpenditure(isExpenditure.getAsBoolean());
		}
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

	private List<Tag> parseTags(JsonObject transaction)
	{
		List<Tag> parsedTags = new ArrayList<>();
		JsonArray tags = transaction.get("tags").getAsJsonArray();
		for(JsonElement currentTag : tags)
		{
			parsedTags.add(new Gson().fromJson(currentTag, Tag.class));
		}

		return parsedTags;
	}

	private BackupCategory_v5 getCategoryByID(int ID)
	{
		for(BackupCategory_v5 category : database.getCategories())
		{
			if(category.getID() == ID)
			{
				return category;
			}
		}

		return null;
	}

	private BackupAccount_v5 getAccountByID(int ID)
	{
		for(BackupAccount_v5 account : database.getAccounts())
		{
			if(account.getID() == ID)
			{
				return account;
			}
		}

		return null;
	}
}