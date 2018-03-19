package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.entities.Settings;
import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.Localization;

import java.util.Locale;

@Service
public class SettingsService
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public SettingsService(SettingsRepository settingsRepository)
	{
		if(settingsRepository.findOne(0) == null)
		{
			settingsRepository.save(Settings.getDefault());
			LOGGER.debug("Created default settings");
		}

		Localization.init("languages/");
		Localization.loadLanguage(settingsRepository.findOne(0).getLanguage().getLocale());
	}
}