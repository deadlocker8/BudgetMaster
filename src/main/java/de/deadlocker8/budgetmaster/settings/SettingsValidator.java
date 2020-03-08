package de.deadlocker8.budgetmaster.settings;

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
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "autoBackupDays", Strings.WARNING_EMPTY_NUMBER);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "autoBackupFilesToKeep", Strings.WARNING_EMPTY_NUMBER);
	}
}