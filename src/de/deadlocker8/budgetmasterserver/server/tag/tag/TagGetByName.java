package de.deadlocker8.budgetmasterserver.server.tag.tag;

import static spark.Spark.halt;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseTagHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class TagGetByName implements Route
{
	private DatabaseTagHandler tagHandler;
	private Gson gson;

	public TagGetByName(DatabaseTagHandler tagHandler, Gson gson)
	{
		this.tagHandler = tagHandler;
		this.gson = gson;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
	{
		if(!req.queryParams().contains("name"))
		{
			halt(400, "Bad Request");
		}

		try
		{
			Tag tag = tagHandler.getTagByName(req.queryMap("name").value());
			return gson.toJson(tag);
		}
		catch(Exception e)
		{
			halt(500, "Internal Server Error");
		}
	
		return null;
	}
}