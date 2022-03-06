package de.deadlocker8.budgetmaster.search;

public class Search
{
	public static final Search DEFAULT = new Search("", true, true, true, true, false, 0);

	private String searchText;
	private boolean searchName;
	private boolean searchDescription;
	private boolean searchCategory;
	private boolean searchTags;
	private boolean includeHiddenAccounts;
	private int page;

	public Search()
	{
		this.searchText = "";
		this.page = 0;
	}

	public Search(String searchText, boolean searchName, boolean searchDescription, boolean searchCategory, boolean searchTags, boolean includeHiddenAccounts, int page)
	{
		this.searchText = searchText;
		this.searchName = searchName;
		this.searchDescription = searchDescription;
		this.searchCategory = searchCategory;
		this.searchTags = searchTags;
		this.includeHiddenAccounts = includeHiddenAccounts;
		this.page = page;
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

	public void setSearchCategory(boolean searchCategory)
	{
		this.searchCategory = searchCategory;
	}

	public boolean isSearchTags()
	{
		return searchTags;
	}

	public void setSearchTags(boolean searchTags)
	{
		this.searchTags = searchTags;
	}

	public boolean isIncludeHiddenAccounts()
	{
		return includeHiddenAccounts;
	}

	public void setIncludeHiddenAccounts(boolean includeHiddenAccounts)
	{
		this.includeHiddenAccounts = includeHiddenAccounts;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public boolean isEmptySearch(){
		return !searchName && !searchDescription && !searchCategory && !searchTags;
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
				", includeHiddenAccounts=" + includeHiddenAccounts +
				", page=" + page +
				'}';
	}
}
