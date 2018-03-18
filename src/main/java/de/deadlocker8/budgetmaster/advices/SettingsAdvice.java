package de.deadlocker8.budgetmaster.advices;

import de.deadlocker8.budgetmaster.entities.Settings;
import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SettingsAdvice
{
	@Autowired
	private SettingsRepository settingsRepository;

	@ModelAttribute("settings")
	public Settings getSettings()
	{
		return settingsRepository.findOne(0);
	}
}


