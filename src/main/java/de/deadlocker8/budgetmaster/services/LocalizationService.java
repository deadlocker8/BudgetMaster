package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.thecodelabs.utils.util.Localization;
import de.thecodelabs.utils.util.localization.LocalizationMessageFormatter;
import de.thecodelabs.utils.util.localization.formatter.JavaMessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocalizationService implements  Localization.LocalizationDelegate
{
	private final SettingsService settingsService;

	@Autowired
	public LocalizationService(SettingsService settingsService)
	{
		this.settingsService = settingsService;

		Localization.setDelegate(this);
		Localization.load();
	}

	@Override
	public Locale getLocale()
	{
		return settingsService.getSettings().getLanguage().getLocale();
	}

	@Override
	public String[] getBaseResources()
	{
		return new String[]{"languages/base", "languages/news", "languages/hints"};
	}

	@Override
	public LocalizationMessageFormatter messageFormatter()
	{
		return new JavaMessageFormatter();
	}

	@Override
	public boolean useMultipleResourceBundles()
	{
		return true;
	}
}