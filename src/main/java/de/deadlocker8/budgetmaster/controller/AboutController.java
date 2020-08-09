package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class AboutController extends BaseController
{
	private final SettingsService settingsService;

	@Autowired
	public AboutController(SettingsService settingsService)
	{
		this.settingsService = settingsService;
	}

	@RequestMapping(Mappings.ABOUT)
	public String index(Model model)
	{
		model.addAttribute("settings", settingsService.getSettings());
		return "about";
	}
}