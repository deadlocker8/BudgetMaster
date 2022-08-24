package de.deadlocker8.budgetmaster.settings.containers;

import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.springframework.validation.Errors;

public interface SettingsContainer
{
	void validate(Errors errors);

	void fixBooleans();

	String getErrorLocalizationKey();

	String getSuccessLocalizationKey();

	String getTemplatePath();

	Settings updateSettings(SettingsService settingsService);

	void persistChanges(SettingsService settingsService, Settings previousSettings,  Settings settings);
}
