package de.deadlocker8.budgetmaster.logic;

public class NormalPayment extends Payment
{	
	public NormalPayment(int ID, int amount, String date, int categoryID, String name)
	{		
		super(ID, amount, date, categoryID, name);
	}
	
	@Override
	public String toString()
	{
		return "Payment [ID=" + super.getID() + ", amount=" + super.getAmount() + ", date=" + super.getDate() + ", categoryID=" + super.getCategoryID() + ", name=" + super.getName() + "]";
	}
}