package de.deadlocker8.budgetmaster.logic;

public class RepeatingPayment extends Payment
{	
	private int repeatInterval;
	private String repeatEndDate;
	private int repeatMonthDay;

	public RepeatingPayment(int ID, int amount, String date, int categoryID, String name, int repeatInterval, String repeatEndDate, int repeatMonthDay)
	{
		super(ID, amount, date, categoryID, name);
		this.repeatInterval = repeatInterval;
		this.repeatEndDate = repeatEndDate;
		this.repeatMonthDay = repeatMonthDay;
	}

	public int getRepeatInterval()
	{
		return repeatInterval;
	}

	public void setRepeatInterval(int repeatInterval)
	{
		this.repeatInterval = repeatInterval;
	}

	public String getRepeatEndDate()
	{
		return repeatEndDate;
	}

	public void setRepeatEndDate(String repeatEndDate)
	{
		this.repeatEndDate = repeatEndDate;
	}

	public int getRepeatMonthDay()
	{
		return repeatMonthDay;
	}

	public void setRepeatMonthDay(int repeatMonthDay)
	{
		this.repeatMonthDay = repeatMonthDay;
	}
	
	@Override
	public String toString()
	{
		return "RepeatingPayment [ID=" + super.getID() + ", amount=" + super.getAmount() + ", date=" + super.getDate() + ", categoryID=" + super.getCategoryID() + ", name=" + super.getName() + ", repeatInterval=" + repeatInterval + ", repeatEndDate=" + repeatEndDate + ", repeatMonthDay=" + repeatMonthDay + "]";
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof LatestRepeatingPayment)
		{
			return super.getID() == ((LatestRepeatingPayment)obj).getRepeatingPaymentID();
		}
		else if(obj instanceof RepeatingPayment)
		{
			return super.getID() == ((RepeatingPayment)obj).getID();
		}
		return super.equals(obj);
	}
}