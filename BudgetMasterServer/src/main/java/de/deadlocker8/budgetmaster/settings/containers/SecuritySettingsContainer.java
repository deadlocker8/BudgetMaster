package de.deadlocker8.budgetmaster.settings.containers;

import de.deadlocker8.budgetmaster.utils.Strings;
import org.springframework.validation.FieldError;

import java.util.Optional;

public record SecuritySettingsContainer(String password, String passwordConfirmation)
{
	public Optional<FieldError> validate()
	{
		if(password == null || password.equals(""))
		{
			return Optional.of(new FieldError("Settings", "password", password, false, new String[]{Strings.WARNING_SETTINGS_PASSWORD_EMPTY}, null, Strings.WARNING_SETTINGS_PASSWORD_EMPTY));
		}
		else if(password.length() < 3)
		{
			return Optional.of(new FieldError("Settings", "password", password, false, new String[]{Strings.WARNING_SETTINGS_PASSWORD_LENGTH}, null, Strings.WARNING_SETTINGS_PASSWORD_LENGTH));
		}

		if(passwordConfirmation == null || passwordConfirmation.equals(""))
		{
			return Optional.of(new FieldError("Settings", "passwordConfirmation", passwordConfirmation, false, new String[]{Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_EMPTY}, null, Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_EMPTY));
		}

		if(!password.equals(passwordConfirmation))
		{
			return Optional.of(new FieldError("Settings", "passwordConfirmation", passwordConfirmation, false, new String[]{Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_WRONG}, null, Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_WRONG));
		}

		return Optional.empty();
	}
}
