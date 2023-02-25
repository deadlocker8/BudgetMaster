package de.deadlocker8.budgetmaster.repeating.modifier;

import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
public class RepeatingModifierMonths extends RepeatingModifier
{
	public RepeatingModifierMonths(int numberOfMonths)
	{
		super(numberOfMonths, "repeating.modifier.months");
	}

	public RepeatingModifierMonths()
	{
	}

	@Override
	public LocalDate getNextDate(LocalDate lastDate)
	{
		return lastDate.plusMonths(super.quantity);
	}
}
