package de.deadlocker8.budgetmaster.charts;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

	@RequestMapping("/charts/manage")
	public String manage(Model model)
	{
		model.addAttribute("charts", chartService.getRepository().findAllByOrderByNameAsc());
		model.addAttribute("settings", settingsService.getSettings());
		return "charts/manage";
	}

	@RequestMapping("/charts/newChart")
	public String newChart(Model model)
	{
		Chart emptyChart = new Chart(null, null, ChartType.CUSTOM);
		model.addAttribute("chart", emptyChart);
		model.addAttribute("settings", settingsService.getSettings());
		return "charts/newChart";
	}

	@RequestMapping("/charts/{ID}/edit")
	public String editChart(Model model, @PathVariable("ID") Integer ID)
	{
		Chart chart = chartService.getRepository().findOne(ID);
		if(chart == null)
		{
			throw new ResourceNotFoundException();
		}

		model.addAttribute("chart", chart);
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
			Chart existingChart = chartService.getRepository().getOne(chart.getID());
			if(existingChart != null)
			{
				if(existingChart.getType() != ChartType.CUSTOM)
				{
					return "error/400";
				}
			}

			if(chart.getType() == null)
			{
				chart.setType(ChartType.CUSTOM);
			}

			chartService.getRepository().save(chart);
		}

		return "redirect:/charts/manage";
	}

	@RequestMapping("/charts/{ID}/requestDelete")
	public String requestDeleteChart(Model model, @PathVariable("ID") Integer ID)
	{
		if(!isDeletable(ID))
		{
			return "redirect:/charts/manage";
		}

		model.addAttribute("charts", chartService.getRepository().findAllByOrderByNameAsc());
		model.addAttribute("currentChart", chartService.getRepository().getOne(ID));
		model.addAttribute("settings", settingsService.getSettings());
		return "charts/manage";
	}

	@RequestMapping(value = "/charts/{ID}/delete")
	public String deleteChart(Model model, @PathVariable("ID") Integer ID)
	{
		if(isDeletable(ID))
		{
			chartService.getRepository().delete(ID);
		}

		return "redirect:/charts/manage";
	}

	private boolean isDeletable(Integer ID)
	{
		Chart chartToDelete = chartService.getRepository().getOne(ID);
		return chartToDelete != null && chartToDelete.getType() == ChartType.CUSTOM;
	}
}
