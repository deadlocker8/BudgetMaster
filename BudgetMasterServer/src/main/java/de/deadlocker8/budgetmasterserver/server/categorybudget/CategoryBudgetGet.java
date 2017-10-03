package de.deadlocker8.budgetmasterserver.server.categorybudget;

import static spark.Spark.halt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmaster.logic.category.CategoryBudget;
import de.deadlocker8.budgetmaster.logic.payment.Payment;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class CategoryBudgetGet implements Route
{
	private DatabaseHandler handler;
	private Gson gson;

	public CategoryBudgetGet(DatabaseHandler handler, Gson gson)
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
			
				ArrayList<CategoryBudget> budgets = new ArrayList<>();
				
				for(Category currentCategory : handler.getCategories())
				{
					budgets.add(new CategoryBudget(currentCategory, 0));
					CategoryBudget currentBudget = budgets.get(budgets.size() - 1);
					for(Payment currentPayment : payments)
					{					
						if(currentCategory.getID() == currentPayment.getCategoryID())
						{
							currentBudget.setBudget(currentBudget.getBudget() + currentPayment.getAmount());
						}						
					}
				}
				
				//filter empty categories
				Iterator<CategoryBudget> iterator = budgets.iterator();
				while(iterator.hasNext())
				{		
					if(iterator.next().getBudget() == 0)
					{
						iterator.remove();
					}
				}
				
				Collections.sort(budgets, new Comparator<CategoryBudget>() {
			        @Override
			        public int compare(CategoryBudget budget1, CategoryBudget budget2)
			        {
			            return  Double.compare(budget1.getBudget(), budget2.getBudget());
			        }
			    });		
				
				return gson.toJson(budgets);
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