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

	@Transient
	private String localizationKey;

	RepeatingModifier(int quantity, String localizationKey)
	{
		this.quantity = quantity;
		this.localizationKey = localizationKey;
	}

	RepeatingModifier() {}

	public Integer getID()
	{
		return ID;
	}

	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	public Integer getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}

	public String getLocalizationKey()
	{
		return localizationKey;
	}

	public void setLocalizationKey(String localizationKey)
	{
		this.localizationKey = localizationKey;
	}

	@Transient
	public abstract DateTime getNextDate(DateTime lastDate);
}