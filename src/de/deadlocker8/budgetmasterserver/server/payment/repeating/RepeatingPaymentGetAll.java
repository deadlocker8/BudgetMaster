package de.deadlocker8.budgetmasterserver.server.payment.repeating;

import static spark.Spark.halt;

import java.util.ArrayList;

import org.joda.time.DateTime;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.RepeatingPaymentEntry;
import de.deadlocker8.budgetmasterserver.main.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.server.updater.RepeatingPaymentUpdater;
import spark.Request;
import spark.Response;
import spark.Route;

public class RepeatingPaymentGetAll implements Route
{
	private DatabaseHandler handler;
	private Gson gson;

	public RepeatingPaymentGetAll(DatabaseHandler handler, Gson gson)
	{		
		this.handler = handler;
		this.gson = gson;
	}
	
	@Override
	public Object handle(Request req, Response res) throws Exception
	{
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
			
			//refresh repeating entries
			DateTime date = DateTime.now().withYear(year).withMonthOfYear(month);
			date = date.dayOfMonth().withMaximumValue();
			if(date.isBefore(DateTime.now()))
			{
				date = DateTime.now().dayOfMonth().withMaximumValue();
			}
			new RepeatingPaymentUpdater(handler).updateRepeatingPayments(date);
			
			try
			{					
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
	}
}