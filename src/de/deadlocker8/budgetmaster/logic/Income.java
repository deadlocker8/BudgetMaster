package de.deadlocker8.budgetmaster.logic;

import java.time.LocalDate;

public class Income
{
	private double amount;
	private LocalDate date;	
	private String note;
	
	public Income(double amount, LocalDate date, String note)
	{
		this.amount = amount;
		this.date = date;
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
		return "Income [amount=" + amount + ", date=" + date + ", note=" + note + "]";
	}
}