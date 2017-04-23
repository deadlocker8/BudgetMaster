package de.deadlocker8.budgetmasterserver.server.charts;

import static spark.Spark.halt;

import java.util.ArrayList;

import org.joda.time.DateTime;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.Payment;
import de.deadlocker8.budgetmasterserver.main.DatabaseHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class MonthInOutSum implements Route
{
	private DatabaseHandler handler;
	private Gson gson;

	public MonthInOutSum(DatabaseHandler handler, Gson gson)
	{
		this.handler = handler;
		this.gson = gson;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
	{
		if(!req.queryParams().contains("startDate") || !req.queryParams().contains("endDate"))
		{
			halt(400, "Bad Request");
		}		
	
		try
		{				
			DateTime startDate = DateTime.parse(req.queryMap("startDate").value()).withDayOfMonth(1);
			DateTime endDate = DateTime.parse(req.queryMap("endDate").value()).withDayOfMonth(1);		
			
			ArrayList<de.deadlocker8.budgetmaster.logic.MonthInOutSum> monthInOutSums = new ArrayList<>();
			
			while(startDate.isBefore(endDate) || startDate.isEqual(endDate))
			{
				ArrayList<Payment> currentMonthPayments = new ArrayList<>();
				currentMonthPayments.addAll(handler.getPayments(startDate.getYear(), startDate.getMonthOfYear()));
				currentMonthPayments.addAll(handler.getRepeatingPayments(startDate.getYear(), startDate.getMonthOfYear()));				
				
				int sumIN = 0;
				int sumOUT = 0;						
						
				for(Payment currentPayment : currentMonthPayments)
				{
					if(currentPayment.getAmount() > 0)
					{
						sumIN += currentPayment.getAmount();
					}
					else
					{
						sumOUT += -currentPayment.getAmount();
					}
				}
				
				monthInOutSums.add(new de.deadlocker8.budgetmaster.logic.MonthInOutSum(startDate.getMonthOfYear(), startDate.getYear(), sumIN, sumOUT));				
				
				startDate = startDate.plusMonths(1);
			}	
		
			return gson.toJson(monthInOutSums);
		}
		catch(IllegalStateException ex)
		{
			halt(500, "Internal Server Error");
		}		
		
		return null;
	}
}