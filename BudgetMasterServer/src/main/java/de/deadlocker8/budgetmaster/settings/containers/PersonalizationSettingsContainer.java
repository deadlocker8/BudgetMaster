package de.deadlocker8.budgetmaster.settings.containers;

import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.utils.LanguageType;
import org.springframework.validation.Errors;

public final class PersonalizationSettingsContainer implements SettingsContainer
{
	private final String language;
	private final String currency;
	private Boolean useDarkTheme;
	private Boolean showCategoriesAsCircles;
	private final Integer searchItemsPerPage;

	public PersonalizationSettingsContainer(String language, String currency, Boolean useDarkTheme,
											Boolean showCategoriesAsCircles, Integer searchItemsPerPage)
	{
		this.language = language;
		this.currency = currency;
		this.useDarkTheme = useDarkTheme;
		this.showCategoriesAsCircles = showCategoriesAsCircles;
		this.searchItemsPerPage = searchItemsPerPage;
	}

	private LanguageType getLanguageType()
	{
		return LanguageType.fromName(language);
	}

	@Override
	public void validate(Errors errors)
	{
		// nothing to do
	}

	@Override
	public void fixBooleans()
	{
		if(useDarkTheme == null)
		{
			useDarkTheme = false;
		}

		if(showCategoriesAsCircles == null)
		{
			showCategoriesAsCircles = false;
		}
	}

	@Override
	public String getErrorLocalizationKey()
	{
		return "notification.settings.personalization.error";
	}

	@Override
	public String getSuccessLocalizationKey()
	{
		return "notification.settings.personalization.saved";
	}

	@Override
	public String getTemplatePath()
	{
		return "settings/containers/settingsPersonalization";
	}

	@Override
	public Settings updateSettings(SettingsService settingsService)
	{
		final Settings settings = settingsService.getSettings();

		settings.setLanguage(getLanguageType());
		settings.setCurrency(currency);
		settings.setUseDarkTheme(useDarkTheme);
		settings.setShowCategoriesAsCircles(showCategoriesAsCircles);
		settings.setSearchItemsPerPage(searchItemsPerPage);

		return settings;
	}

	@Override
	public void persistChanges(SettingsService settingsService, Settings previousSettings, Settings settings)
	{
		settingsService.updateSettings(settings);
	}
}
