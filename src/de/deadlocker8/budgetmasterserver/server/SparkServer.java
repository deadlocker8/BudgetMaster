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

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.deadlocker8.budgetmasterserver.main.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.main.Settings;
import de.deadlocker8.budgetmasterserver.server.category.CategoryAdd;
import de.deadlocker8.budgetmasterserver.server.category.CategoryDelete;
import de.deadlocker8.budgetmasterserver.server.category.CategoryGet;
import de.deadlocker8.budgetmasterserver.server.category.CategoryGetAll;
import de.deadlocker8.budgetmasterserver.server.category.CategoryUpdate;
import de.deadlocker8.budgetmasterserver.server.categorybudget.CategoryBudgetGet;
import de.deadlocker8.budgetmasterserver.server.database.DatabaseDelete;
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
import logger.Logger;
import spark.Spark;
import spark.route.RouteOverview;

public class SparkServer
{	
	private Gson gson;
	private DatabaseHandler handler;
	
	public SparkServer(Settings settings)
	{				
		Logger.info("Initialized SparkServer");

		gson = new GsonBuilder().setPrettyPrinting().create();
		
		port(settings.getServerPort());
		
		try
		{
			String filePath = settings.getKeystorePath();
			if(filePath.equals(""))
			{
				throw new Exception("empty string is no valid keystorePath");
			}
			File keystoreFile = new File(filePath);		
			secure(keystoreFile.getAbsolutePath(), settings.getKeystorePassword(), null, null);						
		}
		catch(Exception e)
		{
			Logger.error(e);
			Logger.info("CANCELED server initialization");
			return;
		}		
		
		RouteOverview.enableRouteOverview();
		
		handler = new DatabaseHandler(settings);

		before((request, response) -> {

			String clientSecret = request.queryMap("secret").value();

			if(clientSecret == null || !clientSecret.equals(settings.getServerSecret()))
			{
				halt(401, "Unauthorized");
			}

			new RepeatingPaymentUpdater(handler).updateRepeatingPayments(DateTime.now());
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
		
		// CategoryBudget
		get("/categorybudget", new CategoryBudgetGet(handler, gson));
		
		// Rest
		get("/rest", new RestGet(handler, gson));
		
		// Database
		delete("/database", new DatabaseDelete(handler, settings));

		after((request, response) -> {
			new RepeatingPaymentUpdater(handler).updateRepeatingPayments(DateTime.now());
		});
		
		Spark.exception(Exception.class, (exception, request, response) -> {
			Logger.error(exception);
		});
	}
}