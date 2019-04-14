package de.deadlocker8.budgetmaster.search;

public class Search
{
	private String searchText;

	public Search()
	{
	}

	public String getSearchText()
	{
		return searchText;
	}

	public void setSearchText(String searchText)
	{
		this.searchText = searchText;
	}

	@Override
	public String toString()
	{
		return "Search{" +
				"searchText='" + searchText + '\'' +
				'}';
	}
}
