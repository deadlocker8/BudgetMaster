package de.deadlocker8.budgetmaster.repeating.modifier;

import com.google.gson.annotations.Expose;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Objects;

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

	public RepeatingModifier(int quantity, String localizationKey)
	{
		this.quantity = quantity;
		this.localizationKey = localizationKey;
	}

	public RepeatingModifier() {}

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

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		RepeatingModifier that = (RepeatingModifier) o;
		return Objects.equals(ID, that.ID) &&
				Objects.equals(quantity, that.quantity) &&
				Objects.equals(localizationKey, that.localizationKey);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, quantity, localizationKey);
	}

	public static RepeatingModifier fromModifierType(RepeatingModifierType type, int quantity)
	{
		switch(type)
		{
			case DAYS:
				return new RepeatingModifierDays(quantity);
			case MONTHS:
				return new RepeatingModifierMonths(quantity);
			case YEARS:
				return new RepeatingModifierYears(quantity);
		}

		return null;
	}
}