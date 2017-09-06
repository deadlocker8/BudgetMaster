package de.deadlocker8.budgetmaster.logic.category;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.utils.Strings;
import tools.Localization;

public class CategoryHandler
{
	private ArrayList<Category> categories;

	public CategoryHandler(ArrayList<Category> categories)
	{
		this.categories = new ArrayList<>();
		if(categories != null)
		{
			this.categories.addAll(categories);		
		
			//set correct localized name for category "rest"
			for(Category currentCategory : categories)
			{
				if(currentCategory.getID() == 2)
				{
					currentCategory.setName(Localization.getString(Strings.CATEGORY_REST));
				}
			}
		}
	}

	public ArrayList<Category> getCategories()
	{
		return categories;
	}
	
	public ArrayList<Category> getCategoriesWithoutNone()
	{
		ArrayList<Category> categoriesWithoutNone = new ArrayList<>();
		for(Category currentCategory : categories)
		{			
			if(currentCategory.getID() != 1)
			{
				categoriesWithoutNone.add(currentCategory);
			}
		}
		
		return categoriesWithoutNone;
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
		
		return new Category(1, "NONE", "#FFFFFF");
	}
}