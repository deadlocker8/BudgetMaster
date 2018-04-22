package de.deadlocker8.budgetmaster.repeating;

import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEnd;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifier;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class RepeatingOption
{
	private DateTime startDate;
	private RepeatingModifier modifier;
	private RepeatingEnd endOption;

	public RepeatingOption(DateTime startDate, RepeatingModifier modifier, RepeatingEnd endOption)
	{
		this.startDate = startDate;
		this.modifier = modifier;
		this.endOption = endOption;
	}

	public DateTime getStartDate()
	{
		return startDate;
	}

	public void setStartDate(DateTime startDate)
	{
		this.startDate = startDate;
	}

	public RepeatingModifier getModifier()
	{
		return modifier;
	}

	public void setModifier(RepeatingModifier modifier)
	{
		this.modifier = modifier;
	}

	public RepeatingEnd getEndOption()
	{
		return endOption;
	}

	public void setEndOption(RepeatingEnd endOption)
	{
		this.endOption = endOption;
	}

	public List<DateTime> getRepeatingDates(DateTime dateFetchLimit)
	{
		List<DateTime> dates = new ArrayList<>();
		dates.add(startDate);
		while(!endOption.isEndReached(dates))
		{
			DateTime nextDate = modifier.getNextDate(dates.get(dates.size() - 1));
			if(nextDate.isAfter(dateFetchLimit))
			{
				return dates;
			}

			List<DateTime> temporaryList = new ArrayList<>(dates);
			temporaryList.add(nextDate);
			if(endOption.isEndReached((temporaryList)))
			{
				return dates;
			}
			else
			{
				dates.add(nextDate);
			}
		}

		return dates;
	}

	@Override
	public String toString()
	{
		return "RepeatingOption{" +
				"startDate=" + startDate +
				", modifier=" + modifier +
				", endOption=" + endOption +
				'}';
	}
}
