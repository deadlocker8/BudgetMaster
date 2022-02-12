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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping(Mappings.CHARTS)
public class ChartController extends BaseController
{
	private static class ModelAttributes
	{
		public static final String ALL_ENTITIES = "charts";
		public static final String ONE_ENTITY = "chart";
		public static final String ENTITY_TO_DELETE = "chartToDelete";
		public static final String ERROR = "error";
		public static final String CHART_SETTINGS = "chartSettings";
		public static final String CONTAINER_ID = "containerID";
		public static final String TRANSACTION_DATA = "transactionData";
		public static final String DISPLAY_TYPES = "displayTypes";
		public static final String GROUP_TYPES = "groupTypes";
	}

	private static class ReturnValues
	{
		public static final String SHOW_ALL = "charts/charts";
		public static final String MANAGE = "charts/manage";
		public static final String REDIRECT_MANAGE = "redirect:/charts/manage";
		public static final String NEW_ENTITY = "charts/newChart";
		public static final String DELETE_ENTITY = "charts/deleteChartModal";
		public static final String ERROR_400 = "error/400";
	}

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

		ChartSettings defaultChartSettings = ChartSettings.getDefault(defaultFilterConfiguration);

		model.addAttribute(ModelAttributes.CHART_SETTINGS, defaultChartSettings);
		model.addAttribute(ModelAttributes.ALL_ENTITIES, charts);
		model.addAttribute(ModelAttributes.DISPLAY_TYPES, ChartDisplayType.values());
		model.addAttribute(ModelAttributes.GROUP_TYPES, ChartGroupType.values());

		return ReturnValues.SHOW_ALL;
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
		List<Transaction> convertedTransactions = convertTransferAmounts(transactions);
		String transactionJson = GSON.toJson(convertedTransactions);

		model.addAttribute(ModelAttributes.CHART_SETTINGS, chartSettings);
		model.addAttribute(ModelAttributes.ALL_ENTITIES, chartService.getAllEntitiesAsc());
		model.addAttribute(ModelAttributes.ONE_ENTITY, chartOptional.get());
		model.addAttribute(ModelAttributes.CONTAINER_ID, UUID.randomUUID());
		model.addAttribute(ModelAttributes.TRANSACTION_DATA, transactionJson);
		model.addAttribute(ModelAttributes.DISPLAY_TYPES, ChartDisplayType.values());
		model.addAttribute(ModelAttributes.GROUP_TYPES, ChartGroupType.values());
		return ReturnValues.SHOW_ALL;
	}

	/**
	 * If a chart is requested for a specific account (the currently selected account) the sign of transfers must be corrected accordingly.
	 * Example: Two accounts: Account_A and Account_B. One Transfer from Account_A to Account_B with an amount of 100€.
	 * Therefore Account_A has an expenditure of 100€ and Account_B an income. The transfer amount retrieved from the database is always negative.
	 * If a chart is to be shown for Account_B the amount has to be inverted, so that it becomes positive. This method will ensure all transfer amounts are converted.
	 *
	 * @param transactions: The transactions to check and convert if necessary.
	 * @return The converted transactions.
	 */
	private List<Transaction> convertTransferAmounts(List<Transaction> transactions)
	{
		List<Transaction> convertedTransactions = new ArrayList<>();
		for(Transaction transaction : transactions)
		{
			Transaction convertedTransaction = transaction;

			if(transaction.isTransfer() && transaction.getTransferAccount().equals(helpers.getCurrentAccount()))
			{
				convertedTransaction = new Transaction(transaction);
				convertedTransaction.setAmount(-transaction.getAmount());
			}

			convertedTransactions.add(convertedTransaction);
		}
		return convertedTransactions;
	}

	@GetMapping("/manage")
	public String manage(Model model)
	{
		model.addAttribute(ModelAttributes.ALL_ENTITIES, chartService.getAllEntitiesAsc());
		return ReturnValues.MANAGE;
	}

	@GetMapping("/newChart")
	public String newChart(Model model)
	{
		Chart emptyChart = DefaultCharts.CHART_DEFAULT;
		model.addAttribute(ModelAttributes.ONE_ENTITY, emptyChart);
		return ReturnValues.NEW_ENTITY;
	}

	@GetMapping("/{ID}/edit")
	public String editChart(Model model, @PathVariable("ID") Integer ID)
	{
		Optional<Chart> chartOptional = chartService.getRepository().findById(ID);
		if(chartOptional.isEmpty())
		{
			throw new ResourceNotFoundException();
		}

		model.addAttribute(ModelAttributes.ONE_ENTITY, chartOptional.get());
		return ReturnValues.NEW_ENTITY;
	}

	@PostMapping(value = "/newChart")
	public String post(WebRequest request, Model model, @ModelAttribute("NewChart") Chart chart, BindingResult bindingResult)
	{
		ChartValidator userValidator = new ChartValidator();
		userValidator.validate(chart, bindingResult);

		chart.setType(ChartType.CUSTOM);
		chart.setDisplayType(ChartDisplayType.CUSTOM);
		chart.setGroupType(ChartGroupType.NONE);

		if(bindingResult.hasErrors())
		{
			model.addAttribute(ModelAttributes.ERROR, bindingResult);
			model.addAttribute(ModelAttributes.ONE_ENTITY, chart);
			return ReturnValues.NEW_ENTITY;
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
				return ReturnValues.ERROR_400;
			}
		}

		chartService.getRepository().save(chart);
		WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.chart.save.success", chart.getName()), NotificationType.SUCCESS));
		return ReturnValues.REDIRECT_MANAGE;
	}

	@GetMapping("/{ID}/requestDelete")
	public String requestDeleteChart(Model model, @PathVariable("ID") Integer ID)
	{
		if(!chartService.isDeletable(ID))
		{
			return ReturnValues.REDIRECT_MANAGE;
		}

		model.addAttribute(ModelAttributes.ALL_ENTITIES, chartService.getAllEntitiesAsc());
		model.addAttribute(ModelAttributes.ENTITY_TO_DELETE, chartService.getRepository().getById(ID));
		return ReturnValues.DELETE_ENTITY;
	}

	@GetMapping(value = "/{ID}/delete")
	public String deleteChart(WebRequest request, @PathVariable("ID") Integer ID)
	{
		if(chartService.isDeletable(ID))
		{
			final Chart chartToDelete = chartService.getRepository().getById(ID);
			chartService.getRepository().deleteById(ID);
			WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.chart.delete.success", chartToDelete.getName()), NotificationType.SUCCESS));
		}
		else
		{
			WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.chart.delete.not.deletable", String.valueOf(ID)), NotificationType.ERROR));
		}

		return ReturnValues.REDIRECT_MANAGE;
	}
}