package de.deadlocker8.budgetmaster.filter;

public class FilterObject
{
	private Integer ID;
	private String name;
	private boolean include;

	public FilterObject()
	{
	}

	public FilterObject(Integer ID, String name, boolean include)
	{
		this.ID = ID;
		this.name = name;
		this.include = include;
	}

	public Integer getID()
	{
		return ID;
	}

	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isInclude()
	{
		return include;
	}

	public void setInclude(boolean include)
	{
		this.include = include;
	}

	@Override
	public String toString()
	{
		return "FilterObject{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", include=" + include +
				'}';
	}
}
