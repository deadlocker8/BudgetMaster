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

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.deadlocker8.budgetmaster.logic.updater.VersionInformation;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmasterserver.logic.Settings;
import de.deadlocker8.budgetmasterserver.logic.Utils;
import de.deadlocker8.budgetmasterserver.logic.database.creator.DatabaseCreator;
import de.deadlocker8.budgetmasterserver.logic.database.handler.DatabaseHandler;
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
			else if(settings.getKeystorePath().equals("default"))
			{
				Logger.info("Connections are secured with default keystore");
				Logger.warning("The Server is running with the default keystore. This is only recommended if the server is running in a local area network and is not exposed to the internet. Please check if this is intended.");
				secure(SparkServer.class.getClassLoader().getResource("de/deadlocker8/budgetmasterserver/certificate/default_keystore.jks").toString(), settings.getKeystorePassword(), null, null);
			}
			else
			{
				Logger.info("Connections are secured with custom keystore");
				secure(new File(filePath).getAbsolutePath(), settings.getKeystorePassword(), null, null);
			}
		}
		catch(Exception e)
		{
			Logger.error(e);
			Logger.info("CANCELED server initialization");
			return;
		}		
		
		RouteOverview.enableRouteOverview();
		
		Logger.info("Trying to connect to database (jdbc:" + settings.getDatabaseType() + "://" + settings.getDatabaseUrl() + ", databaseName: " + settings.getDatabaseName() + ")");
		
		try
		{
			Connection connection = Utils.getDatabaseConnection(settings);
			DatabaseCreator creator = Utils.getDatabaseCreator(connection, settings);
			creator.createTables();
			Logger.info("Successfully initialized database (jdbc:" + settings.getDatabaseType() + "://" + settings.getDatabaseUrl() + ", databaseName: " + settings.getDatabaseName() + ")");
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

			DatabaseHandler handler = Utils.getDatabaseHandler(settings);
			RepeatingPaymentUpdater paymentUpdater = new RepeatingPaymentUpdater(handler);
			paymentUpdater.updateRepeatingPayments(DateTime.now());
			handler.closeConnection();
		});
		
		try
		{	
			// Category
			get("/category", new CategoryGetAll(Utils.getDatabaseHandler(settings), gson));
			get("/category/single", new CategoryGet(Utils.getDatabaseHandler(settings), gson));
			post("/category", new CategoryAdd(Utils.getDatabaseHandler(settings)));
			put("/category", new CategoryUpdate(Utils.getDatabaseHandler(settings)));
			delete("/category", new CategoryDelete(Utils.getDatabaseHandler(settings)));
	
			// Payment
			get("/payment/search", new PaymentSearch(Utils.getDatabaseHandler(settings), Utils.getDatabaseTagHandler(settings)));
			get("/payment/search/maxAmount", new PaymentMaxAmount(Utils.getDatabaseHandler(settings), gson));
			// Normal
			get("/payment", new PaymentGet(Utils.getDatabaseHandler(settings), gson));
			post("/payment", new PaymentAdd(Utils.getDatabaseHandler(settings), gson));
			put("/payment", new PaymentUpdate(Utils.getDatabaseHandler(settings)));
			delete("/payment", new PaymentDelete(Utils.getDatabaseHandler(settings), Utils.getDatabaseTagHandler(settings)));
	
			// Repeating
			get("/repeatingpayment/single", new RepeatingPaymentGet(Utils.getDatabaseHandler(settings), gson));
			get("/repeatingpayment", new RepeatingPaymentGetAll(Utils.getDatabaseHandler(settings), gson));
			post("/repeatingpayment", new RepeatingPaymentAdd(Utils.getDatabaseHandler(settings), gson));
			delete("/repeatingpayment", new RepeatingPaymentDelete(Utils.getDatabaseHandler(settings), Utils.getDatabaseTagHandler(settings)));
			
			// CategoryBudget
			get("/categorybudget", new CategoryBudgetGet(Utils.getDatabaseHandler(settings), gson));
			
			// Rest
			get("/rest", new RestGet(Utils.getDatabaseHandler(settings), gson));		
	
			// charts
			get("/charts/categoryInOutSum", new CategoryInOutSumForMonth(Utils.getDatabaseHandler(settings), gson));
			get("/charts/monthInOutSum", new MonthInOutSum(Utils.getDatabaseHandler(settings), gson));
			
			// tag
			get("/tag/single", new TagGet(Utils.getDatabaseTagHandler(settings), gson));
			get("/tag/single/byName", new TagGetByName(Utils.getDatabaseTagHandler(settings), gson));
			get("/tag", new TagGetAll(Utils.getDatabaseTagHandler(settings), gson));
			post("/tag", new TagAdd(Utils.getDatabaseTagHandler(settings)));
			delete("/tag", new TagDelete(Utils.getDatabaseTagHandler(settings)));
			
			// tag match
			get("/tag/match/all/normal", new TagMatchGetAllForPayment(Utils.getDatabaseTagHandler(settings), gson));
			get("/tag/match/all/repeating", new TagMatchGetAllForRepeatingPayment(Utils.getDatabaseTagHandler(settings), gson));
			get("/tag/match/normal", new TagMatchExistingForPayment(Utils.getDatabaseTagHandler(settings), gson));
			get("/tag/match/repeating", new TagMatchExistingForRepeatingPayment(Utils.getDatabaseTagHandler(settings), gson));
			post("/tag/match/normal", new TagMatchAddForPayment(Utils.getDatabaseTagHandler(settings)));
			post("/tag/match/repeating", new TagMatchAddForRepeatingPayment(Utils.getDatabaseTagHandler(settings)));
			delete("/tag/match/normal", new TagMatchDeleteForPayment(Utils.getDatabaseTagHandler(settings)));
			delete("/tag/match/repeating", new TagMatchDeleteForRepeatingPayment(Utils.getDatabaseTagHandler(settings)));
	
			// Database
			get("/database", new DatabaseExport(settings, gson));
			post("/database", new DatabaseImport(Utils.getDatabaseHandler(settings), Utils.getDatabaseTagHandler(settings), gson));
			delete("/database", new DatabaseDelete(Utils.getDatabaseHandler(settings), settings));
			
			get("/info", new InformationGet(gson, versionInfo, settings));
			get("/version", new VersionGet(gson, versionInfo));
			delete("/log", new LogDelete());
		}
		catch(ClassNotFoundException e)
		{
			Logger.error(e);
		}

		after((request, response) -> {
			DatabaseHandler handler = Utils.getDatabaseHandler(settings);
			RepeatingPaymentUpdater paymentUpdater = new RepeatingPaymentUpdater(handler);
			paymentUpdater.updateRepeatingPayments(DateTime.now());
			handler.closeConnection();
		});
		
		Spark.exception(Exception.class, (exception, request, response) -> {
			Logger.error(exception);
		});
	}
}