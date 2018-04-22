package de.deadlocker8.budgetmaster.repeating.modifier;

import org.joda.time.DateTime;

public class RepeatingModifierDays extends RepeatingModifier
{
	public RepeatingModifierDays(int numberOfDays)
	{
		super(numberOfDays);
	}

	@Override
	public DateTime getNextDate(DateTime lastDate)
	{
		return lastDate.plusDays(super.quantity);
	}
}
