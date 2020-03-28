package de.deadlocker8.budgetmaster.templates;

import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.charts.ChartType;
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
	private final SettingsService settingsService;

	@Autowired
	public TemplateController(SettingsService settingsService)
	{
		this.settingsService = settingsService;
	}

	@GetMapping("/templates")
	public String showTemplates(Model model)
	{
		model.addAttribute("settings", settingsService.getSettings());
		model.addAttribute("templates", new ArrayList<>());
		return "templates/templates";
	}

	@GetMapping("/templates/manage")
	public String manage(Model model)
	{
		model.addAttribute("settings", settingsService.getSettings());
		model.addAttribute("templates", new ArrayList<>());
		return "templates/manage";
	}
}