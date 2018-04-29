package de.deadlocker8.budgetmaster.repeating.endoption;

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
	private Integer ID;

	public Integer getID()
	{
		return ID;
	}

	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	public abstract boolean isEndReached(List<DateTime> dates);
}