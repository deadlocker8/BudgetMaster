package de.deadlocker8.budgetmaster.repeating.endoption;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.List;

@Entity
public class RepeatingEndDate extends RepeatingEnd
{
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private DateTime endDate;

	public RepeatingEndDate(DateTime endDate)
	{
		this.endDate = endDate;
	}

	@Override
	@Transient
	public boolean isEndReached(List<DateTime> dates)
	{
		DateTime lastDate = dates.get(dates.size() - 1);
		return lastDate.isAfter(endDate);
	}
}