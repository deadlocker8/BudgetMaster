package de.deadlocker8.budgetmaster.repeating.endoption;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

public class RepeatingEndDate extends RepeatingEnd
{
	public RepeatingEndDate(DateTime endDate)
	{
		super(endDate);
	}

	@Override
	public boolean isEndReached(List<DateTime> dates)
	{
		DateTime lastDate = dates.get(dates.size() - 1);
		DateTime endDate = (DateTime)super.quantity;
		return lastDate.isAfter(endDate);
	}
}
