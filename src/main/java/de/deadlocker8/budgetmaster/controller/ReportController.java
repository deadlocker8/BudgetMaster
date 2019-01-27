package de.deadlocker8.budgetmaster.controller;

import com.itextpdf.text.DocumentException;
import de.deadlocker8.budgetmaster.entities.Transaction;
import de.deadlocker8.budgetmaster.entities.account.Account;
import de.deadlocker8.budgetmaster.entities.account.AccountType;
import de.deadlocker8.budgetmaster.entities.report.ReportColumn;
import de.deadlocker8.budgetmaster.entities.report.ReportSettings;
import de.deadlocker8.budgetmaster.reports.Budget;
import de.deadlocker8.budgetmaster.reports.ReportConfiguration;
import de.deadlocker8.budgetmaster.reports.ReportConfigurationBuilder;
import de.deadlocker8.budgetmaster.reports.categoryBudget.CategoryBudgetHandler;
import de.deadlocker8.budgetmaster.services.CategoryService;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.services.SettingsService;
import de.deadlocker8.budgetmaster.services.TransactionService;
import de.deadlocker8.budgetmaster.services.report.ReportColumnService;
import de.deadlocker8.budgetmaster.services.report.ReportGeneratorService;
import de.deadlocker8.budgetmaster.services.report.ReportSettingsService;
import de.thecodelabs.utils.util.Localization;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ReportController extends BaseController
{
	@Autowired
	private SettingsService settingsService;

	@Autowired
	private ReportSettingsService reportSettingsService;

	@Autowired
	private ReportColumnService reportColumnService;

	@Autowired
	private ReportGeneratorService reportGeneratorService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private HelpersService helpers;

	@RequestMapping("/reports")
	public String reports(Model model, @CookieValue(value = "currentDate", required = false) String cookieDate)
	{
		DateTime date = helpers.getDateTimeFromCookie(cookieDate);

		model.addAttribute("reportSettings", reportSettingsService.getReportSettings());
		model.addAttribute("currentDate", date);
		return "reports/reports";
	}

	@RequestMapping(value = "/reports/generate", method = RequestMethod.POST)
	public void post(HttpServletResponse response,
					   @ModelAttribute("NewReportSettings") ReportSettings reportSettings)
	{
		//save new report settings
		reportSettingsService.getRepository().delete(0);
		for(ReportColumn reportColumn : reportSettings.getColumns())
		{
			reportColumnService.getRepository().save(reportColumn);
		}
		reportSettingsService.getRepository().save(reportSettings);


		//prepare generation
		Account account = helpers.getCurrentAccount();
		String accountName = account.getName();
		if(account.getType().equals(AccountType.ALL))
		{
			accountName = Localization.getString("account.all");
		}

		List<Transaction> transactions = transactionService.getTransactionsForMonthAndYear(account, reportSettings.getDate().getMonthOfYear(), reportSettings.getDate().getYear(), settingsService.getSettings().isRestActivated());
		Budget budget = new Budget(helpers.getIncomeSumForTransactionList(transactions), helpers.getExpenditureSumForTransactionList(transactions));

		ReportConfiguration reportConfiguration = new ReportConfigurationBuilder()
				.setBudget(budget)
				.setReportSettings(reportSettings)
				.setTransactions(transactions)
				.setAccountName(accountName)
				.setCategoryBudgets(CategoryBudgetHandler.getCategoryBudgets(transactions, categoryService.getRepository().findAll()))
				.createReportConfiguration();

		String month = reportSettings.getDate().toString("MM");
		String year = reportSettings.getDate().toString("YYYY");

		LOGGER.debug("Exporting month report (month: " + year + "_" + month + ", account: " + accountName + ")...");

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
				e.printStackTrace();
			}
		}
		catch(DocumentException e)
		{
			e.printStackTrace();
		}
	}
}