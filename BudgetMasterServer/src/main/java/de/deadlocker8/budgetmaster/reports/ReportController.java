package de.deadlocker8.budgetmaster.reports;

import com.itextpdf.text.DocumentException;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import de.deadlocker8.budgetmaster.filter.FilterHelpersService;
import de.deadlocker8.budgetmaster.reports.categoryBudget.CategoryBudgetHandler;
import de.deadlocker8.budgetmaster.reports.settings.ReportSettings;
import de.deadlocker8.budgetmaster.reports.settings.ReportSettingsService;
import de.deadlocker8.budgetmaster.services.DateService;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.utils.Mappings;
import de.thecodelabs.utils.util.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequestMapping(Mappings.REPORTS)
public class ReportController extends BaseController
{
	private static class ModelAttributes
	{
		public static final String REPORT_SETTINGS = "reportSettings";
		public static final String CURRENT_DATE = "currentDate";
		public static final String FILTER_CONFIGURATION = "filterConfiguration";
	}

	private static class ReturnValues
	{
		public static final String ALL_ENTITIES = "reports/reports";
	}

	private final ReportSettingsService reportSettingsService;
	private final ReportGeneratorService reportGeneratorService;
	private final TransactionService transactionService;
	private final CategoryService categoryService;
	private final HelpersService helpers;
	private final DateService dateService;
	private final FilterHelpersService filterHelpers;

	@Autowired
	public ReportController(ReportSettingsService reportSettingsService, ReportGeneratorService reportGeneratorService, TransactionService transactionService, CategoryService categoryService, HelpersService helpers, DateService dateService, FilterHelpersService filterHelpers)
	{
		this.reportSettingsService = reportSettingsService;
		this.reportGeneratorService = reportGeneratorService;
		this.transactionService = transactionService;
		this.categoryService = categoryService;
		this.helpers = helpers;
		this.dateService = dateService;
		this.filterHelpers = filterHelpers;
	}

	@GetMapping
	public String reports(HttpServletRequest request, Model model, @CookieValue(value = "currentDate", required = false) String cookieDate)
	{
		LocalDate date = dateService.getDateTimeFromCookie(cookieDate);

		model.addAttribute(ModelAttributes.REPORT_SETTINGS, reportSettingsService.getReportSettings());
		model.addAttribute(ModelAttributes.CURRENT_DATE, date);
		model.addAttribute(ModelAttributes.FILTER_CONFIGURATION, filterHelpers.getFilterConfiguration(request));
		return ReturnValues.ALL_ENTITIES;
	}

	@PostMapping(value = "/generate")
	public void post(HttpServletRequest request, HttpServletResponse response,
					 @ModelAttribute("NewReportSettings") ReportSettings reportSettings)
	{
		//save new report settings
		reportSettings.getColumns().forEach(column -> column.setReferringSettings(reportSettings));
		reportSettingsService.getRepository().save(reportSettings);

		//prepare generation
		Account account = helpers.getCurrentAccount();
		String accountName = account.getName();
		if(account.getType().equals(AccountType.ALL))
		{
			accountName = Localization.getString("account.all");
		}

		FilterConfiguration filterConfiguration = filterHelpers.getFilterConfiguration(request);
		List<Transaction> transactions = transactionService.getTransactionsForMonthAndYear(account, reportSettings.getDate().getMonthValue(), reportSettings.getDate().getYear(), filterConfiguration);
		Budget budget = helpers.getBudget(transactions, account);

		ReportConfiguration reportConfiguration = new ReportConfigurationBuilder()
				.setBudget(budget)
				.setReportSettings(reportSettings)
				.setTransactions(transactions)
				.setAccountName(accountName)
				.setCategoryBudgets(CategoryBudgetHandler.getCategoryBudgets(transactions, categoryService.getAllEntitiesAsc()))
				.createReportConfiguration();

		String month = reportSettings.getDate().format(DateTimeFormatter.ofPattern("MM"));
		String year = reportSettings.getDate().format(DateTimeFormatter.ofPattern("yyyy"));

		LOGGER.debug(MessageFormat.format("Exporting month report (month: {0}_{1}, account: {2})...", year, month, accountName));

		//generate PDF
		try
		{
			byte[] dataBytes = reportGeneratorService.generate(reportConfiguration);
			String fileName = Localization.getString("report.initial.filename", year, month, accountName);
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

			response.setContentType("application/pdf; charset=UTF-8");
			response.setContentLength(dataBytes.length);
			response.setCharacterEncoding("UTF-8");

			try(ServletOutputStream out = response.getOutputStream())
			{
				out.write(dataBytes);
				out.flush();
				LOGGER.debug("Exporting month report DONE");
			}
			catch(IOException e)
			{
				LOGGER.error("Could not generate report PDF", e);
			}
		}
		catch(DocumentException e)
		{
			LOGGER.error("Could not generate report PDF", e);
		}
	}
}