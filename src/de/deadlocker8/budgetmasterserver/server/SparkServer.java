package de.deadlocker8.budgetmasterserver.server;

import static spark.Spark.*;

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
import javafx.scene.paint.Color;
import logger.LogLevel;
import logger.Logger;
import spark.route.RouteOverview;

public class SparkServer
{
	private static Gson gson;

	public static void main(String[] args) throws URISyntaxException
	{		
		//DEBUG
		Logger.setLevel(LogLevel.ALL);
		
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
		//DEBUG
		secure("certs/keystore.jks", "geheim", null, null);	
		RouteOverview.enableRouteOverview();
		
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
		});
		
		post("/category", (req, res) -> {			
			if(!req.queryParams().contains("name") || !req.queryParams().contains("color"))
			{
				halt(400, "Bad Request");
			}	
							
			try
			{
				DatabaseHandler handler = new DatabaseHandler(settings);
				handler.addCategory(req.queryMap("name").value(), Color.web("#" + req.queryMap("color").value()));			

				return "";
			}
			catch(IllegalStateException ex)
			{				
				halt(500, "Internal Server Error");
			}
			catch(Exception e)
			{				
				halt(400, "Bad Request");
			}
			
			return "";
		});
		
		put("/category", (req, res) -> {			
			if(!req.queryParams().contains("id") ||!req.queryParams().contains("name") || !req.queryParams().contains("color"))
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
					DatabaseHandler handler = new DatabaseHandler(settings);
					handler.updateCategory(id, req.queryMap("name").value(), Color.web("#" + req.queryMap("color").value()));			

					return "";
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
			
			return "";
		});
				
		delete("/category", (req, res) -> {			
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
					DatabaseHandler handler = new DatabaseHandler(settings);			
					handler.deleteCategory(id);			

					return "";
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
			
			return "";
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
		
		post("/payment", (req, res) -> {			
			if(!req.queryParams().contains("amount") || !req.queryParams().contains("date") || !req.queryParams().contains("categoryID") || !req.queryParams().contains("name") || !req.queryParams().contains("repeatInterval") || !req.queryParams().contains("repeatEndDate") || !req.queryParams().contains("repeatMonthDay"))
			{				
				halt(400, "Bad Request");
			}	
				
			int amount = 0;
			int categoryID = 0;
			int repeatInterval = 0;
			int repeatMonthDay = 0;
			
			try
			{				
				amount = Integer.parseInt(req.queryMap("amount").value());
				categoryID = Integer.parseInt(req.queryMap("categoryID").value());
				repeatInterval = Integer.parseInt(req.queryMap("repeatInterval").value());
				repeatMonthDay = Integer.parseInt(req.queryMap("repeatMonthDay").value());
				
				try
				{
					DatabaseHandler handler = new DatabaseHandler(settings);
					handler.addPayment(amount, req.queryMap("date").value(), categoryID, req.queryMap("name").value(), repeatInterval, req.queryMap("repeatEndDate").value(), repeatMonthDay);			
	
					return "";
				}
				catch(IllegalStateException ex)
				{				
					halt(500, "Internal Server Error");
				}
			}
			catch(Exception e)
			{			
				e.printStackTrace();
				halt(400, "Bad Request");
			}
			
			return "";
		});

		// DEBUG
		RouteOverview.enableRouteOverview();
	}
}