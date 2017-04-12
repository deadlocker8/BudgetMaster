package de.deadlocker8.budgetmaster.logic;

public class NormalPayment extends Payment
{	
	public NormalPayment(int ID, int amount, String date, int categoryID, String name, String description)
	{		
		super(ID, amount, date, categoryID, name, description);
	}
	
	@Override
	public String toString()
	{
		return "NormalPayment [ID=" + super.getID() + ", amount=" + super.getAmount() + ", date=" + super.getDate() + ", categoryID=" + super.getCategoryID() + ", name=" + super.getName() + ", description=" + super.getDescription() + "]";
	}
}