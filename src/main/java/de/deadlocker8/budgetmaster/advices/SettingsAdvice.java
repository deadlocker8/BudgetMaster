package de.deadlocker8.budgetmaster.advices;

import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@ControllerAdvice
public class SettingsAdvice
{
	@Autowired
	private SettingsRepository settingsRepository;

	@SuppressWarnings("OptionalGetWithoutIsPresent")
	@ModelAttribute("settings")
	public Settings getSettings()
	{
		Optional<Settings> settingsOptional = settingsRepository.findById(0);
		return settingsOptional.get();
	}
}


