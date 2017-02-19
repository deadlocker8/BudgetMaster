package de.deadlocker8.budgetmaster.logic;

import java.util.List;

public class Budget
{
	private double incomeSum;
	private double paymentSum;
	
	public Budget(List<Payment> payments)
	{		
		incomeSum = 0;
		paymentSum = 0;
		for(Payment currentPayment : payments)
		{
			double amount = currentPayment.getAmount();
			if(amount > 0)
			{
				incomeSum += amount;
			}
			else
			{
				paymentSum += amount;
			}
		}
		
		incomeSum /= 100.0;
		paymentSum /= 100.0;
	}

	public double getIncomeSum()
	{
		return incomeSum;
	}

	public double getPaymentSum()
	{
		return paymentSum;
	}
}