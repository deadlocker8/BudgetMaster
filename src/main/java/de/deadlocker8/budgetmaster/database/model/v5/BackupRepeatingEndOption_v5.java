package de.deadlocker8.budgetmaster.database.model.v5;

import org.joda.time.DateTime;

import java.util.Objects;

public class BackupRepeatingEndOption_v5
{
	private String localizationKey;
	private int times;
	private String endDate;

	public BackupRepeatingEndOption_v5()
	{
	}

	public String getLocalizationKey()
	{
		return localizationKey;
	}

	public void setLocalizationKey(String localizationKey)
	{
		this.localizationKey = localizationKey;
	}

	public int getTimes()
	{
		return times;
	}

	public void setTimes(int times)
	{
		this.times = times;
	}

	public String getEndDate()
	{
		return endDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupRepeatingEndOption_v5 that = (BackupRepeatingEndOption_v5) o;
		return times == that.times && Objects.equals(localizationKey, that.localizationKey) && Objects.equals(endDate, that.endDate);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(localizationKey, times, endDate);
	}

	@Override
	public String toString()
	{
		return "BackupRepeatingEndOption_v5{" +
				"localizationKey='" + localizationKey + '\'' +
				", times=" + times +
				", endDate='" + endDate + '\'' +
				'}';
	}
}
