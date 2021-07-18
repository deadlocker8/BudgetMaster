package de.deadlocker8.budgetmaster.charts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import de.deadlocker8.budgetmaster.filter.FilterHelpersService;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.utils.Mappings;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import de.deadlocker8.budgetmaster.utils.WebRequestUtils;
import de.deadlocker8.budgetmaster.utils.notification.Notification;
import de.deadlocker8.budgetmaster.utils.notification.NotificationType;
import de.thecodelabs.utils.util.Localization;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

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
	private final FilterHelpersService filterHelpersService;
	private final TransactionService transactionService;

	@Autowired
	public ChartController(ChartService chartService, HelpersService helpers, FilterHelpersService filterHelpersService, TransactionService transactionService)
	{
		this.chartService = chartService;
		this.helpers = helpers;
		this.filterHelpersService = filterHelpersService;
		this.transactionService = transactionService;
	}

	@GetMapping
	public String charts(Model model)
	{
		List<Chart> charts = chartService.getAllEntitiesAsc();

		FilterConfiguration defaultFilterConfiguration = FilterConfiguration.DEFAULT;
		defaultFilterConfiguration.setFilterCategories(filterHelpersService.getFilterCategories());
		defaultFilterConfiguration.setFilterTags(filterHelpersService.getFilterTags());

		ChartSettings defaultChartSettings = ChartSettings.getDefault(charts.get(0).getID(), defaultFilterConfiguration);

		model.addAttribute("chartSettings", defaultChartSettings);
		model.addAttribute("charts", charts);
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
		model.addAttribute("charts", chartService.getAllEntitiesAsc());
		model.addAttribute("chart", chartOptional.get());
		model.addAttribute("containerID", UUID.randomUUID());
		model.addAttribute("transactionData", transactionJson);
		return "charts/charts";
	}

	@GetMapping("/manage")
	public String manage(Model model)
	{
		model.addAttribute("charts", chartService.getAllEntitiesAsc());
		return "charts/manage";
	}

	@GetMapping("/newChart")
	public String newChart(Model model)
	{
		Chart emptyChart = DefaultCharts.CHART_DEFAULT;
		model.addAttribute("chart", emptyChart);
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

		if(chart.getDisplayType() == null)
		{
			chart.setDisplayType(ChartDisplayType.CUSTOM);
		}

		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			model.addAttribute("chart", chart);
			return "charts/newChart";
		}

		boolean isNewChart = chart.getID() == null;
		if(isNewChart)
		{
			final int highestUsedID = chartService.getHighestUsedID();
			chart.setID(highestUsedID + 1);
		}
		else
		{
			// reject editing of default chart
			Optional<Chart> existingChartOptional = chartService.getRepository().findById(chart.getID());
			if(existingChartOptional.isPresent() && existingChartOptional.get().getType() != ChartType.CUSTOM)
			{
				return "error/400";
			}
		}

		chartService.getRepository().save(chart);

		return "redirect:/charts/manage";
	}

	@GetMapping("/{ID}/requestDelete")
	public String requestDeleteChart(Model model, @PathVariable("ID") Integer ID)
	{
		if(!chartService.isDeletable(ID))
		{
			return "redirect:/charts/manage";
		}

		model.addAttribute("charts", chartService.getAllEntitiesAsc());
		model.addAttribute("currentChart", chartService.getRepository().getOne(ID));
		return "charts/manage";
	}

	@GetMapping(value = "/{ID}/delete")
	public String deleteChart(WebRequest request, @PathVariable("ID") Integer ID)
	{
		if(chartService.isDeletable(ID))
		{
			final Chart chartToDelete = chartService.getRepository().getOne(ID);
			chartService.getRepository().deleteById(ID);
			WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.chart.delete.success", chartToDelete.getName()), NotificationType.SUCCESS));
		}
		else
		{
			WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.chart.delete.not.deletable", String.valueOf(ID)), NotificationType.ERROR));
		}

		return "redirect:/charts/manage";
	}
}