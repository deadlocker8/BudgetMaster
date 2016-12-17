package de.deadlocker8.budgetmaster.logic;

import java.time.LocalDate;

public class Payment
{	
	private boolean income;
	private double amount;
	private LocalDate date;
	private Category category;
	private String name;	
	private RepeatingType repeatingType;	
	private int repeatingPeriod;	
	
	public Payment(boolean income, double amount, LocalDate date, Category category, String name, RepeatingType repeatingType, int repeatingPeriod)
	{		
		this.income = income;
		this.amount = amount;
		this.date = date;
		this.category = category;
		this.name = name;
		this.repeatingType = repeatingType;
		this.repeatingPeriod = repeatingPeriod;
	}

	public boolean isIncome()
	{
		return income;
	}
	
	public void setIncome(boolean income)
	{
		this.income = income;
	}

	public double getAmount()
	{
		return amount;
	}

	public void setAmount(double amount)
	{
		this.amount = amount;
	}

	public LocalDate getDate()
	{
		return date;
	}

	public void setDate(LocalDate date)
	{
		this.date = date;
	}

	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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
		return "Payment [income=" + income + ", amount=" + amount + ", date=" + date + ", category=" + category + ", name=" + name + ", repeatingType=" + repeatingType + ", repeatingPeriod=" + repeatingPeriod + "]";
	}
}