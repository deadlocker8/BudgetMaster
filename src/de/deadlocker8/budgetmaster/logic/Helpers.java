package de.deadlocker8.budgetmaster.logic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class Helpers
{
	public static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.00");
	
	public static String getURLEncodedString(String input)
	{
		try
		{
			return URLEncoder.encode(input, "UTF-8");
		}
		catch(UnsupportedEncodingException e)
		{
			return input;
		}
	}
	
	public static String getDateString(LocalDate date)
	{
		if(date == null)
		{
			return "";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return date.format(formatter);
	}
	
	public static ArrayList<CategoryBudget> getCategoryBudgets(CategoryHandler categoryHandler, ArrayList<Payment> payments)
	{
		ArrayList<CategoryBudget> budgets = new ArrayList<>();
		
		for(Category currentCategory : categoryHandler.getCategories())
		{
			budgets.add(new CategoryBudget(currentCategory.getName(), currentCategory.getColor(), 0));
			CategoryBudget currentBudget = budgets.get(budgets.size() - 1);
			for(Payment currentPayment : payments)
			{
				//filter rest
				if(currentCategory.getID() != 2)
				{
					if(currentCategory.getID() == currentPayment.getCategoryID())
					{
						currentBudget.setBudget(currentBudget.getBudget() + currentPayment.getAmount());
					}
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
		
		return budgets;		
	}
}