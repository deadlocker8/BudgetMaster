package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.entities.Settings;
import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import de.thecodelabs.logger.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService
{
	private SettingsRepository settingsRepository;

	@Autowired
	public SettingsService(SettingsRepository settingsRepository)
	{
		this.settingsRepository = settingsRepository;
		createDefaultSettingsIfNotExists();
	}

	public void createDefaultSettingsIfNotExists()
	{
		if(settingsRepository.findOne(0) == null)
		{
			settingsRepository.save(Settings.getDefault());
			Logger.debug("Created default settings");
		}
	}

	public Settings getSettings()
	{
		return settingsRepository.findOne(0);
	}
}