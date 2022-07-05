package de.deadlocker8.budgetmaster.unit.settings.containers;

import de.deadlocker8.budgetmaster.backup.AutoBackupStrategy;
import de.deadlocker8.budgetmaster.backup.AutoBackupTime;
import de.deadlocker8.budgetmaster.settings.containers.BackupSettingsContainer;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
}
