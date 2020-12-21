package de.deadlocker8.budgetmaster.settings;

import de.deadlocker8.budgetmaster.backup.AutoBackupStrategy;
import de.deadlocker8.budgetmaster.utils.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


public class SettingsValidator implements Validator
{
	public boolean supports(Class clazz)
	{
		return Settings.class.equals(clazz);
	}

	public void validate(Object obj, Errors errors)
	{
		final Settings settings = (Settings) obj;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "autoBackupDays", Strings.WARNING_EMPTY_NUMBER);

		if(settings.getAutoBackupStrategy() == AutoBackupStrategy.LOCAL)
		{
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "autoBackupFilesToKeep", Strings.WARNING_EMPTY_NUMBER_ZERO_ALLOWED);
		}

		if(settings.getAutoBackupStrategy() == AutoBackupStrategy.GIT_REMOTE)
		{
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "autoBackupGitUrl", Strings.WARNING_EMPTY_GIT_URL);
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "autoBackupGitUserName", Strings.WARNING_EMPTY_GIT_USER_NAME);
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "autoBackupGitPassword", Strings.WARNING_EMPTY_GIT_PASSWORD);
		}
	}
}