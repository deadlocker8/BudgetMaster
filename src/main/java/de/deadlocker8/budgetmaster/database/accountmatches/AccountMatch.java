package de.deadlocker8.budgetmaster.database.accountmatches;

import de.deadlocker8.budgetmaster.entities.account.Account;

public class AccountMatch
{
	private Account accountSource;
	private Account accountDestination;

	public AccountMatch()
	{
	}

	public AccountMatch(Account accountSource)
	{
		this.accountSource = accountSource;
	}

	public Account getAccountSource()
	{
		return accountSource;
	}

	public void setAccountSource(Account accountSource)
	{
		this.accountSource = accountSource;
	}

	public Account getAccountDestination()
	{
		return accountDestination;
	}

	public void setAccountDestination(Account accountDestination)
	{
		this.accountDestination = accountDestination;
	}

	@Override
	public String toString()
	{
		return "AccountMatch{" +
				"accountSource=" + accountSource.getName() +
				", accountDestination=" + accountDestination.getName() +
				'}';
	}
}