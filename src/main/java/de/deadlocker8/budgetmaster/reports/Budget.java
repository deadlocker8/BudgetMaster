package de.deadlocker8.budgetmaster.reports;

public class Budget
{
	private int incomeSum;
	private int expenditureSum;

	public Budget(int incomeSum, int expenditureSum)
	{
		this.incomeSum = incomeSum;
		this.expenditureSum = expenditureSum;
	}

	public int getIncomeSum()
	{
		return incomeSum;
	}

	public int getExpenditureSum()
	{
		return expenditureSum;
	}

	@Override
	public String toString()
	{
		return "Budget{" +
				"incomeSum=" + incomeSum +
				", expenditureSum=" + expenditureSum +
				'}';
	}
}