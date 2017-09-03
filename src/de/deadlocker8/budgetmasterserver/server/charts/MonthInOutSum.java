package de.deadlocker8.budgetmasterserver.server.charts;

import static spark.Spark.halt;

import java.util.ArrayList;

import org.joda.time.DateTime;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmaster.logic.charts.CategoryInOutSum;
import de.deadlocker8.budgetmaster.logic.payment.Payment;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseHandler;
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
			
			ArrayList<de.deadlocker8.budgetmaster.logic.charts.MonthInOutSum> monthInOutSums = new ArrayList<>();
			
			while(startDate.isBefore(endDate) || startDate.isEqual(endDate))
			{
				ArrayList<Payment> currentMonthPayments = new ArrayList<>();
				currentMonthPayments.addAll(handler.getPayments(startDate.getYear(), startDate.getMonthOfYear()));
				currentMonthPayments.addAll(handler.getRepeatingPayments(startDate.getYear(), startDate.getMonthOfYear()));		
				
				ArrayList<CategoryInOutSum> sums = new ArrayList<>();
				
				for(Category currentCategory : handler.getCategories())
				{					
					sums.add(new CategoryInOutSum(currentCategory.getID(), currentCategory.getName(), currentCategory.getColor(), 0, 0));
					CategoryInOutSum currentInOutSum = sums.get(sums.size() - 1);
					for(Payment currentPayment : currentMonthPayments)
					{					
						if(currentCategory.getID() == currentPayment.getCategoryID())
						{
							int amount = currentPayment.getAmount();
							if(amount > 0)
							{
								currentInOutSum.setBudgetIN(currentInOutSum.getBudgetIN() + amount);
							}
							else
							{
								currentInOutSum.setBudgetOUT(currentInOutSum.getBudgetOUT() + amount);
							}
						}						
					}
				}
				
				monthInOutSums.add(new de.deadlocker8.budgetmaster.logic.charts.MonthInOutSum(startDate.getMonthOfYear(), startDate.getYear(), sums));				
				
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