package de.deadlocker8.budgetmaster.repeating.endoption;

import com.google.gson.annotations.Expose;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
public class RepeatingEndAfterXTimes extends RepeatingEnd
{
	@Expose
	private int times;

	public RepeatingEndAfterXTimes(int times)
	{
		super("repeating.end.key.afterXTimes");
		this.times = times;
	}

	public RepeatingEndAfterXTimes()
	{
	}

	@Override
	@Transient
	public boolean isEndReached(List<LocalDate> dates)
	{
		return dates.size() - 1 > times;
	}

	@Override
	public Object getValue()
	{
		return times;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		if(!super.equals(o)) return false;
		RepeatingEndAfterXTimes that = (RepeatingEndAfterXTimes) o;
		return times == that.times;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), times);
	}
}