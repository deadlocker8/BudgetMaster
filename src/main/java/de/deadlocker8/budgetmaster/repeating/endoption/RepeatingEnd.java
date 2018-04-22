package de.deadlocker8.budgetmaster.repeating.endoption;

import org.joda.time.DateTime;

import java.util.List;

public abstract class RepeatingEnd
{
	Object quantity;

	RepeatingEnd(Object quantity)
	{
		this.quantity = quantity;
	}

	public abstract boolean isEndReached(List<DateTime> dates);
}
