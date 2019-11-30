package de.deadlocker8.budgetmaster.settings;

import de.deadlocker8.budgetmaster.charts.ChartRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
		if(!settingsRepository.findById(0).isPresent())
		{
			settingsRepository.save(Settings.getDefault());
			LOGGER.debug("Created default settings");
		}

		Settings defaultSettings = Settings.getDefault();
		Optional<Settings> settingsOptional = settingsRepository.findById(0);
		if(!settingsOptional.isPresent())
		{
			throw new RuntimeException("Missing Settings in database");
		}

		Settings settings = settingsOptional.get();
		if(settings.getBackupReminderActivated() == null)
		{
			settings.setBackupReminderActivated(defaultSettings.getBackupReminderActivated());
		}
		if(settings.getLastBackupReminderDate() == null)
		{
			settings.setLastBackupReminderDate(defaultSettings.getLastBackupReminderDate());
		}
		if(settings.getSearchItemsPerPage() == null)
		{
			settings.setSearchItemsPerPage(defaultSettings.getSearchItemsPerPage());
		}
		if(settings.getAutoBackupActivated() == null)
		{
			settings.setAutoBackupActivated(defaultSettings.getAutoBackupActivated());
		}
		settingsRepository.deleteById(0);
		settingsRepository.save(settings);
	}

	@SuppressWarnings("OptionalGetWithoutIsPresent")
	public Settings getSettings()
	{
		return settingsRepository.findById(0).get();
	}

	public void updateLastBackupReminderDate()
	{
		Settings settings = getSettings();
		settings.setLastBackupReminderDate(DateTime.now());
		settingsRepository.deleteById(0);
		settingsRepository.save(settings);
	}

	public SettingsRepository getRepository()
	{
		return settingsRepository;
	}
}