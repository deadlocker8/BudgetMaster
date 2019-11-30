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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ChartController extends BaseController
{
	private static final Gson GSON = new GsonBuilder()
			.excludeFieldsWithoutExposeAnnotation()
			.setPrettyPrinting()
			.registerTypeAdapter(DateTime.class, (JsonSerializer<DateTime>) (json, typeOfSrc, context) -> new JsonPrimitive(ISODateTimeFormat.date().print(json)))
			.create();

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

	@PostMapping(value = "/charts")
	public String showChart(Model model, @ModelAttribute("NewChartSettings") ChartSettings chartSettings)
	{
		chartSettings.setFilterConfiguration(filterHelpersService.updateCategoriesAndTags(chartSettings.getFilterConfiguration()));
		Optional<Chart> chartOptional = chartService.getRepository().findById(chartSettings.getChartID());
		if(!chartOptional.isPresent())
		{
			throw new ResourceNotFoundException();
		}

		List<Transaction> transactions = transactionService.getTransactionsForAccount(helpers.getCurrentAccount(), chartSettings.getStartDate(), chartSettings.getEndDate(), chartSettings.getFilterConfiguration());
		String transactionJson = GSON.toJson(transactions);

		model.addAttribute("chartSettings", chartSettings);
		model.addAttribute("charts", chartService.getRepository().findAllByOrderByNameAsc());
		model.addAttribute("settings", settingsService.getSettings());
		model.addAttribute("chart", chartOptional.get());
		model.addAttribute("containerID", UUID.randomUUID());
		model.addAttribute("transactionData", transactionJson);
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
		Chart emptyChart = DefaultCharts.CHART_DEFAULT;
		model.addAttribute("chart", emptyChart);
		model.addAttribute("settings", settingsService.getSettings());
		return "charts/newChart";
	}

	@RequestMapping("/charts/{ID}/edit")
	public String editChart(Model model, @PathVariable("ID") Integer ID)
	{
		Optional<Chart> chartOptional = chartService.getRepository().findById(ID);
		if(!chartOptional.isPresent())
		{
			throw new ResourceNotFoundException();
		}

		model.addAttribute("chart", chartOptional.get());
		model.addAttribute("settings", settingsService.getSettings());
		return "charts/newChart";
	}

	@PostMapping(value = "/charts/newChart")
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

				Optional<Chart> existingChartOptional = chartService.getRepository().findById(chart.getID());
				if(existingChartOptional.isPresent())
				{
					if(existingChartOptional.get().getType() != ChartType.CUSTOM)
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
			chartService.getRepository().deleteById(ID);
		}

		return "redirect:/charts/manage";
	}

	@SuppressWarnings("OptionalIsPresent")
	private boolean isDeletable(Integer ID)
	{
		Optional<Chart> chartOptional = chartService.getRepository().findById(ID);
		if(chartOptional.isPresent())
		{
			return chartOptional.get().getType() == ChartType.CUSTOM;
		}
		return false;
	}
}