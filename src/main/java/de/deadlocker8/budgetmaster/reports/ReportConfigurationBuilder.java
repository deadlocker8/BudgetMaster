package de.deadlocker8.budgetmaster.reports;

import de.deadlocker8.budgetmaster.entities.Transaction;
import de.deadlocker8.budgetmaster.entities.report.ReportSettings;

import java.util.ArrayList;
import java.util.List;

public class ReportConfigurationBuilder
{
	private List<Transaction> transactions;
	private List<CategoryBudget> categoryBudgets;
	private ReportSettings reportSettings;
	private Budget budget;

	public ReportConfigurationBuilder setTransactions(List<Transaction> transactions)
	{
		this.transactions = transactions;
		return this;
	}

	public ReportConfigurationBuilder setCategoryBudgets(List<CategoryBudget> categoryBudgets)
	{
		this.categoryBudgets = categoryBudgets;
		return this;
	}

	public ReportConfigurationBuilder setReportSettings(ReportSettings reportSettings)
	{
		this.reportSettings = reportSettings;
		return this;
	}

	public ReportConfigurationBuilder setBudget(Budget budget)
	{
		this.budget = budget;
		return this;
	}

	public ReportConfiguration createReportConfiguration()
	{
		return new ReportConfiguration(transactions, categoryBudgets, reportSettings, budget);
	}
}