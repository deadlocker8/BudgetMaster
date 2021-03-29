package de.deadlocker8.budgetmaster.statistics;

import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.charts.ChartService;
import de.deadlocker8.budgetmaster.charts.ChartType;
import de.deadlocker8.budgetmaster.services.DateService;
import de.deadlocker8.budgetmaster.templates.TemplateService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.thecodelabs.utils.util.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsService
{
	private static final String TEXT_WHITE = "text-white";
	private static final String TEXT_BLACK = "text-black";

	private final AccountService accountService;
	private final CategoryService categoryService;
	private final TransactionService transactionService;
	private final TemplateService templateService;
	private final ChartService chartService;
	private final DateService dateService;

	@Autowired
	public StatisticsService(AccountService accountService, CategoryService categoryService, TransactionService transactionService, TemplateService templateService, ChartService chartService, DateService dateService)
	{
		this.accountService = accountService;
		this.categoryService = categoryService;
		this.transactionService = transactionService;
		this.templateService = templateService;
		this.chartService = chartService;
		this.dateService = dateService;
	}

	public List<StatisticItem> getStatisticItems()
	{
		final List<StatisticItem> statisticItems = new ArrayList<>();
		statisticItems.add(new StatisticItem("account_balance", Localization.getString("statistics.number.of.accounts", accountService.getRepository().findAllByType(AccountType.CUSTOM).size()), "background-red", TEXT_WHITE));
		statisticItems.add(new StatisticItem("list", Localization.getString("statistics.number.of.transactions", transactionService.getRepository().findAll().size()), "background-blue-baby", TEXT_BLACK));
		statisticItems.add(new StatisticItem("file_copy", Localization.getString("statistics.number.of.templates", templateService.getRepository().findAll().size()), "background-orange-dark", TEXT_BLACK));
		statisticItems.add(new StatisticItem("show_chart", Localization.getString("statistics.number.of.custom.charts", chartService.getRepository().findAllByType(ChartType.CUSTOM).size()), "background-purple", TEXT_WHITE));
		statisticItems.add(new StatisticItem("label", Localization.getString("statistics.number.of.categories", categoryService.getAllCustomCategories().size()), "background-orange", TEXT_BLACK));
		statisticItems.add(new StatisticItem("event", Localization.getString("statistics.first.transaction", getFirstTransactionDate()), "background-grey", TEXT_BLACK));
		return statisticItems;
	}

	private String getFirstTransactionDate()
	{
		final Transaction firstTransaction = transactionService.getRepository().findFirstByOrderByDate();

		String firstTransactionDate = "-";
		if(firstTransaction != null)
		{
			firstTransactionDate = dateService.getDateStringNormal(firstTransaction.getDate());
		}

		return firstTransactionDate;
	}
}
