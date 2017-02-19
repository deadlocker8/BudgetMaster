package de.deadlocker8.budgetmasterserver.server.category;

import static spark.Spark.halt;

import java.util.ArrayList;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmasterserver.main.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.main.Settings;
import spark.Request;
import spark.Response;
import spark.Route;

public class CategoryGetAll implements Route
{
	private Settings settings;
	private Gson gson;

	public CategoryGetAll(Settings settings, Gson gson)
	{
		this.settings = settings;
		this.gson = gson;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
	{
		try
		{
			DatabaseHandler handler = new DatabaseHandler(settings);			
			ArrayList<Category> categories = handler.getCategories();			

			return gson.toJson(categories);
		}
		catch(IllegalStateException e)
		{
			halt(500, "Internal Server Error");
		}	
		return null;
	}
}