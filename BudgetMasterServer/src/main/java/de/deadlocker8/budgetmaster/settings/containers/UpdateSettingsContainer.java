package de.deadlocker8.budgetmaster.settings.containers;

public final class UpdateSettingsContainer
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

	public void fixBooleans()
	{
		if(autoUpdateCheckEnabled == null)
		{
			autoUpdateCheckEnabled = false;
		}
	}
}
