package de.deadlocker8.budgetmaster.reports.categoryBudget;

import de.deadlocker8.budgetmaster.entities.Transaction;
import de.deadlocker8.budgetmaster.entities.category.Category;

import java.util.*;

public class CategoryBudgetHandler
{
	public static List<CategoryBudget> getCategoryBudgets(List<Transaction> transactions, List<Category> categories)
	{
		List<CategoryBudget> budgets = new ArrayList<>();

		for(Category currentCategory : categories)
		{
			budgets.add(new CategoryBudget(currentCategory, 0));
			CategoryBudget currentBudget = budgets.get(budgets.size() - 1);
			for(Transaction currentPayment : transactions)
			{
				if(currentCategory.getID().equals(currentPayment.getCategory().getID()))
				{
					currentBudget.setBudget(currentBudget.getBudget() + currentPayment.getAmount());
				}
			}
		}

		budgets.removeIf(categoryBudget -> categoryBudget.getBudget() == 0);
		budgets.sort(Comparator.comparingDouble(CategoryBudget::getBudget));

		return budgets;
	}
}
