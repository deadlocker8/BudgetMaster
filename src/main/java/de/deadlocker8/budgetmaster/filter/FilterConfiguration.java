package de.deadlocker8.budgetmaster.filter;

import java.util.List;

public class FilterConfiguration
{
	private boolean includeIncome;
	private boolean includeExpenditure;
	private boolean includeNotRepeating;
	private boolean includeRepeating;
	private List<FilterCategory> filterCategories;

	public static final FilterConfiguration DEFAULT = new FilterConfiguration(true, true, true, true, null);

	public FilterConfiguration()
	{
	}

	public FilterConfiguration(boolean includeIncome, boolean includeExpenditure, boolean includeNotRepeating, boolean includeRepeating, List<FilterCategory> filterCategories)
	{
		this.includeIncome = includeIncome;
		this.includeExpenditure = includeExpenditure;
		this.includeNotRepeating = includeNotRepeating;
		this.includeRepeating = includeRepeating;
		this.filterCategories = filterCategories;
	}

	public boolean isIncludeIncome()
	{
		return includeIncome;
	}

	public void setIncludeIncome(boolean includeIncome)
	{
		this.includeIncome = includeIncome;
	}

	public boolean isIncludeExpenditure()
	{
		return includeExpenditure;
	}

	public void setIncludeExpenditure(boolean includeExpenditure)
	{
		this.includeExpenditure = includeExpenditure;
	}

	public boolean isIncludeNotRepeating()
	{
		return includeNotRepeating;
	}

	public void setIncludeNotRepeating(boolean includeNotRepeating)
	{
		this.includeNotRepeating = includeNotRepeating;
	}

	public boolean isIncludeRepeating()
	{
		return includeRepeating;
	}

	public void setIncludeRepeating(boolean includeRepeating)
	{
		this.includeRepeating = includeRepeating;
	}

	public List<FilterCategory> getFilterCategories()
	{
		return filterCategories;
	}

	public void setFilterCategories(List<FilterCategory> filterCategories)
	{
		this.filterCategories = filterCategories;
	}

	@Override
	public String toString()
	{
		return "FilterConfiguration{" +
				"includeIncome=" + includeIncome +
				", includeExpenditure=" + includeExpenditure +
				", includeNotRepeating=" + includeNotRepeating +
				", includeRepeating=" + includeRepeating +
				", filterCategories=" + filterCategories +
				'}';
	}
}
