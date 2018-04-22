package de.deadlocker8.budgetmaster.repeating.modifier;

import org.joda.time.DateTime;

public class RepeatingModifierYears extends RepeatingModifier
{
	public RepeatingModifierYears(int numberOfYears)
	{
		super(numberOfYears);
	}

	@Override
	public DateTime getNextDate(DateTime lastDate)
	{
		return lastDate.plusYears(super.quantity);
	}
}
