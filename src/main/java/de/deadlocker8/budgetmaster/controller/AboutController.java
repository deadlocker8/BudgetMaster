package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class AboutController extends BaseController
{
	@Autowired
	private SettingsService settingsService;

	@RequestMapping("/about")
	public String index(Model model)
	{
		model.addAttribute("settings", settingsService.getSettings());
		return "about";
	}
}