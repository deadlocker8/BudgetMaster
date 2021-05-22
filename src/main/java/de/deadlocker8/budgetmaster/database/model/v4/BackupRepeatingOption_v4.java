package de.deadlocker8.budgetmaster.database.model.v4;

import java.util.Objects;

public class BackupRepeatingOption_v4
{
	private String startDate;
	private BackupRepeatingModifier_v4 modifier;
	private BackupRepeatingEndOption_v4 endOption;

	public BackupRepeatingOption_v4()
	{
	}

	public BackupRepeatingOption_v4(String startDate, BackupRepeatingModifier_v4 modifier, BackupRepeatingEndOption_v4 endOption)
	{
		this.startDate = startDate;
		this.modifier = modifier;
		this.endOption = endOption;
	}

	public String getStartDate()
	{
		return startDate;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public BackupRepeatingModifier_v4 getModifier()
	{
		return modifier;
	}

	public void setModifier(BackupRepeatingModifier_v4 modifier)
	{
		this.modifier = modifier;
	}

	public BackupRepeatingEndOption_v4 getEndOption()
	{
		return endOption;
	}

	public void setEndOption(BackupRepeatingEndOption_v4 endOption)
	{
		this.endOption = endOption;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupRepeatingOption_v4 that = (BackupRepeatingOption_v4) o;
		return Objects.equals(startDate, that.startDate) && Objects.equals(modifier, that.modifier) && Objects.equals(endOption, that.endOption);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(startDate, modifier, endOption);
	}

	@Override
	public String toString()
	{
		return "BackupRepeatingOption_v4{" +
				"startDate='" + startDate + '\'' +
				", modifier=" + modifier +
				", endOption=" + endOption +
				'}';
	}
}
