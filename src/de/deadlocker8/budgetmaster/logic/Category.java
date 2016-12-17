package de.deadlocker8.budgetmaster.logic;

import javafx.scene.paint.Color;

public class Category
{
	private String name;
	private Color color;
	
	public Category(String name, Color color)
	{
		this.name = name;
		this.color = color;
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

	@Override
	public String toString()
	{
		return "Category [name=" + name + ", color=" + color + "]";
	}
}