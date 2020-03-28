package de.deadlocker8.budgetmaster.templates;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class TemplateController extends BaseController
{
	private final SettingsService settingsService;

	@Autowired
	public TemplateController(SettingsService settingsService)
	{
		this.settingsService = settingsService;
	}

	@GetMapping("/templates")
	public String index(Model model)
	{
		model.addAttribute("settings", settingsService.getSettings());
		return "templates/manage";
	}
}