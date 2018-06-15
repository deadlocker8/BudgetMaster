package de.deadlocker8.budgetmaster.repeating.endoption;

import com.google.gson.annotations.Expose;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public abstract class RepeatingEnd
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Expose
	private Integer ID;

	@Expose
	private String localizationKey;

	RepeatingEnd(String localizationKey)
	{
		this.localizationKey = localizationKey;
	}

	RepeatingEnd() {}

	public Integer getID()
	{
		return ID;
	}

	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	public String getLocalizationKey()
	{
		return localizationKey;
	}

	public void setLocalizationKey(String localizationKey)
	{
		this.localizationKey = localizationKey;
	}

	public abstract boolean isEndReached(List<DateTime> dates);

	public abstract  Object getValue();

	@Override
	public String toString()
	{
		return "RepeatingEnd{" +
				"ID=" + ID +
				", localizationKey='" + localizationKey + '\'' +
				'}';
	}
}