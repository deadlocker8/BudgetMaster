package de.deadlocker8.budgetmasterserver.server.tag.tag;

import static spark.Spark.halt;

import java.util.ArrayList;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseTagHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class TagGetAll implements Route
{
	private DatabaseTagHandler tagHandler;
	private Gson gson;

	public TagGetAll(DatabaseTagHandler tagHandler, Gson gson)
	{
		this.tagHandler = tagHandler;
		this.gson = gson;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
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
}