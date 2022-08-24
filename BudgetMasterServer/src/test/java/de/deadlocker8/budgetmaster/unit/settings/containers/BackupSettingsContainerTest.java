package de.deadlocker8.budgetmaster.unit.settings.containers;

import de.deadlocker8.budgetmaster.backup.AutoBackupStrategy;
import de.deadlocker8.budgetmaster.backup.AutoBackupTime;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsController;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.settings.containers.BackupSettingsContainer;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@LocalizedTest
class BackupSettingsContainerTest
{
	@Mock
	private SettingsService settingsService;

	@Test
	void test_validate_valid()
	{
		final BackupSettingsContainer container = new BackupSettingsContainer(false, AutoBackupStrategy.LOCAL.getName(), 5, AutoBackupTime.TIME_03.name(), 2, null, null, null, null);

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		assertThat(errors.getAllErrors())
				.isEmpty();
	}

	@Test
	void test_validate_daysIsNull()
	{
		final BackupSettingsContainer container = new BackupSettingsContainer(false, AutoBackupStrategy.LOCAL.getName(), null, AutoBackupTime.TIME_03.name(), 2, null, null, null, null);

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		final List<ObjectError> finalErrors = errors.getAllErrors();
		assertThat(finalErrors)
				.hasSize(1);
		assertThat(finalErrors.get(0))
				.hasFieldOrPropertyWithValue("field", "autoBackupDays");
	}

	@Test
	void test_validate_autoBackupFilesToKeepIsNull()
	{
		final BackupSettingsContainer container = new BackupSettingsContainer(false, AutoBackupStrategy.LOCAL.getName(), 5, AutoBackupTime.TIME_03.name(), null, null, null, null, null);

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		final List<ObjectError> finalErrors = errors.getAllErrors();
		assertThat(finalErrors)
				.hasSize(1);
		assertThat(finalErrors.get(0))
				.hasFieldOrPropertyWithValue("field", "autoBackupFilesToKeep");
	}

	@Test
	void test_validate_gitBackup_autoBackupGitUrlIsNull()
	{
		final BackupSettingsContainer container = new BackupSettingsContainer(false, AutoBackupStrategy.GIT_REMOTE.getName(), 5, AutoBackupTime.TIME_03.name(), null, null, "master", "me", "superSecret");

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		final List<ObjectError> finalErrors = errors.getAllErrors();
		assertThat(finalErrors)
				.hasSize(1);
		assertThat(finalErrors.get(0))
				.hasFieldOrPropertyWithValue("field", "autoBackupGitUrl");
	}

	@Test
	void test_validate_gitBackup_autoBackupGitBranchNameIsNull()
	{
		final BackupSettingsContainer container = new BackupSettingsContainer(false, AutoBackupStrategy.GIT_REMOTE.getName(), 5, AutoBackupTime.TIME_03.name(), null, "http://repo:12345", null, "me", "superSecret");

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		final List<ObjectError> finalErrors = errors.getAllErrors();
		assertThat(finalErrors)
				.hasSize(1);
		assertThat(finalErrors.get(0))
				.hasFieldOrPropertyWithValue("field", "autoBackupGitBranchName");
	}

	@Test
	void test_validate_gitBackup_autoBackupGitUserNameIsNull()
	{
		final BackupSettingsContainer container = new BackupSettingsContainer(false, AutoBackupStrategy.GIT_REMOTE.getName(), 5, AutoBackupTime.TIME_03.name(), null, "http://repo:12345", "master", null, "superSecret");

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		final List<ObjectError> finalErrors = errors.getAllErrors();
		assertThat(finalErrors)
				.hasSize(1);
		assertThat(finalErrors.get(0))
				.hasFieldOrPropertyWithValue("field", "autoBackupGitUserName");
	}

	@Test
	void test_validate_gitBackup_autoBackupGitTokenIsNull()
	{
		final BackupSettingsContainer container = new BackupSettingsContainer(false, AutoBackupStrategy.GIT_REMOTE.getName(), 5, AutoBackupTime.TIME_03.name(), null, "http://repo:12345", "master", "me", null);

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		final List<ObjectError> finalErrors = errors.getAllErrors();
		assertThat(finalErrors)
				.hasSize(1);
		assertThat(finalErrors.get(0))
				.hasFieldOrPropertyWithValue("field", "autoBackupGitToken");
	}

	@Test
	void test_updateSettings_backupReminderActivatedIsNull()
	{
		final Settings defaultSettings = Settings.getDefault();

		Mockito.when(settingsService.getSettings()).thenReturn(defaultSettings);

		final BackupSettingsContainer container = new BackupSettingsContainer(null, AutoBackupStrategy.LOCAL.getName(), 1, AutoBackupTime.TIME_03.name(), 1, "", "", "", "");
		final Settings updatedSettings = container.updateSettings(settingsService);

		assertThat(updatedSettings.getBackupReminderActivated()).isFalse();
	}

