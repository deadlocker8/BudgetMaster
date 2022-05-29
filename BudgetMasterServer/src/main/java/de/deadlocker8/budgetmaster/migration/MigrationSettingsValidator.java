package de.deadlocker8.budgetmaster.migration;

import de.deadlocker8.budgetmaster.utils.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


public class MigrationSettingsValidator implements Validator
{
	public boolean supports(Class clazz)
	{
		return MigrationSettings.class.equals(clazz);
	}

	public void validate(Object obj, Errors errors)
	{
		final MigrationSettings migrationSettings = (MigrationSettings) obj;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "hostname", Strings.WARNING_EMPTY_MIGRATION_HOSTNAME);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "port", Strings.WARNING_EMPTY_MIGRATION_PORT);

		final Integer port = migrationSettings.port();
		if(port != null && (port < 1 || port > 65535))
		{
			errors.rejectValue("port", Strings.WARNING_EMPTY_MIGRATION_PORT);
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "databaseName", Strings.WARNING_EMPTY_MIGRATION_DATABASE_NAME);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", Strings.WARNING_EMPTY_MIGRATION_USERNAME);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", Strings.WARNING_EMPTY_MIGRATION_PASSWORD);
	}
}