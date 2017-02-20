package de.deadlocker8.budgetmasterserver.server;

import static spark.Spark.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.deadlocker8.budgetmasterserver.main.Settings;
import de.deadlocker8.budgetmasterserver.main.Utils;
import de.deadlocker8.budgetmasterserver.server.category.CategoryAdd;
import de.deadlocker8.budgetmasterserver.server.category.CategoryDelete;
import de.deadlocker8.budgetmasterserver.server.category.CategoryGet;
import de.deadlocker8.budgetmasterserver.server.category.CategoryGetAll;
import de.deadlocker8.budgetmasterserver.server.category.CategoryUpdate;
import de.deadlocker8.budgetmasterserver.server.categorybudget.CategoryBudgetGet;
import de.deadlocker8.budgetmasterserver.server.payment.normal.PaymentAdd;
import de.deadlocker8.budgetmasterserver.server.payment.normal.PaymentDelete;
import de.deadlocker8.budgetmasterserver.server.payment.normal.PaymentGet;
import de.deadlocker8.budgetmasterserver.server.payment.normal.PaymentUpdate;
import de.deadlocker8.budgetmasterserver.server.payment.repeating.RepeatingPaymentAdd;
import de.deadlocker8.budgetmasterserver.server.payment.repeating.RepeatingPaymentDelete;
import de.deadlocker8.budgetmasterserver.server.payment.repeating.RepeatingPaymentGet;
import de.deadlocker8.budgetmasterserver.server.payment.repeating.RepeatingPaymentGetAll;
import de.deadlocker8.budgetmasterserver.server.updater.RepeatingPaymentUpdater;
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
		// DEBUG
		Logger.setLevel(LogLevel.ALL);

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

		before((request, response) -> {

			String clientSecret = request.queryMap("secret").value();

			if(clientSecret == null || !clientSecret.equals(settings.getServerSecret()))
			{
				halt(401, "Unauthorized");
			}

			new RepeatingPaymentUpdater(settings).updateRepeatingPayments();
		});

		// Category
		get("/category", new CategoryGetAll(settings, gson));
		get("/category/single", new CategoryGet(settings, gson));
		post("/category", new CategoryAdd(settings));
		put("/category", new CategoryUpdate(settings));
		delete("/category", new CategoryDelete(settings));

		// CategoryBudget
		get("/categorybudget", new CategoryBudgetGet(settings, gson));

		// Payment
		// Normal
		get("/payment", new PaymentGet(settings, gson));
		post("/payment", new PaymentAdd(settings));
		put("/payment", new PaymentUpdate(settings));
		delete("/payment", new PaymentDelete(settings));

		// Repeating
		get("/repeatingpayment/single", new RepeatingPaymentGet(settings, gson));
		get("/repeatingpayment", new RepeatingPaymentGetAll(settings, gson));
		post("/repeatingpayment", new RepeatingPaymentAdd(settings));
		delete("/repeatingpayment", new RepeatingPaymentDelete(settings));

		after((request, response) -> {
			new RepeatingPaymentUpdater(settings).updateRepeatingPayments();
		});
		
		Spark.exception(Exception.class, (exception, request, response) -> {
			exception.printStackTrace();
		});
	}
}