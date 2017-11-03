package de.deadlocker8.budgetmasterserver.server.tag.tag;

import static spark.Spark.halt;

import java.util.ArrayList;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseTagHandler;
import spark.Request;
import spark.Response;

public class TagGetAll implements AdvancedRoute
{
	private DatabaseTagHandler tagHandler;
	private Gson gson;

	public TagGetAll(DatabaseTagHandler tagHandler, Gson gson)
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
		try
		{	
			ArrayList<Tag> tags = tagHandler.getAllTags();
			
			return gson.toJson(tags);
		}
		catch(IllegalStateException e)
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