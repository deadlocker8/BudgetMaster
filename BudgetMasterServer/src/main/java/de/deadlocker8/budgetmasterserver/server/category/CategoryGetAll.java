package de.deadlocker8.budgetmasterserver.server.category;

import static spark.Spark.halt;

import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseHandler;
import spark.Request;
import spark.Response;

public class CategoryGetAll implements AdvancedRoute
{
	private DatabaseHandler handler;
	private Gson gson;

	public CategoryGetAll(DatabaseHandler handler, Gson gson)
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
		try
		{
			ArrayList<Category> categories = handler.getCategories();
			Collections.sort(categories, (c1, c2) -> c1.getName().toLowerCase().compareTo(c2.getName().toLowerCase()));

			return gson.toJson(categories);
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
		handler.closeConnection();
	}
}