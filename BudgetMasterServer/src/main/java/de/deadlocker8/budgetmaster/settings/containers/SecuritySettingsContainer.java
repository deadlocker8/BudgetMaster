package de.deadlocker8.budgetmaster.settings.containers;

import de.deadlocker8.budgetmaster.utils.Strings;
import org.springframework.validation.Errors;

public record SecuritySettingsContainer(String password, String passwordConfirmation) implements SettingsContainer
{
	@Override
	public void validate(Errors errors)
	{
		if(password == null || password.equals(""))
		{
			errors.rejectValue("password", Strings.WARNING_SETTINGS_PASSWORD_EMPTY, Strings.WARNING_SETTINGS_PASSWORD_EMPTY);
			return;
		}
		else if(password.length() < 3)
		{
			errors.rejectValue("password", Strings.WARNING_SETTINGS_PASSWORD_LENGTH, Strings.WARNING_SETTINGS_PASSWORD_LENGTH);
			return;
		}

		if(passwordConfirmation == null || passwordConfirmation.equals(""))
		{
			errors.rejectValue("passwordConfirmation", Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_EMPTY, Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_EMPTY);
			return;
		}

		if(!password.equals(passwordConfirmation))
		{
			errors.rejectValue("passwordConfirmation", Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_WRONG, Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_WRONG);
		}
	}

	@Override
	public void fixBooleans()
	{
		// nothing to do
	}
}
