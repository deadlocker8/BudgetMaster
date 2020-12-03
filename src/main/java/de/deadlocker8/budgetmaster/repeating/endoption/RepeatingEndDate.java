package de.deadlocker8.budgetmaster.repeating.endoption;

import com.google.gson.annotations.Expose;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class RepeatingEndDate extends RepeatingEnd
{
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	@Expose
	private DateTime endDate;

	public RepeatingEndDate(DateTime endDate)
	{
		super("repeating.end.key.date");
		this.endDate = endDate;
	}

	public RepeatingEndDate() {}

	@Override
	@Transient
	public boolean isEndReached(List<DateTime> dates)
	{
		DateTime lastDate = dates.get(dates.size() - 1);
		return lastDate.isAfter(endDate);
	}

	@Override
	public Object getValue()
	{
		return endDate;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		if(!super.equals(o)) return false;
		RepeatingEndDate that = (RepeatingEndDate) o;
		return Objects.equals(endDate, that.endDate);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), endDate);
	}
}