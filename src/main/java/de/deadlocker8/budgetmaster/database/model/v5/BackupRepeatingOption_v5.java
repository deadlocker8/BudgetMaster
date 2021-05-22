package de.deadlocker8.budgetmaster.database.model.v5;

import java.util.Objects;

public class BackupRepeatingOption_v5
{
	private String startDate;
	private BackupRepeatingModifier_v5 modifier;
	private BackupRepeatingEndOption_v5 endOption;

	public BackupRepeatingOption_v5()
	{
	}

	public BackupRepeatingOption_v5(String startDate, BackupRepeatingModifier_v5 modifier, BackupRepeatingEndOption_v5 endOption)
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

	public BackupRepeatingModifier_v5 getModifier()
	{
		return modifier;
	}

	public void setModifier(BackupRepeatingModifier_v5 modifier)
	{
		this.modifier = modifier;
	}

	public BackupRepeatingEndOption_v5 getEndOption()
	{
		return endOption;
	}

	public void setEndOption(BackupRepeatingEndOption_v5 endOption)
	{
		this.endOption = endOption;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupRepeatingOption_v5 that = (BackupRepeatingOption_v5) o;
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
		return "BackupRepeatingOption_v5{" +
				"startDate='" + startDate + '\'' +
				", modifier=" + modifier +
				", endOption=" + endOption +
				'}';
	}
}
