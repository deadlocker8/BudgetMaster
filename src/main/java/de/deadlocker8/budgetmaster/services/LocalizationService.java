package de.deadlocker8.budgetmaster.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.Localization;

@Service
public class LocalizationService
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public LocalizationService(SettingsService settingsService)
	{
		Localization.init("languages/");
		Localization.loadLanguage(settingsService.getSettings().getLanguage().getLocale());
	}
}