package de.deadlocker8.budgetmaster.transactions.csvimport;

import de.deadlocker8.budgetmaster.categories.Category;

import java.time.LocalDate;
import java.util.Objects;

public final class CsvTransaction
{
	private final LocalDate date;
	private String name;
	private final Integer amount;
	private String description;
	private CsvTransactionStatus status;
	private Category category;

	public CsvTransaction(LocalDate date, String name, Integer amount, String description, CsvTransactionStatus status, Category category)
	{
		this.date = date;
		this.name = name;
		this.amount = amount;
		this.description = description;
		this.status = status;
		this.category = category;
	}

	public LocalDate getDate()
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

	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		CsvTransaction that = (CsvTransaction) o;
		return Objects.equals(date, that.date) && Objects.equals(name, that.name) && Objects.equals(amount, that.amount) && Objects.equals(description, that.description) && status == that.status && Objects.equals(category, that.category);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(date, name, amount, description, status, category);
	}

	@Override
	public String toString()
	{
		return "CsvTransaction{" +
				"date=" + date +
				", name='" + name + '\'' +
				", amount=" + amount +
				", description='" + description + '\'' +
				", status=" + status +
				", category=" + category +
				'}';
	}
}
