package de.deadlocker8.budgetmaster.repeating.modifier;

import org.joda.time.DateTime;

import javax.persistence.Entity;

@Entity
public class RepeatingModifierMonths extends RepeatingModifier
{
	public RepeatingModifierMonths(int numberOfmonths)
	{
		super(numberOfmonths);
	}

	@Override
	public DateTime getNextDate(DateTime lastDate)
	{
		return lastDate.plusMonths(super.quantity);
	}
}
