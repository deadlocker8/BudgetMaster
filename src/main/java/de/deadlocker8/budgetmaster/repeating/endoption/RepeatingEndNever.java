package de.deadlocker8.budgetmaster.repeating.endoption;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.List;

@Entity
public class RepeatingEndNever extends RepeatingEnd
{
	@Override
	@Transient
	public boolean isEndReached(List<DateTime> dates)
	{
		return false;
	}
}