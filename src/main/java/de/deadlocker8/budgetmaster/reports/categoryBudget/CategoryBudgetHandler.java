package de.deadlocker8.budgetmaster.reports.categoryBudget;

import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.categories.Category;

import java.util.*;

public class CategoryBudgetHandler
{
	private CategoryBudgetHandler()
	{
	}

	public static List<CategoryBudget> getCategoryBudgets(List<Transaction> transactions, List<Category> categories)
	{
		List<CategoryBudget> budgets = new ArrayList<>();

		for(Category currentCategory : categories)
		{
			budgets.add(new CategoryBudget(currentCategory, 0));
			CategoryBudget currentBudget = budgets.get(budgets.size() - 1);
			for(Transaction currentTransaction : transactions)
			{
				final Category category = currentTransaction.getCategory();
				if(category != null)
				{
					if(currentCategory.getID().equals(category.getID()))
					{
						currentBudget.setBudget(currentBudget.getBudget() + currentTransaction.getAmount());
					}
				}
			}
		}

		budgets.removeIf(categoryBudget -> categoryBudget.getBudget() == 0);
		budgets.sort(Comparator.comparingDouble(CategoryBudget::getBudget));

		return budgets;
	}
}
