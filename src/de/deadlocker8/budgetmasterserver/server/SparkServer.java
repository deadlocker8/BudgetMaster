package de.deadlocker8.budgetmasterserver.server;

import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.secure;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.CategoryBudget;
import de.deadlocker8.budgetmaster.logic.LatestRepeatingPayment;
import de.deadlocker8.budgetmaster.logic.NormalPayment;
import de.deadlocker8.budgetmaster.logic.RepeatingPayment;
import de.deadlocker8.budgetmaster.logic.RepeatingPaymentEntry;
import de.deadlocker8.budgetmasterserver.main.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.main.Settings;
import de.deadlocker8.budgetmasterserver.main.Utils;
import javafx.scene.paint.Color;
import logger.LogLevel;
import logger.Logger;
import spark.Spark;
import spark.route.RouteOverview;

public class SparkServer
{
	private static Settings settings;
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
		
		settings = Utils.loadSettings();
		
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
			
			updateRepeatingPayments();			
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
		
		get("/category/single", (req, res) -> {			
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
					ArrayList<NormalPayment> payments = new ArrayList<>();				
					payments.addAll(handler.getPayments(year, month));	
					
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
		
		get("/repeatingpayment", (req, res) -> {					
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
					ArrayList<RepeatingPaymentEntry> payments = new ArrayList<>();				
					payments.addAll(handler.getRepeatingPayments(year, month));	
					
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
			if(!req.queryParams().contains("amount") || !req.queryParams().contains("date") || !req.queryParams().contains("categoryID") || !req.queryParams().contains("name"))
			{				
				halt(400, "Bad Request");
			}	
				
			int amount = 0;
			int categoryID = 0;			
			
			try
			{				
				amount = Integer.parseInt(req.queryMap("amount").value());
				categoryID = Integer.parseInt(req.queryMap("categoryID").value());				
				
				try
				{
					DatabaseHandler handler = new DatabaseHandler(settings);
					handler.addNormalPayment(amount, req.queryMap("date").value(), categoryID, req.queryMap("name").value());			
	
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
		
		post("/repeatingpayment", (req, res) -> {			
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
					handler.addRepeatingPayment(amount, req.queryMap("date").value(), categoryID, req.queryMap("name").value(), repeatInterval, req.queryMap("repeatEndDate").value(), repeatMonthDay);			
	
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
		
		delete("/payment", (req, res) -> {			
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
					handler.deletePayment(id);			

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
		
		Spark.exception(Exception.class, (exception, request, response)->{
			exception.printStackTrace();
		});
	}
	
	private static void updateRepeatingPayments()
	{
		try
		{
			DatabaseHandler handler = new DatabaseHandler(settings);			
			ArrayList<RepeatingPayment> repeatingPayments = handler.getAllRunningRepeatingPayments();				
			ArrayList<LatestRepeatingPayment> latest = handler.getLatestRepeatingPaymentEntries();;
			
			for(RepeatingPayment currentPayment : repeatingPayments)
			{
				int index = latest.indexOf(currentPayment);	
				DateTime now = DateTime.now();				
				ArrayList<DateTime> correctDates = getCorrectRepeatingDates(currentPayment, now);				
				if(index != -1)
				{
					LatestRepeatingPayment currentLatest = latest.get(index);					
					DateTime latestDate = DateTime.parse(currentLatest.getLastDate());	
					
					for(int i = correctDates.size()-1; i > 0; i--)
					{
						DateTime currentDate = correctDates.get(i);
						if(currentDate.isBefore(latestDate) || currentDate.isEqual(latestDate))
						{
							break;
						}
						
						handler.addRepeatingPaymentEntry(currentLatest.getRepeatingPaymentID(), currentDate.toString("yyyy-MM-dd"));
					}
				}
				else
				{
					for(DateTime currentDate : correctDates)
					{
						handler.addRepeatingPaymentEntry(currentPayment.getID(), currentDate.toString("yyyy-MM-dd"));
					}
				}
			}
		}
		catch(IllegalStateException ex)
		{
			Logger.log(LogLevel.ERROR, Logger.exceptionToString(ex));
		}
	}
	
	private static ArrayList<DateTime> getCorrectRepeatingDates(RepeatingPayment payment, DateTime now)
	{
		ArrayList<DateTime> dates = new ArrayList<>();
		DateTime startDate = DateTime.parse(payment.getDate());
		
		//repeat every x days
		if(payment.getRepeatInterval() != 0)
		{			
			int numberOfDays = Days.daysBetween(startDate, now).getDays();
			int occurrences = numberOfDays % payment.getRepeatInterval();
			for(int i = 0; i <= occurrences + 1; i++)
			{
				dates.add(startDate.plusDays(i * payment.getRepeatInterval()));
			}
		}
		//repeat every month on day x
		else
		{
			int numberOfMonths = Months.monthsBetween(startDate.withDayOfMonth(payment.getRepeatMonthDay()), now).getMonths();
			for(int i = 0; i <= numberOfMonths; i++)
			{
				dates.add(startDate.plusMonths(i));
			}				
		}
		
		return dates;
	}
}