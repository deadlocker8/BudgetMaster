package de.deadlocker8.budgetmaster.database;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DatabaseImporter
{
	private String jsonString;

	public DatabaseImporter(String json)
	{
		this.jsonString = json;
	}

	public Database parseJSON()
	{
		JsonObject root = new JsonParser().parse(jsonString).getAsJsonObject();
		String type = root.get("TYPE").getAsString();
		if(!type.equals(JSONIdentifier.BUDGETMASTER_DATABASE.toString()))
		{
			throw new IllegalArgumentException("JSON is not of type BUDGETMASTER_DATABASE");
		}

		int version = root.get("VERSION").getAsInt();
		if(version == 3)
		{
			return new Gson().fromJson(jsonString, Database.class);
		}

		return null;
	}

}