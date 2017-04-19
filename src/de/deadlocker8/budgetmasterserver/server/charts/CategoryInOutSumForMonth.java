package de.deadlocker8.budgetmasterserver.server.charts;

import static spark.Spark.halt;

import java.util.ArrayList;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.CategoryInOutSum;
import de.deadlocker8.budgetmaster.logic.Payment;
import de.deadlocker8.budgetmasterserver.main.DatabaseHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class CategoryInOutSumForMonth implements Route
{
	private DatabaseHandler handler;
	private Gson gson;

	public CategoryInOutSumForMonth(DatabaseHandler handler, Gson gson)
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
			
			try
			{	
				ArrayList<Payment> payments = new ArrayList<>();
				payments.addAll(handler.getPayments(year, month));
				payments.addAll(handler.getRepeatingPayments(year, month));			
			
				ArrayList<CategoryInOutSum> inOutSums = new ArrayList<>();
				
				for(Category currentCategory : handler.getCategories())
				{					
					inOutSums.add(new CategoryInOutSum(currentCategory.getID(), currentCategory.getName(), currentCategory.getColor(), 0, 0));
					CategoryInOutSum currentInOutSum = inOutSums.get(inOutSums.size() - 1);
					for(Payment currentPayment : payments)
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
				
				return gson.toJson(inOutSums);
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