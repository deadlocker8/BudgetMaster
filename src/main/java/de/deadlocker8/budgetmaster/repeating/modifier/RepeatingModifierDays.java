package de.deadlocker8.budgetmaster.repeating.modifier;

import org.joda.time.DateTime;

import javax.persistence.Entity;

@Entity
public class RepeatingModifierDays extends RepeatingModifier
{
	public RepeatingModifierDays(int numberOfDays)
	{
		super(numberOfDays, "repeating.modifier.days");
	}

	public RepeatingModifierDays() {}

	@Override
	public DateTime getNextDate(DateTime lastDate)
	{
		return lastDate.plusDays(super.quantity);
	}
}
