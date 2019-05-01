package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class EmptyPageController extends BaseController
{
	private final SettingsService settingsService;

	@Autowired
	public EmptyPageController(SettingsService settingsService)
	{
		this.settingsService = settingsService;
	}

	@RequestMapping("/charts")
	public String charts(Model model)
	{
		model.addAttribute("active", "charts");
		model.addAttribute("settings", settingsService.getSettings());
		return "placeholder/comingSoon";
	}
}