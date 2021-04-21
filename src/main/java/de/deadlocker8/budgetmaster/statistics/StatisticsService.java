package de.deadlocker8.budgetmaster.statistics;

import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.charts.ChartService;
import de.deadlocker8.budgetmaster.charts.ChartType;
import de.deadlocker8.budgetmaster.services.DateService;
import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.templates.TemplateService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.thecodelabs.utils.util.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
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

		final int numberOfAccounts = accountService.getRepository().findAllByType(AccountType.CUSTOM).size();
		statisticItems.add(new StatisticItem(EntityType.ACCOUNT.getIcon(), getTextForEntityType(EntityType.ACCOUNT, numberOfAccounts), "background-red", TEXT_WHITE));

		final int numberOfTransactions = transactionService.getRepository().findAll().size();
		statisticItems.add(new StatisticItem(EntityType.TRANSACTION.getIcon(), getTextForEntityType(EntityType.TRANSACTION, numberOfTransactions), "background-blue-baby", TEXT_BLACK));

		final int numberOfTemplates = templateService.getRepository().findAll().size();
		statisticItems.add(new StatisticItem(EntityType.TEMPLATE.getIcon(), getTextForEntityType(EntityType.TEMPLATE, numberOfTemplates), "background-orange-dark", TEXT_BLACK));

		final int numberOfCharts = chartService.getRepository().findAllByType(ChartType.CUSTOM).size();
		statisticItems.add(new StatisticItem(EntityType.CHART.getIcon(), getTextForEntityType(EntityType.CHART, numberOfCharts), "background-purple", TEXT_WHITE));

		final int numberOfCategories = categoryService.getAllCustomCategories().size();
		statisticItems.add(new StatisticItem(EntityType.CATEGORY.getIcon(), getTextForEntityType(EntityType.CATEGORY, numberOfCategories), "background-orange", TEXT_BLACK));

		statisticItems.add(new StatisticItem("event", Localization.getString("statistics.first.transaction", getFirstTransactionDate()), "background-grey", TEXT_BLACK));
		return statisticItems;
	}

	private String getTextForEntityType(EntityType entityType, int numberOfItems)
	{
		return MessageFormat.format("{0} {1}", numberOfItems, Localization.getString(entityType.getLocalizationKey()));
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
