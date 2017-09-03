package de.deadlocker8.budgetmaster.logic.charts;

import java.util.ArrayList;

import org.joda.time.DateTime;

public class MonthInOutSum
{
	private int month;
	private int year;
	private ArrayList<CategoryInOutSum> sums;
	
	public MonthInOutSum(int month, int year, ArrayList<CategoryInOutSum> sums)
	{
		this.month = month;
		this.year = year;
		this.sums = sums;
	}

	public int getMonth()
	{
		return month;
	}

	public int getYear()
	{
		return year;
	}

	public ArrayList<CategoryInOutSum> getSums()
	{
		return sums;
	}
	
	public DateTime getDate()
	{
		return DateTime.now().withYear(year).withMonthOfYear(month).withDayOfMonth(1);
	}
	
	public int getBudgetIN()
	{
		int budget = 0;
		for(CategoryInOutSum currentCategorySum : sums)
		{
			budget += currentCategorySum.getBudgetIN();
		}
			
		return budget;
	}
	
	public int getBudgetOUT()
	{
		int budget = 0;
		for(CategoryInOutSum currentCategorySum : sums)
		{
			budget += currentCategorySum.getBudgetOUT();
		}
			
		return budget;
	}

	@Override
	public String toString()
	{
		return "MonthInOutSum [month=" + month + ", year=" + year + ", sums=" + sums + "]";
	}
}