package de.deadlocker8.budgetmaster.services;

import de.thecodelabs.utils.util.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocalizationService implements  Localization.LocalizationDelegate
{
	private SettingsService settingsService;

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
	public String getBaseResource()
	{
		return "languages/";
	}

	@Override
	public boolean useMessageFormatter()
	{
		return true;
	}
}