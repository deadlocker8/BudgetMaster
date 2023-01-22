package de.deadlocker8.budgetmaster.reports;

public record Budget(int incomeSum, int expenditureSum)
{
	public int getRest()
	{
		return incomeSum + expenditureSum;
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