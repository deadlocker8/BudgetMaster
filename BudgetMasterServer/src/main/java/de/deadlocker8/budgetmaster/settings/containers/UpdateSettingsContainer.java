package de.deadlocker8.budgetmaster.settings.containers;

import org.springframework.validation.Errors;

public final class UpdateSettingsContainer implements SettingsContainer
{
	private Boolean autoUpdateCheckEnabled;

	public UpdateSettingsContainer(Boolean autoUpdateCheckEnabled)
	{
		this.autoUpdateCheckEnabled = autoUpdateCheckEnabled;
	}

	public Boolean getAutoUpdateCheckEnabled()
	{
		return autoUpdateCheckEnabled;
	}

	@Override
	public void validate(Errors errors)
	{
		// nothing to do
	}

	@Override
	public void fixBooleans()
	{
		if(autoUpdateCheckEnabled == null)
		{
			autoUpdateCheckEnabled = false;
		}
	}
}
