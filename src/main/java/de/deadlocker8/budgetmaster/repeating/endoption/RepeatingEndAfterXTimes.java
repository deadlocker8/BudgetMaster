package de.deadlocker8.budgetmaster.repeating.endoption;

import org.joda.time.DateTime;

import java.util.List;

public class RepeatingEndAfterXTimes extends RepeatingEnd
{
	public RepeatingEndAfterXTimes(int times)
	{
		super(times);
	}

	@Override
	public boolean isEndReached(List<DateTime> dates)
	{
		return dates.size() - 1 > (int)super.quantity;
	}
}
