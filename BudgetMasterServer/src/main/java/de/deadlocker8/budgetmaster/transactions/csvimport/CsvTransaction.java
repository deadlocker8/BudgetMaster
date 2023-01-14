package de.deadlocker8.budgetmaster.transactions.csvimport;

import java.util.Objects;

public final class CsvTransaction
{
	private final String date;
	private String name;
	private final Integer amount;
	private String description;
	private CsvTransactionStatus status;

	public CsvTransaction(String date, String name, Integer amount, String description, CsvTransactionStatus status)
	{
		this.date = date;
		this.name = name;
		this.amount = amount;
		this.description = description;
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

	public Integer getAmount()
	{
		return amount;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
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
		return Objects.equals(date, that.date) && Objects.equals(name, that.name) && Objects.equals(amount, that.amount) && Objects.equals(description, that.description) && status == that.status;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(date, name, amount, description, status);
	}

	@Override
	public String toString()
	{
		return "CsvTransaction{" +
				"date='" + date + '\'' +
				", name='" + name + '\'' +
				", amount=" + amount +
				", description='" + description + '\'' +
				", status=" + status +
				'}';
	}
}
