package de.deadlocker8.budgetmaster.templates;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;


@Controller
public class TemplateController extends BaseController
{
	private final TemplateService templateService;
	private final SettingsService settingsService;

	@Autowired
	public TemplateController(TemplateService templateService, SettingsService settingsService)
	{
		this.templateService = templateService;
		this.settingsService = settingsService;
	}

	@GetMapping("/templates")
	public String showTemplates(Model model)
	{
		model.addAttribute("settings", settingsService.getSettings());
		model.addAttribute("templates", templateService.getRepository().findAllByOrderByTemplateNameAsc());
		return "templates/templates";
	}

	@GetMapping("/templates/manage")
	public String manage(Model model)
	{
		model.addAttribute("settings", settingsService.getSettings());
		model.addAttribute("templates", templateService.getRepository().findAllByOrderByTemplateNameAsc());
		return "templates/manage";
	}
}