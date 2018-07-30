package de.deadlocker8.budgetmaster.database.accountmatches;

import java.util.List;

public class AccountMatchList
{
	private List<AccountMatch> accountMatches;

	public AccountMatchList()
	{
	}

	public AccountMatchList(List<AccountMatch> accountMatches)
	{
		this.accountMatches = accountMatches;
	}

	public List<AccountMatch> getAccountMatches()
	{
		return accountMatches;
	}

	public void setAccountMatches(List<AccountMatch> accountMatches)
	{
		this.accountMatches = accountMatches;
	}

	@Override
	public String toString()
	{
		return "AccountMatchList{" +
				"accountMatches=" + accountMatches +
				'}';
	}
}