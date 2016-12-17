package de.deadlocker8.budgetmaster.logic;

import java.time.LocalDate;

public class Payment
{
	private double amount;
	private LocalDate date;
	private Category category;
	private String note;	
	
	public Payment(double amount, LocalDate date, Category category, String note)
	{		
		this.amount = amount;
		this.date = date;
		this.category = category;
		this.note = note;
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

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	@Override
	public String toString()
	{
		return "Payment [amount=" + amount + ", date=" + date + ", category=" + category + ", note=" + note + "]";
	}
}