package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.entities.Settings;
import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private SettingsRepository settingsRepository;

	@Autowired
	public SettingsService(SettingsRepository settingsRepository)
	{
		this.settingsRepository = settingsRepository;
		createDefaultSettingsIfNotExists();
	}

	private void createDefaultSettingsIfNotExists()
	{
		if(settingsRepository.findOne(0) == null)
		{
			settingsRepository.save(Settings.getDefault());
			LOGGER.debug("Created default settings");
		}
	}

	public Settings getSettings()
	{
		return settingsRepository.findOne(0);
	}
}