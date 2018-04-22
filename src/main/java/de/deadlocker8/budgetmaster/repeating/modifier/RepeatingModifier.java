package de.deadlocker8.budgetmaster.repeating.modifier;

import org.joda.time.DateTime;

public abstract class RepeatingModifier
{
	int quantity;

	RepeatingModifier(int quantity)
	{
		this.quantity = quantity;
	}

	public abstract DateTime getNextDate(DateTime lastDate);
}
