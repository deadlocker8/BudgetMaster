package de.deadlocker8.budgetmaster.database;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.deadlocker8.budgetmaster.database.model.v4.*;

import java.util.List;

public class DatabaseParser_v4
{
	private final String jsonString;

	public DatabaseParser_v4(String json)
	{
		this.jsonString = json;
	}

	public BackupDatabase_v4 parseDatabaseFromJSON() throws IllegalArgumentException
	{
		BackupDatabase_v4 database = new BackupDatabase_v4();

		final JsonObject root = JsonParser.parseString(jsonString).getAsJsonObject();
		database.setAccounts(BackupItemParser.parseItems(root.get("accounts").getAsJsonArray(), BackupAccount_v4.class));
		database.setCategories(BackupItemParser.parseItems(root.get("categories").getAsJsonArray(), BackupCategory_v4.class));

		database.setTransactions(BackupItemParser.parseItems(root.get("transactions").getAsJsonArray(), BackupTransaction_v4.class));
		fixMissingIsExpenditure(database.getTransactions());

		database.setTemplates(BackupItemParser.parseItems(root.get("templates").getAsJsonArray(), BackupTemplate_v4.class));
		fixMissingIsExpenditure(database.getTemplates());

		return database;
	}

	private void fixMissingIsExpenditure(List<? extends BackupTransactionBase_v4> items)
	{
		for(BackupTransactionBase_v4 item : items)
		{
			if(item.getExpenditure() != null)
			{
				continue;
			}

			if(item.getAmount() == null)
			{
				item.setExpenditure(true);
			}
			else
			{
				item.setExpenditure(item.getAmount() <= 0);
			}
		}
	}
}