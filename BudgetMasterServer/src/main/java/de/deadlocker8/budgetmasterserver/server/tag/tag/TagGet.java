package de.deadlocker8.budgetmasterserver.server.tag.tag;

import static spark.Spark.halt;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseTagHandler;
import spark.Request;
import spark.Response;

public class TagGet implements AdvancedRoute
{
	private DatabaseTagHandler tagHandler;
	private Gson gson;

	public TagGet(DatabaseTagHandler tagHandler, Gson gson)
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
		if(!req.queryParams().contains("id"))
		{
			halt(400, "Bad Request");
		}

		int id = -1;

		try
		{
			id = Integer.parseInt(req.queryMap("id").value());

			if(id < 0)
			{
				halt(400, "Bad Request");
			}

			try
			{
				Tag tag = tagHandler.getTagByID(id);

				return gson.toJson(tag);
			}
			catch(IllegalStateException e)
			{				
				halt(500, "Internal Server Error");
			}
		}
		catch(Exception e)
		{
			halt(400, "Bad Request");
		}
		return null;
	}

	@Override
	public void after()
	{
		tagHandler.closeConnection();		
	}
}