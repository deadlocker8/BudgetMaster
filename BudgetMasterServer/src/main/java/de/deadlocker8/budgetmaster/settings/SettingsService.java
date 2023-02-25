package de.deadlocker8.budgetmaster.settings;

import de.deadlocker8.budgetmaster.authentication.User;
import de.deadlocker8.budgetmaster.authentication.UserRepository;
import de.deadlocker8.budgetmaster.utils.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SettingsService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SettingsService.class);

	private final SettingsRepository settingsRepository;
	private final UserRepository userRepository;

	@Autowired
	public SettingsService(SettingsRepository settingsRepository, UserRepository userRepository)
	{
		this.settingsRepository = settingsRepository;
		this.userRepository = userRepository;
	}

	@PostConstruct
	public void postInit()
	{
		this.createDefaultSettingsIfNotExists();
	}

	public void createDefaultSettingsIfNotExists()
	{
		if(settingsRepository.findById(1).isEmpty())
		{
			settingsRepository.save(Settings.getDefault());
			LOGGER.debug("Created default settings");
		}

		Settings defaultSettings = Settings.getDefault();
		Optional<Settings> settingsOptional = settingsRepository.findById(1);
		if(settingsOptional.isEmpty())
		{
			throw new NoSuchElementException("Missing Settings in database");
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
		if(settings.getAutoBackupStrategy() == null)
		{
			settings.setAutoBackupStrategy(defaultSettings.getAutoBackupStrategy());
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
		if(settings.getAutoBackupGitUrl() == null)
		{
			settings.setAutoBackupGitUrl(defaultSettings.getAutoBackupGitUrl());
		}
		if(settings.getAutoBackupGitBranchName() == null)
		{
			settings.setAutoBackupGitBranchName(defaultSettings.getAutoBackupGitBranchName());
		}
		if(settings.getAutoBackupGitUserName() == null)
		{
			settings.setAutoBackupGitUserName(defaultSettings.getAutoBackupGitUserName());
		}
		if(settings.getAutoBackupGitToken() == null)
		{
			settings.setAutoBackupGitToken(defaultSettings.getAutoBackupGitToken());
		}
		if(settings.getInstalledVersionCode() == null)
		{
			settings.setInstalledVersionCode(defaultSettings.getInstalledVersionCode());
		}
		if(settings.getWhatsNewShownForCurrentVersion() == null)
		{
			settings.setWhatsNewShownForCurrentVersion(defaultSettings.getWhatsNewShownForCurrentVersion());
		}
		if(settings.getShowCategoriesAsCircles() == null)
		{
			settings.setShowCategoriesAsCircles(defaultSettings.getShowCategoriesAsCircles());
		}
		if(settings.getMigrationDeclined() == null)
		{
			settings.setMigrationDeclined(defaultSettings.getMigrationDeclined());
		}

		settingsRepository.save(settings);
	}

	public Settings getSettings()
	{
		return settingsRepository.findById(1).orElseThrow();
	}

	@Transactional
	public void updateLastBackupReminderDate()
	{
		Settings settings = getSettings();
		settings.setLastBackupReminderDate(DateHelper.getCurrentDate());
	}

	@Transactional
	public void updateMigrationDeclined(boolean isDeclined)
	{
		Settings settings = getSettings();
		settings.setMigrationDeclined(isDeclined);
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

	public void savePassword(String password)
	{
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String encryptedPassword = bCryptPasswordEncoder.encode(password);
		User user = userRepository.findByName("Default");
		user.setPassword(encryptedPassword);
		userRepository.save(user);
	}
}