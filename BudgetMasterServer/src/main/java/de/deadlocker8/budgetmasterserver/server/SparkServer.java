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
import java.sql.Connection;
import java.sql.DriverManager;

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.deadlocker8.budgetmaster.logic.updater.VersionInformation;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmasterserver.logic.Settings;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseCreator;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseTagHandler;
import de.deadlocker8.budgetmasterserver.server.category.CategoryAdd;
import de.deadlocker8.budgetmasterserver.server.category.CategoryDelete;
import de.deadlocker8.budgetmasterserver.server.category.CategoryGet;
import de.deadlocker8.budgetmasterserver.server.category.CategoryGetAll;
import de.deadlocker8.budgetmasterserver.server.category.CategoryUpdate;
import de.deadlocker8.budgetmasterserver.server.categorybudget.CategoryBudgetGet;
import de.deadlocker8.budgetmasterserver.server.charts.CategoryInOutSumForMonth;
import de.deadlocker8.budgetmasterserver.server.charts.MonthInOutSum;
import de.deadlocker8.budgetmasterserver.server.database.DatabaseDelete;
import de.deadlocker8.budgetmasterserver.server.database.DatabaseExport;
import de.deadlocker8.budgetmasterserver.server.database.DatabaseImport;
import de.deadlocker8.budgetmasterserver.server.info.InformationGet;
import de.deadlocker8.budgetmasterserver.server.info.VersionGet;
import de.deadlocker8.budgetmasterserver.server.log.LogDelete;
import de.deadlocker8.budgetmasterserver.server.payment.normal.PaymentAdd;
import de.deadlocker8.budgetmasterserver.server.payment.normal.PaymentDelete;
import de.deadlocker8.budgetmasterserver.server.payment.normal.PaymentGet;
import de.deadlocker8.budgetmasterserver.server.payment.normal.PaymentUpdate;
import de.deadlocker8.budgetmasterserver.server.payment.repeating.RepeatingPaymentAdd;
import de.deadlocker8.budgetmasterserver.server.payment.repeating.RepeatingPaymentDelete;
import de.deadlocker8.budgetmasterserver.server.payment.repeating.RepeatingPaymentGet;
import de.deadlocker8.budgetmasterserver.server.payment.repeating.RepeatingPaymentGetAll;
import de.deadlocker8.budgetmasterserver.server.payment.search.PaymentMaxAmount;
import de.deadlocker8.budgetmasterserver.server.payment.search.PaymentSearch;
import de.deadlocker8.budgetmasterserver.server.rest.RestGet;
import de.deadlocker8.budgetmasterserver.server.tag.match.TagMatchAddForPayment;
import de.deadlocker8.budgetmasterserver.server.tag.match.TagMatchAddForRepeatingPayment;
import de.deadlocker8.budgetmasterserver.server.tag.match.TagMatchDeleteForPayment;
import de.deadlocker8.budgetmasterserver.server.tag.match.TagMatchDeleteForRepeatingPayment;
import de.deadlocker8.budgetmasterserver.server.tag.match.TagMatchExistingForPayment;
import de.deadlocker8.budgetmasterserver.server.tag.match.TagMatchExistingForRepeatingPayment;
import de.deadlocker8.budgetmasterserver.server.tag.match.TagMatchGetAllForPayment;
import de.deadlocker8.budgetmasterserver.server.tag.match.TagMatchGetAllForRepeatingPayment;
import de.deadlocker8.budgetmasterserver.server.tag.tag.TagAdd;
import de.deadlocker8.budgetmasterserver.server.tag.tag.TagDelete;
import de.deadlocker8.budgetmasterserver.server.tag.tag.TagGet;
import de.deadlocker8.budgetmasterserver.server.tag.tag.TagGetAll;
import de.deadlocker8.budgetmasterserver.server.tag.tag.TagGetByName;
import de.deadlocker8.budgetmasterserver.server.updater.RepeatingPaymentUpdater;
import logger.Logger;
import spark.Spark;
import spark.route.RouteOverview;
import tools.HashUtils;

public class SparkServer
{	
	private Gson gson;
	