	@Test
	void test_updateSettings_autoBackupStrategyIsNull()
	{
		final Settings defaultSettings = Settings.getDefault();

		Mockito.when(settingsService.getSettings()).thenReturn(defaultSettings);

		final BackupSettingsContainer container = new BackupSettingsContainer(false, null, 1, AutoBackupTime.TIME_03.name(), 1, "", "", "", "");
		final Settings updatedSettings = container.updateSettings(settingsService);

		assertThat(updatedSettings.getAutoBackupStrategy()).isEqualTo(AutoBackupStrategy.NONE);
	}

	@Test
	void test_updateSettings_gitRemote()
	{
		final Settings defaultSettings = Settings.getDefault();

		Mockito.when(settingsService.getSettings()).thenReturn(defaultSettings);

		final BackupSettingsContainer container = new BackupSettingsContainer(true, AutoBackupStrategy.GIT_REMOTE.getName(), 2, AutoBackupTime.TIME_03.name(), 1, "https://myrepo.git", "master", "bm", "bm0815");
		final Settings updatedSettings = container.updateSettings(settingsService);

		assertThat(updatedSettings)
				.hasFieldOrPropertyWithValue("backupReminderActivated", true)
				.hasFieldOrPropertyWithValue("autoBackupStrategy", AutoBackupStrategy.GIT_REMOTE)
				.hasFieldOrPropertyWithValue("autoBackupDays", 2)
				.hasFieldOrPropertyWithValue("autoBackupTime", AutoBackupTime.TIME_03)
				.hasFieldOrPropertyWithValue("autoBackupFilesToKeep", 1)
				.hasFieldOrPropertyWithValue("autoBackupGitUrl", "https://myrepo.git")
				.hasFieldOrPropertyWithValue("autoBackupGitBranchName", "master")
				.hasFieldOrPropertyWithValue("autoBackupGitUserName", "bm")
				.hasFieldOrPropertyWithValue("autoBackupGitToken", "bm0815");
	}

	@Test
	void test_updateSettings_gitTokenIsPlaceholder_keepPasswordFromSavedSettings()
	{
		final Settings settings = Settings.getDefault();
		settings.setAutoBackupGitToken("SuperSecretDeluxe");

		Mockito.when(settingsService.getSettings()).thenReturn(settings);

		final BackupSettingsContainer container = new BackupSettingsContainer(true, AutoBackupStrategy.GIT_REMOTE.getName(), 2, AutoBackupTime.TIME_03.name(), 1, "https://myrepo.git", "master", "bm", SettingsController.PASSWORD_PLACEHOLDER);
		final Settings updatedSettings = container.updateSettings(settingsService);

		assertThat(updatedSettings.getAutoBackupGitToken()).isEqualTo("SuperSecretDeluxe");
	}

	@Test
	void test_updateSettings_localFiles()
	{
		final Settings defaultSettings = Settings.getDefault();

		Mockito.when(settingsService.getSettings()).thenReturn(defaultSettings);

		final BackupSettingsContainer container = new BackupSettingsContainer(false, AutoBackupStrategy.LOCAL.getName(), 2, AutoBackupTime.TIME_03.name(), 15, "", "", "", "");
		final Settings updatedSettings = container.updateSettings(settingsService);

		assertThat(updatedSettings)
				.hasFieldOrPropertyWithValue("backupReminderActivated", false)
				.hasFieldOrPropertyWithValue("autoBackupStrategy", AutoBackupStrategy.LOCAL)
				.hasFieldOrPropertyWithValue("autoBackupDays", 2)
				.hasFieldOrPropertyWithValue("autoBackupTime", AutoBackupTime.TIME_03)
				.hasFieldOrPropertyWithValue("autoBackupFilesToKeep", 15)
				.hasFieldOrPropertyWithValue("autoBackupGitUrl", "")
				.hasFieldOrPropertyWithValue("autoBackupGitBranchName", "")
				.hasFieldOrPropertyWithValue("autoBackupGitUserName", "")
				.hasFieldOrPropertyWithValue("autoBackupGitToken", "");
	}

	@Test
	void test_updateSettings_none_cleanFields()
	{
		final Settings defaultSettings = Settings.getDefault();

		Mockito.when(settingsService.getSettings()).thenReturn(defaultSettings);

		final BackupSettingsContainer container = new BackupSettingsContainer(false, AutoBackupStrategy.NONE.getName(), 2, AutoBackupTime.TIME_03.name(), 1, "https://myrepo.git", "master", "bm", "bm0815");
		final Settings updatedSettings = container.updateSettings(settingsService);

		assertThat(updatedSettings)
				.hasFieldOrPropertyWithValue("backupReminderActivated", false)
				.hasFieldOrPropertyWithValue("autoBackupStrategy", AutoBackupStrategy.NONE)
				.hasFieldOrPropertyWithValue("autoBackupDays", 1)
				.hasFieldOrPropertyWithValue("autoBackupTime", AutoBackupTime.TIME_00)
				.hasFieldOrPropertyWithValue("autoBackupFilesToKeep", 3)
				.hasFieldOrPropertyWithValue("autoBackupGitUrl", "https://myrepo.git")
				.hasFieldOrPropertyWithValue("autoBackupGitBranchName", "master")
				.hasFieldOrPropertyWithValue("autoBackupGitUserName", "")
				.hasFieldOrPropertyWithValue("autoBackupGitToken", "");
	}
}
