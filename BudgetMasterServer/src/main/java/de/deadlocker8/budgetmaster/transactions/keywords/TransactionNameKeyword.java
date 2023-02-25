package de.deadlocker8.budgetmaster.transactions.keywords;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.utils.ProvidesID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity
public class TransactionNameKeyword implements ProvidesID
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Integer ID;

	@Expose
	private String value;

	public TransactionNameKeyword()
	{
	}

	public TransactionNameKeyword(String value)
	{
		this.value = value;
	}

	public TransactionNameKeyword(Integer ID, String value)
	{
		this.ID = ID;
		this.value = value;
	}

	@Override
	public Integer getID()
	{
		return ID;
	}

	@Override
	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return "TransactionNameKeyword{" +
				"ID=" + ID +
				", value='" + value + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		TransactionNameKeyword transactionNameKeyword = (TransactionNameKeyword) o;
		return Objects.equals(ID, transactionNameKeyword.ID) && Objects.equals(value, transactionNameKeyword.value);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, value);
	}
}
