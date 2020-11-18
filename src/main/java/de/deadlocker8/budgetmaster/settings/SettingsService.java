package de.deadlocker8.budgetmaster.settings;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Optional;

@Service
public class SettingsService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SettingsService.class);
	private final SettingsRepository settingsRepository;

	@Autowired
	private SettingsService settingsService;

	@Autowired
	public SettingsService(SettingsRepository settingsRepository)
	{
		this.settingsRepository = settingsRepository;
	}

	@PostConstruct
	public void postInit()
	{
		this.settingsService.createDefaultSettingsIfNotExists();
	}

	@Transactional
	public void createDefaultSettingsIfNotExists()
	{
		if(settingsRepository.findById(0).isEmpty())
		{
			settingsRepository.save(Settings.getDefault());
			LOGGER.debug("Created default settings");
		}

		Settings defaultSettings = Settings.getDefault();
		Optional<Settings> settingsOptional = settingsRepository.findById(0);
		if(settingsOptional.isEmpty())
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
		if(settings.getAutoBackupDays() == null)
		{
			settings.setAutoBackupDays(defaultSettings.getAutoBackupDays());
		}
		if(settings.getAutoBackupTime() == null)
		{
			settings.setAutoBackupTime(defaultSettings.getAutoBackupTime());
		}
		if(settings.getAutoBackupFilesToKeep() == null)
		{
			settings.setAutoBackupFilesToKeep(defaultSettings.getAutoBackupFilesToKeep());
		}
		if(settings.getInstalledVersionCode() == null)
		{
			settings.setInstalledVersionCode(defaultSettings.getInstalledVersionCode());
		}
		if(settings.getWhatsNewShownForCurrentVersion() == null)
		{
			settings.setWhatsNewShownForCurrentVersion(defaultSettings.getWhatsNewShownForCurrentVersion());
		}
		if(settings.getShowFirstUseBanner() == null)
		{
			settings.setShowFirstUseBanner(defaultSettings.getShowFirstUseBanner());
		}
	}

	@SuppressWarnings("OptionalGetWithoutIsPresent")
	public Settings getSettings()
	{
		return settingsRepository.findById(0).get();
	}

	@Transactional
	public void updateLastBackupReminderDate()
	{
		Settings settings = getSettings();
		settings.setLastBackupReminderDate(DateTime.now());
	}

	@Transactional
	public void disableFirstUseBanner()
	{
		Settings settings = getSettings();
		settings.setShowFirstUseBanner(false);
	}

	@Transactional
	public void updateSettings(Settings newSettings)
	{
		final Settings settings = getSettings();

		for(Field declaredField : Settings.class.getDeclaredFields())
		{
			declaredField.setAccessible(true);

			try
			{
				// Update database object
				declaredField.set(settings, declaredField.get(newSettings));
			}
			catch(IllegalAccessException e)
			{
				LOGGER.error("Error copying settings data", e);
			}
		}
	}
}