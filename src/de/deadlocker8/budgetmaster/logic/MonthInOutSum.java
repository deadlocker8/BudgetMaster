package de.deadlocker8.budgetmaster.logic;

import org.joda.time.DateTime;

public class MonthInOutSum
{
	private int month;
	private int year;
	private int budgetIN;
	private int budgetOUT;
	
	public MonthInOutSum(int month, int year, int budgetIN, int budgetOUT)
	{
		this.month = month;
		this.year = year;
		this.budgetIN = budgetIN;
		this.budgetOUT = budgetOUT;
	}
	
	public DateTime getDate()
	{
		return DateTime.now().withYear(year).withMonthOfYear(month).withDayOfMonth(1);
	}
	
	public int getMonth()
	{
		return month;
	}

	public int getYear()
	{
		return year;
	}

	public int getBudgetIN()
	{
		return budgetIN;
	}

	public void setBudgetIN(int budgetIN)
	{
		this.budgetIN = budgetIN;
	}

	public int getBudgetOUT()
	{
		return budgetOUT;
	}

	public void setBudgetOUT(int budgetOUT)
	{
		this.budgetOUT = budgetOUT;
	}

	@Override
	public String toString()
	{
		return "MonthInOutSum [month=" + month + ", year=" + year + ", budgetIN=" + budgetIN + ", budgetOUT=" + budgetOUT + "]";
	}
}