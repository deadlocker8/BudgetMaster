package de.deadlocker8.budgetmaster.repeating.modifier;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class RepeatingModifierYears extends RepeatingModifier
{
	public RepeatingModifierYears(int numberOfYears)
	{
		super(numberOfYears, "repeating.modifier.years");
	}

	public RepeatingModifierYears()
	{
	}

	@Override
	public LocalDate getNextDate(LocalDate lastDate)
	{
		return lastDate.plusYears(super.quantity);
	}
}
