package de.deadlocker8.budgetmaster.reports.categoryBudget;

import de.deadlocker8.budgetmaster.entities.category.Category;

public class CategoryBudget
{
	private Category category;
	private double budget;

	public CategoryBudget(Category category, double budget)
	{
		this.category = category;
		this.budget = budget;
	}

	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

	public double getBudget()
	{
		return budget;
	}

	public void setBudget(double budget)
	{
		this.budget = budget;
	}

	@Override
	public String toString()
	{
		return "CategoryBudget [category=" + category + ", budget=" + budget + "]";
	}
}