package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IndexController extends BaseController
{
	private final SettingsService settingsService;

	@Autowired
	public IndexController(SettingsService settingsService)
	{
		this.settingsService = settingsService;
	}

	@RequestMapping
	public String index(Model model)
	{
		model.addAttribute("settings", settingsService.getSettings());
		return "index";
	}

	@GetMapping("/firstUse")
	public String firstUse(Model model)
	{
		model.addAttribute("settings", settingsService.getSettings());
		return "firstUse";
	}
}