	public SparkServer(Settings settings, VersionInformation versionInfo)
	{
		Logger.info("Initializing SparkServer...");

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
		
		try
		{
			Connection connection = DriverManager.getConnection(settings.getDatabaseUrl() + settings.getDatabaseName() + "?useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin&autoReconnect=true&wait_timeout=86400", settings.getDatabaseUsername(), settings.getDatabasePassword());
			new DatabaseCreator(connection, settings);
			Logger.info("Successfully initialized database (" + settings.getDatabaseUrl() + settings.getDatabaseName() + ")");
			connection.close();
		}
		catch(Exception e)
		{
			Logger.error(e);
			throw new IllegalStateException("Cannot connect the database!", e);
		}
		
		before((request, response) -> {

			String clientSecret = request.queryMap("secret").value();

			if(clientSecret == null || !clientSecret.equals(HashUtils.hash(settings.getServerSecret(), Helpers.SALT)))
			{
				halt(401, "Unauthorized");
			}

			DatabaseHandler handler = new DatabaseHandler(settings);
			RepeatingPaymentUpdater paymentUpdater = new RepeatingPaymentUpdater(handler);
			paymentUpdater.updateRepeatingPayments(DateTime.now());
			handler.closeConnection();
		});

		// Category
		get("/category", new CategoryGetAll(new DatabaseHandler(settings), gson));
		get("/category/single", new CategoryGet(new DatabaseHandler(settings), gson));
		post("/category", new CategoryAdd(new DatabaseHandler(settings)));
		put("/category", new CategoryUpdate(new DatabaseHandler(settings)));
		delete("/category", new CategoryDelete(new DatabaseHandler(settings)));

		// Payment
		get("/payment/search", new PaymentSearch(new DatabaseHandler(settings), new DatabaseTagHandler(settings)));
		get("/payment/search/maxAmount", new PaymentMaxAmount(new DatabaseHandler(settings), gson));
		// Normal
		get("/payment", new PaymentGet(new DatabaseHandler(settings), gson));
		post("/payment", new PaymentAdd(new DatabaseHandler(settings), gson));
		put("/payment", new PaymentUpdate(new DatabaseHandler(settings)));
		delete("/payment", new PaymentDelete(new DatabaseHandler(settings), new DatabaseTagHandler(settings)));

		// Repeating
		get("/repeatingpayment/single", new RepeatingPaymentGet(new DatabaseHandler(settings), gson));
		get("/repeatingpayment", new RepeatingPaymentGetAll(new DatabaseHandler(settings), gson));
		post("/repeatingpayment", new RepeatingPaymentAdd(new DatabaseHandler(settings), gson));
		delete("/repeatingpayment", new RepeatingPaymentDelete(new DatabaseHandler(settings), new DatabaseTagHandler(settings)));
		
		// CategoryBudget
		get("/categorybudget", new CategoryBudgetGet(new DatabaseHandler(settings), gson));
		
		// Rest
		get("/rest", new RestGet(new DatabaseHandler(settings), gson));		

		// charts
		get("/charts/categoryInOutSum", new CategoryInOutSumForMonth(new DatabaseHandler(settings), gson));
		get("/charts/monthInOutSum", new MonthInOutSum(new DatabaseHandler(settings), gson));
		
		// tag
		get("/tag/single", new TagGet(new DatabaseTagHandler(settings), gson));
		get("/tag/single/byName", new TagGetByName(new DatabaseTagHandler(settings), gson));
		get("/tag", new TagGetAll(new DatabaseTagHandler(settings), gson));
		post("/tag", new TagAdd(new DatabaseTagHandler(settings)));
		delete("/tag", new TagDelete(new DatabaseTagHandler(settings)));
		
		// tag match
		get("/tag/match/all/normal", new TagMatchGetAllForPayment(new DatabaseTagHandler(settings), gson));
		get("/tag/match/all/repeating", new TagMatchGetAllForRepeatingPayment(new DatabaseTagHandler(settings), gson));
		get("/tag/match/normal", new TagMatchExistingForPayment(new DatabaseTagHandler(settings), gson));
		get("/tag/match/repeating", new TagMatchExistingForRepeatingPayment(new DatabaseTagHandler(settings), gson));
		post("/tag/match/normal", new TagMatchAddForPayment(new DatabaseTagHandler(settings)));
		post("/tag/match/repeating", new TagMatchAddForRepeatingPayment(new DatabaseTagHandler(settings)));
		delete("/tag/match/normal", new TagMatchDeleteForPayment(new DatabaseTagHandler(settings)));
		delete("/tag/match/repeating", new TagMatchDeleteForRepeatingPayment(new DatabaseTagHandler(settings)));

		// Database
		get("/database", new DatabaseExport(settings, gson));
		post("/database", new DatabaseImport(new DatabaseHandler(settings), new DatabaseTagHandler(settings), gson));
		delete("/database", new DatabaseDelete(new DatabaseHandler(settings), settings));
		
		get("/info", new InformationGet(gson, versionInfo, settings));
		get("/version", new VersionGet(gson, versionInfo));
		delete("/log", new LogDelete());

		after((request, response) -> {
			DatabaseHandler handler = new DatabaseHandler(settings);
			RepeatingPaymentUpdater paymentUpdater = new RepeatingPaymentUpdater(handler);
			paymentUpdater.updateRepeatingPayments(DateTime.now());
			handler.closeConnection();
		});
		
		Spark.exception(Exception.class, (exception, request, response) -> {
			Logger.error(exception);
		});
	}
}