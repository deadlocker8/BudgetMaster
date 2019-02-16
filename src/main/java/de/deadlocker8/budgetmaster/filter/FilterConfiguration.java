package de.deadlocker8.budgetmaster.filter;

import java.util.ArrayList;
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

	public Boolean isIncludeRepeatingAndNotRepeating()
	{
		if(isIncludeNotRepeating() && isIncludeRepeating())
		{
			return null;
		}
		return isIncludeRepeating();
	}

	public List<FilterCategory> getFilterCategories()
	{
		return filterCategories;
	}

	public void setFilterCategories(List<FilterCategory> filterCategories)
	{
		this.filterCategories = filterCategories;
	}

	public List<Integer> getIncludedCategoryIDs()
	{
		if(filterCategories == null)
		{
			return null;
		}

		List<Integer> includedCategoryIDs = new ArrayList<>();
		for(FilterCategory filterCategory : filterCategories)
		{
			if(filterCategory.isInclude())
			{
				includedCategoryIDs.add(filterCategory.getID());
			}
		}

		if(includedCategoryIDs.size() == filterCategories.size())
		{
			return null;
		}

		return includedCategoryIDs;
	}

	public boolean isActive()
	{
		FilterConfiguration defaultConfiguration = FilterConfiguration.DEFAULT;
		if(defaultConfiguration.isIncludeIncome() != isIncludeIncome())
		{
			return true;
		}

		if(defaultConfiguration.isIncludeExpenditure() != isIncludeExpenditure())
		{
			return true;
		}

		if(defaultConfiguration.isIncludeNotRepeating() != isIncludeNotRepeating())
		{
			return true;
		}

		if(defaultConfiguration.isIncludeRepeating() != isIncludeRepeating())
		{
			return true;
		}

		for(FilterCategory filterCategory : filterCategories)
		{
			if(!filterCategory.isInclude())
			{
				return true;
			}
		}

		return false;
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
