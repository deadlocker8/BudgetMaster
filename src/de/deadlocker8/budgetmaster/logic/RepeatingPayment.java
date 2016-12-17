package de.deadlocker8.budgetmaster.logic;

import java.time.LocalDate;

public class RepeatingPayment extends Payment
{	
	private RepeatingType repeatingType;	
	private int repeatingPeriod;
	
	public RepeatingPayment(double amount, LocalDate date, Category category, String note, RepeatingType repeatingType, int repeatingPeriod)
	{
		super(amount, date, category, note);
		// TODO Auto-generated constructor stub
	}

	public RepeatingType getRepeatingType()
	{
		return repeatingType;
	}

	public void setRepeatingType(RepeatingType repeatingType)
	{
		this.repeatingType = repeatingType;
	}

	public int getRepeatingPeriod()
	{
		return repeatingPeriod;
	}

	public void setRepeatingPeriod(int repeatingPeriod)
	{
		this.repeatingPeriod = repeatingPeriod;
	}

	@Override
	public String toString()
	{
		return "RepeatingPayment [" + super.toString() + "repeatingType=" + repeatingType + ", repeatingPeriod=" + repeatingPeriod + "]";
	}
}