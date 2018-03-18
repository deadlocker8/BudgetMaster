package de.deadlocker8.budgetmaster.entities;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
public class Payment
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer ID;
	private Integer amount;
	private DateTime date;
	@ManyToOne
	private Category category;
	private String name;
	private String description;
//	private RepeatingDefinition repeatingDefinition;

	public Payment()
	{
	}

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

	public DateTime getDate()
	{
		return date;
	}

	public void setDate(DateTime date)
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
				", category=" + category +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
//				", repeatingDefinition=" + repeatingDefinition +
				'}';
	}
}