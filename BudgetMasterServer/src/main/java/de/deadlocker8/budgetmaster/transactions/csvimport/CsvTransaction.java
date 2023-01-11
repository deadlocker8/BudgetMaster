package de.deadlocker8.budgetmaster.transactions.csvimport;

import java.util.Objects;

public final class CsvTransaction
{
	private final String date;
	private String name;
	private final String amount;
	private CsvTransactionStatus status;

	public CsvTransaction(String date, String name, String amount, CsvTransactionStatus status)
	{
		this.date = date;
		this.name = name;
		this.amount = amount;
		this.status = status;
	}

	public String getDate()
	{
		return date;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAmount()
	{
		return amount;
	}

	public CsvTransactionStatus getStatus()
	{
		return status;
	}

	public void setStatus(CsvTransactionStatus status)
	{
		this.status = status;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		CsvTransaction that = (CsvTransaction) o;
		return Objects.equals(date, that.date) && Objects.equals(name, that.name) && Objects.equals(amount, that.amount) && status == that.status;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(date, name, amount, status);
	}

	@Override
	public String toString()
	{
		return "CsvTransaction{" +
				"date='" + date + '\'' +
				", name='" + name + '\'' +
				", amount='" + amount + '\'' +
				", status=" + status +
				'}';
	}
}
