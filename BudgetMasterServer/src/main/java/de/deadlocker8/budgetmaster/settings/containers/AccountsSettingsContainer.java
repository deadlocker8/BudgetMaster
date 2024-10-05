package de.deadlocker8.budgetmaster.settings.containers;

import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.springframework.validation.Errors;

public final class AccountsSettingsContainer implements SettingsContainer
{
	private Boolean accountEndDateReminderActivated;

	public AccountsSettingsContainer(Boolean accountEndDateReminderActivated)
	{
		this.accountEndDateReminderActivated = accountEndDateReminderActivated;
	}

	@Override
	public void validate(Errors errors)
	{
		// nothing to do
	}

	@Override
	public void fixBooleans()
	{
		// nothing to do
	}

	@Override
	public String getErrorLocalizationKey()
	{
		return "notification.settings.accounts.error";
	}

	@Override
	public String getSuccessLocalizationKey()
	{
		return "notification.settings.accounts.saved";
	}

	@Override
	public String getTemplatePath()
	{
		return "settings/containers/settingsAccounts";
	}

	@Override
	public Settings updateSettings(SettingsService settingsService)
	{
		final Settings settings = settingsService.getSettings();

		if(accountEndDateReminderActivated == null)
		{
			accountEndDateReminderActivated = false;
		}

		settings.setAccountEndDateReminderActivated(accountEndDateReminderActivated);
		return settings;
	}

	@Override
	public void persistChanges(SettingsService settingsService, Settings previousSettings, Settings settings)
	{
		settingsService.updateSettings(settings);
	}
}
