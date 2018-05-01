package de.deadlocker8.budgetmaster.repeating.modifier;

import org.joda.time.DateTime;

import javax.persistence.Entity;

@Entity
public class RepeatingModifierYears extends RepeatingModifier
{
	public RepeatingModifierYears(int numberOfYears)
	{
		super(numberOfYears, "repeating.modifier.years");
	}

	public  RepeatingModifierYears() {}

	@Override
	public DateTime getNextDate(DateTime lastDate)
	{
		return lastDate.plusYears(super.quantity);
	}
}
