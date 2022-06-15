package de.deadlocker8.budgetmaster.settings.containers;

import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.springframework.validation.Errors;

public final class UpdateSettingsContainer implements SettingsContainer
{
	private Boolean autoUpdateCheckEnabled;

	public UpdateSettingsContainer(Boolean autoUpdateCheckEnabled)
	{
		this.autoUpdateCheckEnabled = autoUpdateCheckEnabled;
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

	@Override
	public String getErrorLocalizationKey()
	{
		return "notification.settings.update.error";
	}

	@Override
	public String getSuccessLocalizationKey()
	{
		return "notification.settings.update.saved";
	}

	@Override
	public String getTemplatePath()
	{
		return "settings/containers/settingsUpdate";
	}

	@Override
	public Settings updateSettings(SettingsService settingsService)
	{
		final Settings settings = settingsService.getSettings();

		settings.setAutoUpdateCheckEnabled(autoUpdateCheckEnabled);

		return settings;
	}

	@Override
	public void persistChanges(SettingsService settingsService, Settings previousSettings,  Settings settings)
	{
		settingsService.updateSettings(settings);
	}
}
