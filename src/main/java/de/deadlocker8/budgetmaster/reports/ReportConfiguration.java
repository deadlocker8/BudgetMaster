package de.deadlocker8.budgetmaster.reports;

import de.deadlocker8.budgetmaster.entities.Transaction;
import de.deadlocker8.budgetmaster.entities.report.ReportSettings;

import java.util.ArrayList;

public class ReportConfiguration
{
	private ArrayList<Transaction> transactions;
	private ArrayList<CategoryBudget> categoryBudgets;
	private ReportSettings reportSettings;
	private Budget budget;

	public ReportConfiguration(ArrayList<Transaction> transactions, ArrayList<CategoryBudget> categoryBudgets, ReportSettings reportSettings, Budget budget)
	{
		this.transactions = transactions;
		this.categoryBudgets = categoryBudgets;
		this.reportSettings = reportSettings;
		this.budget = budget;
	}

	public ArrayList<Transaction> getTransactions()
	{
		return transactions;
	}

	public ArrayList<CategoryBudget> getCategoryBudgets()
	{
		return categoryBudgets;
	}

	public ReportSettings getReportSettings()
	{
		return reportSettings;
	}

	public Budget getBudget()
	{
		return budget;
	}

	@Override
	public String toString()
	{
		return "ReportConfiguration{" +
				"transactions=" + transactions +
				", categoryBudgets=" + categoryBudgets +
				", reportSettings=" + reportSettings +
				", budget=" + budget +
				'}';
	}
}
