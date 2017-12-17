package de.deadlocker8.budgetmasterserver.server.tag.tag;

import static spark.Spark.halt;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.database.taghandler.DatabaseTagHandler;
import spark.Request;
import spark.Response;

public class TagGetByName implements AdvancedRoute
{
	private DatabaseTagHandler tagHandler;
	private Gson gson;

	public TagGetByName(DatabaseTagHandler tagHandler, Gson gson)
	{
		this.tagHandler = tagHandler;
		this.gson = gson;
	}

	@Override
	public void before()
	{
		tagHandler.connect();
	}

	@Override
	public Object handleRequest(Request req, Response res)
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

	@Override
	public void after()
	{
		tagHandler.closeConnection();		
	}
}