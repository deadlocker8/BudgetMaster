package de.deadlocker8.budgetmaster.logic;

public abstract class Payment
{	
	private int ID;
	private int amount;
	private String date;
	private int categoryID;
	private String name;
	
	public Payment(int ID, int amount, String date, int categoryID, String name)
	{		
		this.ID = ID;
		this.amount = amount;
		this.date = date;
		this.categoryID = categoryID;
		this.name = name;
	}

	public int getID()
	{
		return ID;
	}

	public void setID(int iD)
	{
		ID = iD;
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

	public int getCategoryID()
	{
		return categoryID;
	}

	public void setCategoryID(int categoryID)
	{
		this.categoryID = categoryID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public boolean isIncome()
	{
		return amount > 0;
	}
}