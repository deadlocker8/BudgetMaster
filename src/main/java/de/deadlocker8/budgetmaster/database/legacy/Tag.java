package de.deadlocker8.budgetmaster.database.legacy;

@Deprecated
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

	public String getName()
	{
		return name;
	}
}