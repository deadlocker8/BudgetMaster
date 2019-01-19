package de.deadlocker8.budgetmaster.reports;

public class Budget
{
	private double incomeSum;
	private double expenditureSum;

	public Budget(double incomeSum, double expenditureSum)
	{
		this.incomeSum = incomeSum;
		this.expenditureSum = expenditureSum;
	}

	public double getIncomeSum()
	{
		return incomeSum;
	}

	public double getExpenditureSum()
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