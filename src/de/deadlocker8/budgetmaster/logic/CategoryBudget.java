package de.deadlocker8.budgetmaster.logic;

import javafx.scene.paint.Color;

public class CategoryBudget
{
	private String name;
	private Color color;
	private double budget;
	
	public CategoryBudget(String name, Color color, double budget)
	{
		this.name = name;
		this.color = color;
		this.budget = budget;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
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
		return "CategoryBudget [name=" + name + ", color=" + color + ", budget=" + budget + "]";
	}
}