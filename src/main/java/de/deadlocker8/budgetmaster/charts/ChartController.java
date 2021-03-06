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
import de.deadlocker8.budgetmaster.utils.Mappings;
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
@RequestMapping(Mappings.CHARTS)
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

	@GetMapping
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

	@PostMapping
	public String showChart(Model model, @ModelAttribute("NewChartSettings") ChartSettings chartSettings)
	{
		chartSettings.setFilterConfiguration(filterHelpersService.updateCategoriesAndTags(chartSettings.getFilterConfiguration()));
		Optional<Chart> chartOptional = chartService.getRepository().findById(chartSettings.getChartID());
		if(chartOptional.isEmpty())
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

	@GetMapping("/manage")
	public String manage(Model model)
	{
		model.addAttribute("charts", chartService.getRepository().findAllByOrderByNameAsc());
		model.addAttribute("settings", settingsService.getSettings());
		return "charts/manage";
	}

	@GetMapping("/newChart")
	public String newChart(Model model)
	{
		Chart emptyChart = DefaultCharts.CHART_DEFAULT;
		model.addAttribute("chart", emptyChart);
		model.addAttribute("settings", settingsService.getSettings());
		return "charts/newChart";
	}

	@GetMapping("/{ID}/edit")
	public String editChart(Model model, @PathVariable("ID") Integer ID)
	{
		Optional<Chart> chartOptional = chartService.getRepository().findById(ID);
		if(chartOptional.isEmpty())
		{
			throw new ResourceNotFoundException();
		}

		model.addAttribute("chart", chartOptional.get());
		model.addAttribute("settings", settingsService.getSettings());
		return "charts/newChart";
	}

	@PostMapping(value = "/newChart")
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

			if (chart.getID() == null)
			{
				final int highestUsedID = chartService.getHighestUsedID();
				chart.setID(highestUsedID + 1);
			}
			chartService.getRepository().save(chart);
		}

		return "redirect:/charts/manage";
	}

	@GetMapping("/{ID}/requestDelete")
	public String requestDeleteChart(Model model, @PathVariable("ID") Integer ID)
	{
		if(!chartService.isDeletable(ID))
		{
			return "redirect:/charts/manage";
		}

		model.addAttribute("charts", chartService.getRepository().findAllByOrderByNameAsc());
		model.addAttribute("currentChart", chartService.getRepository().getOne(ID));
		model.addAttribute("settings", settingsService.getSettings());
		return "charts/manage";
	}

	@GetMapping(value = "/{ID}/delete")
	public String deleteChart(Model model, @PathVariable("ID") Integer ID)
	{
		if(chartService.isDeletable(ID))
		{
			chartService.getRepository().deleteById(ID);
		}

		return "redirect:/charts/manage";
	}
}