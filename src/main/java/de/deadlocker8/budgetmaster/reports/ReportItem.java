package de.deadlocker8.budgetmaster.reports;


import de.deadlocker8.budgetmaster.entities.Category;

public class ReportItem
{
	private int position;
	private int amount;
	private String date;
	private Category category;
	private String name;
	private String description;
	private String tags;
	private boolean repeating;

	public ReportItem()
	{
		
	}

	public int getPosition()
	{
		return position;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}

	public int getAmount()
	{
		return amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}	

	public String getTags()
	{
		return tags;
	}

	public void setTags(String tags)
	{
		this.tags = tags;
	}

	public boolean getRepeating()
	{
		return repeating;
	}

	public void setRepeating(boolean repeating)
	{
		this.repeating = repeating;
	}

	@Override
	public String toString()
	{
		return "ReportItem [position=" + position + ", amount=" + amount + ", date=" + date + ", category=" + category + ", name=" + name + ", description=" + description + ", tags=" + tags + ", repeating=" + repeating + "]";
	}
}