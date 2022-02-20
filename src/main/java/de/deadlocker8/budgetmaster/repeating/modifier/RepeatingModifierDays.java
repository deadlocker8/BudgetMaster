package de.deadlocker8.budgetmaster.repeating.modifier;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class RepeatingModifierDays extends RepeatingModifier
{
	public RepeatingModifierDays(int numberOfDays)
	{
		super(numberOfDays, "repeating.modifier.days");
	}

	public RepeatingModifierDays()
	{
	}

	@Override
	public LocalDate getNextDate(LocalDate lastDate)
	{
		return lastDate.plusDays(super.quantity);
	}
}
