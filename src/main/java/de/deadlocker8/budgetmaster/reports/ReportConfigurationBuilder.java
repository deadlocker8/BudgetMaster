package de.deadlocker8.budgetmaster.reports;

import de.deadlocker8.budgetmaster.entities.Transaction;
import de.deadlocker8.budgetmaster.entities.report.ReportSettings;

import java.util.ArrayList;

public class ReportConfigurationBuilder
{
	private ArrayList<Transaction> transactions;
	private ArrayList<CategoryBudget> categoryBudgets;
	private ReportSettings reportSettings;
	private Budget budget;

	public ReportConfigurationBuilder setReportItems(ArrayList<Transaction> transactions)
	{
		this.transactions = transactions;
		return this;
	}

	public ReportConfigurationBuilder setCategoryBudgets(ArrayList<CategoryBudget> categoryBudgets)
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