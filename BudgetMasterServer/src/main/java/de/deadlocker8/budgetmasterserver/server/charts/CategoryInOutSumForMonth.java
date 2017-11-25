package de.deadlocker8.budgetmasterserver.server.charts;

import static spark.Spark.halt;

import java.util.ArrayList;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmaster.logic.charts.CategoryInOutSum;
import de.deadlocker8.budgetmaster.logic.payment.Payment;
import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.database.handler.DatabaseHandler;
import spark.Request;
import spark.Response;

public class CategoryInOutSumForMonth implements AdvancedRoute
{
	private DatabaseHandler handler;
	private Gson gson;

	public CategoryInOutSumForMonth(DatabaseHandler handler, Gson gson)
	{
		this.handler = handler;
		this.gson = gson;
	}

	@Override
	public void before()
	{
		handler.connect();
	}

	@Override
	public Object handleRequest(Request req, Response res)
	{
		if(!req.queryParams().contains("startDate") || !req.queryParams().contains("endDate"))
		{
			halt(400, "Bad Request");
		}		
	
		try
		{	
			ArrayList<Payment> payments = new ArrayList<>();
			payments.addAll(handler.getPaymentsBetween(req.queryMap("startDate").value(), req.queryMap("endDate").value()));
			payments.addAll(handler.getRepeatingPaymentsBetween(req.queryMap("startDate").value(), req.queryMap("endDate").value()));	
			
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
		
		return null;
	}

	@Override
	public void after()
	{
		handler.closeConnection();
	}
}