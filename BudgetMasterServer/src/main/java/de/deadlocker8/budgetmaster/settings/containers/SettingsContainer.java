package de.deadlocker8.budgetmaster.settings.containers;

import org.springframework.validation.Errors;

public interface SettingsContainer
{
	void validate(Errors errors);

	void fixBooleans();
}
