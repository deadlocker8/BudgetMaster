package de.deadlocker8.budgetmaster.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Payment
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer ID;
	private Integer amount;
	private String date;
	private Integer categoryID;
	private String name;
	private String description;
//	private RepeatingDefinition repeatingDefinition;

	public Integer getID()
	{
		return ID;
	}

	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	public Integer getAmount()
	{
		return amount;
	}

	public void setAmount(Integer amount)
	{
		this.amount = amount;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public Integer getCategoryID()
	{
		return categoryID;
	}

	public void setCategoryID(Integer categoryID)
	{
		this.categoryID = categoryID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

//	public RepeatingDefinition getRepeatingDefinition()
//	{
//		return repeatingDefinition;
//	}
//
//	public void setRepeatingDefinition(RepeatingDefinition repeatingDefinition)
//	{
//		this.repeatingDefinition = repeatingDefinition;
//	}

	@Override
	public String toString()
	{
		return "Payment{" +
				"ID=" + ID +
				", amount=" + amount +
				", date='" + date + '\'' +
				", categoryID=" + categoryID +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
//				", repeatingDefinition=" + repeatingDefinition +
				'}';
	}
}