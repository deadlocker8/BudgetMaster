package de.deadlocker8.budgetmasterserver.server.category;

import static spark.Spark.halt;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseHandler;
import spark.Request;
import spark.Response;

public class CategoryGet implements AdvancedRoute
{
	private DatabaseHandler handler;
	private Gson gson;

	public CategoryGet(DatabaseHandler handler, Gson gson)
	{
		this.handler = handler;
		this.gson = gson;
	}

	@Override
	public void before()
	{
		handler.connect();
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
				Category categeory = handler.getCategory(id);

				return gson.toJson(categeory);
			}
			catch(IllegalStateException e)
			{
				e.printStackTrace();
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
		handler.closeConnection();
	}
}