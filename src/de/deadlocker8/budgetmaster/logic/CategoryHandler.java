package de.deadlocker8.budgetmaster.logic;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public class CategoryHandler
{
	private ArrayList<Category> categories;

	public CategoryHandler(ArrayList<Category> categories)
	{
		this.categories = new ArrayList<>();
		if(categories != null)
		{
			this.categories.addAll(categories);
		}
	}

	public ArrayList<Category> getCategories()
	{
		return categories;
	}
	
	public Category getCategory(int ID)
	{
		for(Category currentCategory : categories)
		{
			if(currentCategory.getID() == ID)
			{
				return currentCategory;
			}
		}
		
		return new Category(0, "NONE", Color.web("#FFFFFF"));
	}
}