package de.deadlocker8.budgetmaster.charts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import de.deadlocker8.budgetmaster.filter.FilterHelpersService;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class ChartController extends BaseController
{
	private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().registerTypeAdapter(DateTime.class, (JsonSerializer<DateTime>) (json, typeOfSrc, context) -> new JsonPrimitive(ISODateTimeFormat.date().print(json))).create();

	private final ChartService chartService;
	private final HelpersService helpers;
	private final SettingsService settingsService;
	private final FilterHelpersService filterHelpersService;
	private final TransactionService transactionService;

	@Autowired
	public ChartController(ChartService chartService, HelpersService helpers, SettingsService settingsService, FilterHelpersService filterHelpersService, TransactionService transactionService)
	{
		this.chartService = chartService;
		this.helpers = helpers;
		this.settingsService = settingsService;
		this.filterHelpersService = filterHelpersService;
		this.transactionService = transactionService;
	}

	@RequestMapping("/charts")
	public String charts(Model model)
	{
		List<Chart> charts = chartService.getRepository().findAllByOrderByNameAsc();

		FilterConfiguration defaultFilterConfiguration = FilterConfiguration.DEFAULT;
		defaultFilterConfiguration.setFilterCategories(filterHelpersService.getFilterCategories());
		defaultFilterConfiguration.setFilterTags(filterHelpersService.getFilterTags());

		ChartSettings defaultChartSettings = ChartSettings.getDefault(charts.get(0).getID(), defaultFilterConfiguration);

		model.addAttribute("chartSettings", defaultChartSettings);
		model.addAttribute("charts", charts);
		model.addAttribute("settings", settingsService.getSettings());
		return "charts/charts";
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

		if(chart.getType() == null)
		{
			chart.setType(ChartType.CUSTOM);
		}

		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			model.addAttribute("chart", chart);
			model.addAttribute("settings", settingsService.getSettings());
			return "charts/newChart";
		}
		else
		{
			// editing an existing chart
			if(chart.getID() != null)
			{
				// reject editing of default chart
				Chart existingChart = chartService.getRepository().getOne(chart.getID());
				if(existingChart != null)
				{
					if(existingChart.getType() != ChartType.CUSTOM)
					{
						return "error/400";
					}
				}
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

	@RequestMapping(value = "/charts/showChart", method = RequestMethod.POST)
	public String showChart(Model model, @ModelAttribute("NewChartSettings") ChartSettings chartSettings)
	{
		chartSettings.setFilterConfiguration(filterHelpersService.updateCategoriesAndTags(chartSettings.getFilterConfiguration()));
		Chart chart = chartService.getRepository().findOne(chartSettings.getChartID());

		List<Transaction> transactions = transactionService.getTransactionsForAccount(helpers.getCurrentAccount(), chartSettings.getStartDate(), chartSettings.getEndDate(), chartSettings.getFilterConfiguration());
		String transactionJson = GSON.toJson(transactions);

		model.addAttribute("chartSettings", chartSettings);
		model.addAttribute("charts", chartService.getRepository().findAllByOrderByNameAsc());
		model.addAttribute("settings", settingsService.getSettings());
		model.addAttribute("chart", chart);
		model.addAttribute("transactionData", transactionJson);
		return "charts/charts";
	}
}
