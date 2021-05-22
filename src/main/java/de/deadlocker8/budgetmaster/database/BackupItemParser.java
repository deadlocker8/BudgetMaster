package de.deadlocker8.budgetmaster.database;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class BackupItemParser
{
	private BackupItemParser()
	{
	}

	public static <T> List<T> parseItems(JsonArray jsonArray, Class<T> itemType)
	{
		List<T> parsedItems = new ArrayList<>();
		for(JsonElement currentItem : jsonArray)
		{
			parsedItems.add(new Gson().fromJson(currentItem, itemType));
		}

		return parsedItems;
	}
}
