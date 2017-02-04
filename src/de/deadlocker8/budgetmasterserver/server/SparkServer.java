package de.deadlocker8.budgetmasterserver.server;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.port;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.deadlocker8.budgetmaster.logic.*;
import de.deadlocker8.budgetmasterserver.main.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.main.Settings;
import de.deadlocker8.budgetmasterserver.main.Utils;
import spark.route.RouteOverview;

public class SparkServer
{
	private static Gson gson;
	
	public static void main(String[] args) throws URISyntaxException
	{		
		gson = new GsonBuilder().setPrettyPrinting().create();
		
		if (!Files.exists(Paths.get("settings.properties")))
		{
			try
			{
				Files.copy(SparkServer.class.getClassLoader().getResourceAsStream("de/deadlocker8/budgetmasterserver/resources/settings.properties"), Paths.get("settings.properties"));
			}
			catch(IOException e)
			{
				//ERRORHANDLING
				e.printStackTrace();
			}		
		}
		
		Settings settings = Utils.loadSettings();
		
		port(settings.getServerPort());
		//TODO HTTPS
		//secure("", "", null, null);	
		
		before((request, response) -> {
			
			String clientSecret = request.queryMap("secret").value();
		
			if(clientSecret == null || !clientSecret.equals(settings.getServerSecret()))			
			{
				halt(401, "Unauthorized");
			}
		});

		/*
		 * Category
		 */
		get("/category", (req, res) -> {
			DatabaseHandler handler = new DatabaseHandler(settings);			
			ArrayList<Category> categories = handler.getCategories();			

			return gson.toJson(categories);
		});
		
		/*
		 * CategoryBudget
		 */
		get("/categorybudget", (req, res) -> {
			
			if(!req.queryParams().contains("year") || !req.queryParams().contains("month"))
			{
				halt(400, "Bad Request");
			}
			
			int year = 0;
			int month = 0;
			
			try
			{				
				year = Integer.parseInt(req.queryMap("year").value());
				month = Integer.parseInt(req.queryMap("month").value());
				
				if(year < 0 || month < 1 || month > 12)
				{
					halt(400, "Bad Request");
				}
				
				try
				{
					DatabaseHandler handler = new DatabaseHandler(settings);			
					ArrayList<CategoryBudget> categories = handler.getCategoryBudget(year, month);			

					return gson.toJson(categories);
				}
				catch(IllegalStateException ex)
				{
					halt(500, "Internal Server Error");
				}
			}
			catch(Exception e)
			{
				halt(400, "Bad Request");
			}
			
			return null;
		});
		
		/*
		 * Payment
		 */
		get("/payment", (req, res) -> {
			
			if(!req.queryParams().contains("year") || !req.queryParams().contains("month"))
			{
				halt(400, "Bad Request");
			}
			
			int year = 0;
			int month = 0;
			
			try
			{				
				year = Integer.parseInt(req.queryMap("year").value());
				month = Integer.parseInt(req.queryMap("month").value());
				
				if(year < 0 || month < 1 || month > 12)
				{
					halt(400, "Bad Request");
				}
				
				try
				{
					DatabaseHandler handler = new DatabaseHandler(settings);			
					ArrayList<Payment> payments = handler.getPayments(year, month);			

					return gson.toJson(payments);
				}
				catch(IllegalStateException ex)
				{
					halt(500, "Internal Server Error");
				}
			}
			catch(Exception e)
			{
				halt(400, "Bad Request");
			}
			
			return null;
		});

		// DEBUG
		RouteOverview.enableRouteOverview();
	}
}