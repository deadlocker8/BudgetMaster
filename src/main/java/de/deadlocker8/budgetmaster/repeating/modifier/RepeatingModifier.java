package de.deadlocker8.budgetmaster.repeating.modifier;

import com.google.gson.annotations.Expose;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public abstract class RepeatingModifier
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Expose
	private Integer ID;

	@Expose
	Integer quantity;

	@Expose
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

	@Override
	public String toString()
	{
		return "RepeatingModifier{" +
				"ID=" + ID +
				", quantity=" + quantity +
				", localizationKey='" + localizationKey + '\'' +
				'}';
	}
}