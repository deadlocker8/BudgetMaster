package de.deadlocker8.budgetmaster.categories;

public class DestinationCategory
{
	private Category category;

	public DestinationCategory()
	{
		// empty
	}

	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

	@Override
	public String toString()
	{
		return "DestinationCategory{" +
				"category=" + category +
				'}';
	}
}
