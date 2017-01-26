package de.deadlocker8.budgetmasterserver.server;

import static spark.Spark.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmasterserver.main.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.main.Settings;
import spark.Spark;
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
		
		String settingsJSON;
		Settings settings;
		try
		{
			settingsJSON = new String(Files.readAllBytes(Paths.get("settings.properties")));				
			settings = gson.fromJson(settingsJSON, Settings.class);	
		}
		catch(IOException e)
		{
			//ERRORHANDLING
			e.printStackTrace();
			return;
		}
		
		port(settings.getPort());
		//TODO HTTPS
		//secure("", "", null, null);	
		
		before((request, response) -> {
			
			String clientSecret = request.queryMap("secret").value();
		
			if(clientSecret == null || !clientSecret.equals(settings.getSecret()))			
			{
				halt(401, "Unauthorized");
			}
		});

		get("/hello", (req, res) -> {

			DatabaseHandler handler = new DatabaseHandler();			
			ArrayList<Category> categories = handler.getCategories();
			System.out.println(req.queryMap("id").doubleValue());

			return gson.toJson(categories);
		});

		// DEBUG
		RouteOverview.enableRouteOverview();
	}
}