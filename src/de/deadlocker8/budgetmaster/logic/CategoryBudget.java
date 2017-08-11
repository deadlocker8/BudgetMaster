package de.deadlocker8.budgetmaster.logic;

import de.deadlocker8.budgetmaster.logic.utils.Strings;
import javafx.scene.paint.Color;
import tools.Localization;

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
	    //TODO this is not safe! --> if user wishes to name a category "NONE" --> use ID to identify NONE-category instead
	    if(name != null && name.equals("NONE"))
	    {
	        return Localization.getString(Strings.CATEGORY_NONE);
	    }
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