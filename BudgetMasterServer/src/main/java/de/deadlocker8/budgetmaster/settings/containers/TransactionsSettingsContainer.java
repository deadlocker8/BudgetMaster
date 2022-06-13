package de.deadlocker8.budgetmaster.settings.containers;

public final class TransactionsSettingsContainer
{
	private Boolean restActivated;

	public TransactionsSettingsContainer(Boolean restActivated)
	{
		this.restActivated = restActivated;
	}

	public Boolean getRestActivated()
	{
		return restActivated;
	}

	public void fixBooleans()
	{
		if(restActivated == null)
		{
			restActivated = false;
		}
	}
}
