package de.deadlocker8.budgetmaster.search;

public class Search
{
	private String searchText;
	private boolean searchName;
	private boolean searchDescription;
	private boolean searchCategory;
	private boolean searchTags;

	public Search()
	{
	}

	public Search(String searchText, boolean searchName, boolean searchDescription, boolean searchCategory, boolean searchTags)
	{
		this.searchText = searchText;
		this.searchName = searchName;
		this.searchDescription = searchDescription;
		this.searchCategory = searchCategory;
		this.searchTags = searchTags;
	}

	public String getSearchText()
	{
		return searchText;
	}

	public void setSearchText(String searchText)
	{
		this.searchText = searchText;
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

	public boolean isSearchCategory()
	{
		return searchCategory;
	}

	public void setSearchCategory(boolean searchCatgeory)
	{
		this.searchCategory = searchCatgeory;
	}

	public boolean isSearchTags()
	{
		return searchTags;
	}

	public void setSearchTags(boolean searchTags)
	{
		this.searchTags = searchTags;
	}

	@Override
	public String toString()
	{
		return "Search{" +
				"searchText='" + searchText + '\'' +
				", searchName=" + searchName +
				", searchDescription=" + searchDescription +
				", searchCategory=" + searchCategory +
				", searchTags=" + searchTags +
				'}';
	}
}
