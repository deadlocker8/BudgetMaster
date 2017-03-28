package de.deadlocker8.budgetmaster.logic;

public class LatestRepeatingPayment
{
	private int ID;
	private int repeatingPaymentID;				
	private String lastDate;
	
	public LatestRepeatingPayment(int ID, int repeatingPaymentID, String lastDate)
	{
		this.ID = ID;
		this.repeatingPaymentID = repeatingPaymentID;
		this.lastDate = lastDate;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public int getRepeatingPaymentID()
	{
		return repeatingPaymentID;
	}
	
	public String getLastDate()
	{
		return lastDate;
	}
	
	@Override
	public String toString()
	{
		return "LatestRepeatingPayment [ID=" + ID + ", repeatingPaymentID=" + repeatingPaymentID + ", lastDate=" + lastDate + "]";
	}
}