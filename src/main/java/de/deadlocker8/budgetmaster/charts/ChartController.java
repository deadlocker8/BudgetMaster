package de.deadlocker8.budgetmaster.charts;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ChartController extends BaseController
{
	private final ChartService chartService;
	private final HelpersService helpers;
	private final SettingsService settingsService;

	@Autowired
	public ChartController(ChartService chartService, HelpersService helpers, SettingsService settingsService)
	{
		this.chartService = chartService;
		this.helpers = helpers;
		this.settingsService = settingsService;
	}

	@RequestMapping("/charts/chartList")
	public String chartList(Model model)
	{
		model.addAttribute("charts", chartService.getRepository().findAllByOrderByNameAsc());
		model.addAttribute("settings", settingsService.getSettings());
		return "charts/chartList";
	}
}
