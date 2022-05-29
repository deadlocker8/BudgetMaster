package de.deadlocker8.budgetmaster.repeating.endoption;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.List;

@Entity
public class RepeatingEndNever extends RepeatingEnd
{
	public RepeatingEndNever()
	{
		super("repeating.end.key.never");
	}

	@Override
	@Transient
	public boolean isEndReached(List<LocalDate> dates)
	{
		return false;
	}

	@Override
	public Object getValue()
	{
		return null;
	}
}