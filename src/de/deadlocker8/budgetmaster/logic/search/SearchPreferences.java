package de.deadlocker8.budgetmaster.logic.search;

public class SearchPreferences
{
	private String lastQuery;
	private boolean searchName;
	private boolean searchDescription;
	private boolean searchCategorNames;
	private boolean searchTags;
	private boolean searchAmount;
	private int minAmount;
	private int maxAmount;
	
	public SearchPreferences()
	{
		
	}

	public SearchPreferences(String lastQuery, boolean searchName, boolean searchDescription, boolean searchCategorNames, boolean searchTags, boolean searchAmount, int minAmount, int maxAmount)
	{
		this.lastQuery = lastQuery;
		this.searchName = searchName;
		this.searchDescription = searchDescription;
		this.searchCategorNames = searchCategorNames;
		this.searchTags = searchTags;
		this.searchAmount = searchAmount;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
	}
	
	public String getLastQuery()
	{
		return lastQuery;
	}

	public void setLastQuery(String lastQuery)
	{
		this.lastQuery = lastQuery;
	}

	public boolean isSearchName()
	{
		return searchName;
	}

	public void setSearchName(boolean searchName)
	{
		this.searchName = searchName;
	}

	public boolean isSearchDescription()
	{
		return searchDescription;
	}

	public void setSearchDescription(boolean searchDescription)
	{
		this.searchDescription = searchDescription;
	}

	public boolean isSearchCategorNames()
	{
		return searchCategorNames;
	}

	public void setSearchCategorNames(boolean searchCategorNames)
	{
		this.searchCategorNames = searchCategorNames;
	}

	public boolean isSearchTags()
	{
		return searchTags;
	}

	public void setSearchTags(boolean searchTags)
	{
		this.searchTags = searchTags;
	}

	public boolean isSearchAmount()
	{
		return searchAmount;
	}

	public void setSearchAmount(boolean searchAmount)
	{
		this.searchAmount = searchAmount;
	}

	public int getMinAmount()
	{
		return minAmount;
	}

	public void setMinAmount(int minAmount)
	{
		this.minAmount = minAmount;
	}

	public int getMaxAmount()
	{
		return maxAmount;
	}
	
	public void setMaxAmount(int maxAmount)
	{
		this.maxAmount = maxAmount;
	}

	public void searchPreferences(int maxAmount)
	{
		this.maxAmount = maxAmount;
	}
	
	@Override
	public String toString()
	{
		return "SearchPreferences [lastQuery=" + lastQuery + ", searchName=" + searchName + ", searchDescription=" + searchDescription + ", searchCategorNames=" + searchCategorNames + ", searchTags=" + searchTags + ", searchAmount=" + searchAmount + ", minAmount=" + minAmount + ", maxAmount="
				+ maxAmount + "]";
	}
}