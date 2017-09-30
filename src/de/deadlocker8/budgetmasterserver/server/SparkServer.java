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

import de.deadlocker8.budgetmaster.logic.updater.VersionInformation;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmasterserver.logic.Settings;
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
import de.deadlocker8.budgetmasterserver.server.version.VersionGet;
import logger.Logger;
import spark.Spark;
import spark.route.RouteOverview;
import tools.HashUtils;

public class SparkServer
{	
	private Gson gson;
	private DatabaseHandler handler;
	private DatabaseTagHandler tagHandler;
	
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
		
		handler = new DatabaseHandler(settings);
		tagHandler = new DatabaseTagHandler(settings);

		before((request, response) -> {

			String clientSecret = request.queryMap("secret").value();

			if(clientSecret == null || !clientSecret.equals(HashUtils.hash(settings.getServerSecret(), Helpers.SALT)))
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
		get("/payment/search", new PaymentSearch(handler, tagHandler));
		get("/payment/search/maxAmount", new PaymentMaxAmount(handler, gson));
		// Normal
		get("/payment", new PaymentGet(handler, gson));
		post("/payment", new PaymentAdd(handler, gson));
		put("/payment", new PaymentUpdate(handler));
		delete("/payment", new PaymentDelete(handler, tagHandler));

		// Repeating
		get("/repeatingpayment/single", new RepeatingPaymentGet(handler, gson));
		get("/repeatingpayment", new RepeatingPaymentGetAll(handler, gson));
		post("/repeatingpayment", new RepeatingPaymentAdd(handler, gson));
		delete("/repeatingpayment", new RepeatingPaymentDelete(handler, tagHandler));
		
		// CategoryBudget
		get("/categorybudget", new CategoryBudgetGet(handler, gson));
		
		// Rest
		get("/rest", new RestGet(handler, gson));		

		// charts
		get("/charts/categoryInOutSum", new CategoryInOutSumForMonth(handler, gson));
		get("/charts/monthInOutSum", new MonthInOutSum(handler, gson));
		
		// tag
		get("/tag/single", new TagGet(tagHandler, gson));
		get("/tag/single/byName", new TagGetByName(tagHandler, gson));
		get("/tag", new TagGetAll(tagHandler, gson));
		post("/tag", new TagAdd(tagHandler));
		delete("/tag", new TagDelete(tagHandler));
		
		// tag match
		get("/tag/match/all/normal", new TagMatchGetAllForPayment(tagHandler, gson));
		get("/tag/match/all/repeating", new TagMatchGetAllForRepeatingPayment(tagHandler, gson));
		get("/tag/match/normal", new TagMatchExistingForPayment(tagHandler, gson));
		get("/tag/match/repeating", new TagMatchExistingForRepeatingPayment(tagHandler, gson));
		post("/tag/match/normal", new TagMatchAddForPayment(tagHandler));
		post("/tag/match/repeating", new TagMatchAddForRepeatingPayment(tagHandler));
		delete("/tag/match/normal", new TagMatchDeleteForPayment(tagHandler));
		delete("/tag/match/repeating", new TagMatchDeleteForRepeatingPayment(tagHandler));

		// Database
		get("/database", new DatabaseExport(settings, gson));
		post("/database", new DatabaseImport(handler, tagHandler, gson));
		delete("/database", new DatabaseDelete(handler, settings));
		
		get("/version", new VersionGet(gson, versionInfo));

		after((request, response) -> {
			new RepeatingPaymentUpdater(handler).updateRepeatingPayments(DateTime.now());
		});
		
		Spark.exception(Exception.class, (exception, request, response) -> {
			Logger.error(exception);
		});
	}
}