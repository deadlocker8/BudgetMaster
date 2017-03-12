package de.deadlocker8.budgetmasterserver.server;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.secure;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.deadlocker8.budgetmasterserver.main.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.main.Settings;
import de.deadlocker8.budgetmasterserver.main.Utils;
import de.deadlocker8.budgetmasterserver.server.category.CategoryAdd;
import de.deadlocker8.budgetmasterserver.server.category.CategoryDelete;
import de.deadlocker8.budgetmasterserver.server.category.CategoryGet;
import de.deadlocker8.budgetmasterserver.server.category.CategoryGetAll;
import de.deadlocker8.budgetmasterserver.server.category.CategoryUpdate;
import de.deadlocker8.budgetmasterserver.server.payment.normal.PaymentAdd;
import de.deadlocker8.budgetmasterserver.server.payment.normal.PaymentDelete;
import de.deadlocker8.budgetmasterserver.server.payment.normal.PaymentGet;
import de.deadlocker8.budgetmasterserver.server.payment.normal.PaymentUpdate;
import de.deadlocker8.budgetmasterserver.server.payment.repeating.RepeatingPaymentAdd;
import de.deadlocker8.budgetmasterserver.server.payment.repeating.RepeatingPaymentDelete;
import de.deadlocker8.budgetmasterserver.server.payment.repeating.RepeatingPaymentGet;
import de.deadlocker8.budgetmasterserver.server.payment.repeating.RepeatingPaymentGetAll;
import de.deadlocker8.budgetmasterserver.server.rest.RestGet;
import de.deadlocker8.budgetmasterserver.server.updater.RepeatingPaymentUpdater;
import logger.LogLevel;
import logger.Logger;
import spark.Spark;
import spark.route.RouteOverview;

public class SparkServer
{
	private static Settings settings;
	private static Gson gson;
	private static DatabaseHandler handler;

	// DEBUG move main method to extra file and start SparkServer from there
	public static void main(String[] args) throws URISyntaxException
	{		
		Logger.setLevel(LogLevel.ALL);
		File logFile = new File(Paths.get(SparkServer.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().toFile() + "/error.log");	
		Logger.enableFileOutput(logFile);
		Logger.info("Initialized SparkServer");

		gson = new GsonBuilder().setPrettyPrinting().create();

		if(!Files.exists(Paths.get("settings.properties")))
		{
			try
			{
				Files.copy(SparkServer.class.getClassLoader().getResourceAsStream("de/deadlocker8/budgetmasterserver/resources/settings.properties"), Paths.get("settings.properties"));
			}
			catch(IOException e)
			{
				// ERRORHANDLING
				e.printStackTrace();
			}
		}

		settings = Utils.loadSettings();

		port(settings.getServerPort());
		// DEBUG
		secure("certs/keystore.jks", "geheim", null, null);
		RouteOverview.enableRouteOverview();
		
		handler = new DatabaseHandler(settings);

		before((request, response) -> {

			String clientSecret = request.queryMap("secret").value();

			if(clientSecret == null || !clientSecret.equals(settings.getServerSecret()))
			{
				halt(401, "Unauthorized");
			}

			new RepeatingPaymentUpdater(handler).updateRepeatingPayments();
		});

		// Category
		get("/category", new CategoryGetAll(handler, gson));
		get("/category/single", new CategoryGet(handler, gson));
		post("/category", new CategoryAdd(handler));
		put("/category", new CategoryUpdate(handler));
		delete("/category", new CategoryDelete(handler));

		// Payment
		// Normal
		get("/payment", new PaymentGet(handler, gson));
		post("/payment", new PaymentAdd(handler));
		put("/payment", new PaymentUpdate(handler));
		delete("/payment", new PaymentDelete(handler));

		// Repeating
		get("/repeatingpayment/single", new RepeatingPaymentGet(handler, gson));
		get("/repeatingpayment", new RepeatingPaymentGetAll(handler, gson));
		post("/repeatingpayment", new RepeatingPaymentAdd(handler));
		delete("/repeatingpayment", new RepeatingPaymentDelete(handler));
		
		// Rest
		get("/rest", new RestGet(handler, gson));

		after((request, response) -> {
			new RepeatingPaymentUpdater(handler).updateRepeatingPayments();
		});
		
		Spark.exception(Exception.class, (exception, request, response) -> {
			exception.printStackTrace();
		});
	}
}