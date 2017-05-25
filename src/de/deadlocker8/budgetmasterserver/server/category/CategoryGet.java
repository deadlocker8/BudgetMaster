package de.deadlocker8.budgetmasterserver.server.category;

import static spark.Spark.halt;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmasterserver.logic.DatabaseHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class CategoryGet implements Route
{
	private DatabaseHandler handler;
	private Gson gson;

	public CategoryGet(DatabaseHandler handler, Gson gson)
	{
		this.handler = handler;
		this.gson = gson;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
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
}