package de.deadlocker8.budgetmaster.charts;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	@RequestMapping("/charts/newChart")
	public String newChart(Model model)
	{
		Chart emptyChart = new Chart(null, null, ChartType.CUSTOM);
		model.addAttribute("chart", emptyChart);
		model.addAttribute("settings", settingsService.getSettings());
		return "charts/newChart";
	}

	@RequestMapping(value = "/charts/newChart", method = RequestMethod.POST)
	public String post(Model model, @ModelAttribute("NewChart") Chart chart, BindingResult bindingResult)
	{
		ChartValidator userValidator = new ChartValidator();
		userValidator.validate(chart, bindingResult);

		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			model.addAttribute("chart", chart);
			model.addAttribute("settings", settingsService.getSettings());
			return "charts/newChart";
		}
		else
		{
			if(chart.getType() == null)
			{
				chart.setType(ChartType.CUSTOM);
			}
			chartService.getRepository().save(chart);
		}

		return "redirect:/charts/chartList";
	}
}
