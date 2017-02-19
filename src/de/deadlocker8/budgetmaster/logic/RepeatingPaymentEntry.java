package de.deadlocker8.budgetmaster.logic;

public class RepeatingPaymentEntry extends Payment
{		
	private int repeatingPaymentID;		
	private int repeatInterval;
	private String repeatEndDate;
	private int repeatMonthDay;
	
	public RepeatingPaymentEntry(int ID, int repeatingPaymentID, String date, int amount, int categoryID, String name, int repeatInterval, String repeatEndDate, int repeatMonthDay)
	{
		super(ID, amount, date, categoryID, name);		
		this.repeatingPaymentID = repeatingPaymentID;	
		this.repeatInterval = repeatInterval;
		this.repeatEndDate = repeatEndDate;
		this.repeatMonthDay = repeatMonthDay;
	}
	
	public int getRepeatingPaymentID()
	{
		return repeatingPaymentID;
	}

	public int getRepeatInterval()
	{
		return repeatInterval;
	}

	public String getRepeatEndDate()
	{
		return repeatEndDate;
	}

	public int getRepeatMonthDay()
	{
		return repeatMonthDay;
	}

	@Override
	public String toString()
	{
		return "RepeatingPaymentEntry [ID=" + super.getID() + ", repeatingPaymentID=" + repeatingPaymentID + ", date=" + super.getDate() + ", amount=" + super.getAmount() + ", categoryID=" + super.getCategoryID() + ", name=" + super.getName() + ", repeatInterval=" + repeatInterval + ", repeatEndDate=" + repeatEndDate + ", repeatMonthDay="
				+ repeatMonthDay + "]";
	}
}