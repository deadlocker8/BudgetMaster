package de.deadlocker8.budgetmaster.filter;

import java.util.ArrayList;
import java.util.List;

public class FilterConfiguration
{
	private boolean includeIncome;
	private boolean includeExpenditure;
	private boolean includeTransfer;
	private boolean includeNotRepeating;
	private boolean includeRepeating;
	private List<FilterObject> filterCategories;
	private List<FilterObject> filterTags;
	private String name;

	public static final FilterConfiguration DEFAULT = new FilterConfiguration(true, true, true, true, true, null, null, "");

	public FilterConfiguration()
	{
	}

	public FilterConfiguration(boolean includeIncome, boolean includeExpenditure, boolean includeTransfer, boolean includeNotRepeating, boolean includeRepeating, List<FilterObject> filterCategories, List<FilterObject> filterTags, String name)
	{
		this.includeIncome = includeIncome;
		this.includeExpenditure = includeExpenditure;
		this.includeTransfer = includeTransfer;
		this.includeNotRepeating = includeNotRepeating;
		this.includeRepeating = includeRepeating;
		this.filterCategories = filterCategories;
		this.filterTags = filterTags;
		this.name = name;
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

	public boolean isIncludeTransfer()
	{
		return includeTransfer;
	}

	public void setIncludeTransfer(boolean includeTransfer)
	{
		this.includeTransfer = includeTransfer;
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

	public List<FilterObject> getFilterCategories()
	{
		return filterCategories;
	}

	public void setFilterCategories(List<FilterObject> filterCategories)
	{
		this.filterCategories = filterCategories;
	}

	public List<FilterObject> getFilterTags()
	{
		return filterTags;
	}

	public void setFilterTags(List<FilterObject> filterTags)
	{
		this.filterTags = filterTags;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	private List<Integer> getIncludedIDs(List<FilterObject> objects)
	{
		if(objects == null)
		{
			return new ArrayList<>();
		}

		List<Integer> includedIDs = new ArrayList<>();
		for(FilterObject filterObject : objects)
		{
			if(filterObject.isInclude())
			{
				includedIDs.add(filterObject.getID());
			}
		}

		if(includedIDs.size() == objects.size())
		{
			return new ArrayList<>();
		}

		return includedIDs;
	}

	public List<Integer> getIncludedCategoryIDs()
	{
		return getIncludedIDs(filterCategories);
	}

	public List<Integer> getIncludedTagIDs()
	{
		return getIncludedIDs(filterTags);
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

		if(defaultConfiguration.isIncludeTransfer() != isIncludeTransfer())
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

		for(FilterObject filterCategory : filterCategories)
		{
			if(!filterCategory.isInclude())
			{
				return true;
			}
		}

		for(FilterObject filterTag : filterTags)
		{
			if(!filterTag.isInclude())
			{
				return true;
			}
		}

		if(!defaultConfiguration.getName().equals(name))
		{
			return true;
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
				", includeTransfer=" + includeTransfer +
				", includeRepeating=" + includeRepeating +
				", filterCategories=" + filterCategories +
				", filterTags=" + filterTags +
				", name='" + name + '\'' +
				'}';
	}
}
