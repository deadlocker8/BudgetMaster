package de.deadlocker8.budgetmaster.settings.containers;

import org.springframework.validation.Errors;

public final class TransactionsSettingsContainer implements SettingsContainer
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

	@Override
	public void validate(Errors errors)
	{
		// nothing to do
	}

	@Override
	public void fixBooleans()
	{
		if(restActivated == null)
		{
			restActivated = false;
		}
	}
}
