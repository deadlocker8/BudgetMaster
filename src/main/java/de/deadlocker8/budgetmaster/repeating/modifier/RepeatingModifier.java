package de.deadlocker8.budgetmaster.repeating.modifier;

import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public abstract class RepeatingModifier
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer ID;

	Integer quantity;

	RepeatingModifier(int quantity)
	{
		this.quantity = quantity;
	}

	public Integer getID()
	{
		return ID;
	}

	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	@Transient
	public abstract DateTime getNextDate(DateTime lastDate);
}