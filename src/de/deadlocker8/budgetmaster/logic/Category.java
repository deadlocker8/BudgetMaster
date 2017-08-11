package de.deadlocker8.budgetmaster.logic;

import de.deadlocker8.budgetmaster.logic.utils.Strings;
import javafx.scene.paint.Color;
import tools.Localization;

public class Category
{
	private int ID;
	private String name;
	private Color color;

	public Category(String name, Color color)
	{
		this.name = name;
		this.color = color;
	}

	public Category(int ID, String name, Color color)
	{
		this.ID = ID;
		this.name = name;
		this.color = color;
	}

	public int getID()
	{
		return ID;
	}

	public String getName()
	{	    
	    if(ID == 1)
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

	@Override
	public String toString()
	{
		return "Category [ID=" + ID + ", name=" + name + ", color=" + color + "]";
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Category other = (Category)obj;
		if(ID != other.ID)
			return false;
		return true;
	}
}