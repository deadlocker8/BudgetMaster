package de.deadlocker8.budgetmaster.logic.tag;

public class Tag
{
	private int ID;
	private String name;
	
	public Tag(int ID, String name)
	{
		this.ID = ID;
		this.name = name;
	}
	
	public int getID()
	{
		return ID;
	}

	public void setID(int ID)
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

	@Override
	public String toString()
	{
		return "Tag [ID=" + ID + ", Name=" + name + "]";
	}
}