package de.deadlocker8.budgetmaster.repeating.endoption;

import org.joda.time.DateTime;

import java.util.List;

public class RepeatingEndNever extends RepeatingEnd
{
	public RepeatingEndNever()
	{
		super(null);
	}

	@Override
	public boolean isEndReached(List<DateTime> dates)
	{
		return false;
	}
}
