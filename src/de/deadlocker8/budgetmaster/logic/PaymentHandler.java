package de.deadlocker8.budgetmaster.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class PaymentHandler
{
	private ArrayList<Payment> payments;

	public PaymentHandler()
	{	
		payments = new ArrayList<>();
	}

	public ArrayList<Payment> getPayments()
	{
		return payments;
	}

	public void setPayments(ArrayList<Payment> payments)
	{
		this.payments = payments;
	}
	
	public void sort()
	{
		Collections.sort(payments, new Comparator<Payment>() {
	        @Override
	        public int compare(Payment payment1, Payment payment2)
	        {
	            return  payment2.getDate().compareTo(payment1.getDate());
	        }
	    });		
	}
	
	private ArrayList<Payment> filterByRepeating(FilterSettings filterSettings, ArrayList<Payment> paymentsList)
	{
		if(filterSettings.isNoRepeatingAllowed() && filterSettings.isMonthlyRepeatingAllowed() && filterSettings.isRepeatingEveryXDaysAllowed())
		{				
			return payments;
		}
		
		ArrayList<Payment> filteredPayments = new ArrayList<>();
		for(Payment currentPayment : paymentsList)
		{
			//TODO
		}
		
		return new ArrayList<>();		
	}
	
	private ArrayList<Payment> filterByCategory(FilterSettings filterSettings, ArrayList<Payment> paymentsList)
	{
		ArrayList<Payment> filteredPayments = new ArrayList<>();
		for(Payment currentPayment : paymentsList)
		{
			if(filterSettings.getAllowedCategoryIDs().contains(currentPayment.getCategoryID()))
			{
				filteredPayments.add(currentPayment);
			}
		}
		
		return filteredPayments;		
	}
	
	private ArrayList<Payment> filterByName(FilterSettings filterSettings, ArrayList<Payment> paymentsList)
	{
		if(filterSettings.getName() == null)
		{
			return paymentsList;
		}
		
		ArrayList<Payment> filteredPayments = new ArrayList<>();
		for(Payment currentPayment : paymentsList)
		{
			if(currentPayment.getName().contains(filterSettings.getName()))
			{
				filteredPayments.add(currentPayment);
			}
		}
		
		return filteredPayments;		
	} 
	
	private ArrayList<Payment> filterByType(FilterSettings filterSettings, ArrayList<Payment> paymentsList)
	{		
		if(filterSettings.isIncomeAllowed() && filterSettings.isPaymentAllowed())
		{			
			return paymentsList;
		}

		if(filterSettings.isIncomeAllowed())	
		{
			return new ArrayList<Payment>(paymentsList.stream().
				filter(p -> p.getAmount() > 0).
				collect(Collectors.toList()));
		}
		else if(filterSettings.isPaymentAllowed())	
		{
			return new ArrayList<Payment>(paymentsList.stream().
					filter(p -> p.getAmount() < 0).
					collect(Collectors.toList()));
		}
		
		return new ArrayList<>();
	}
	
	public void filter(FilterSettings filterSettings)
	{
		ArrayList<Payment> filteredPayments = filterByType(filterSettings, payments);
		filteredPayments = filterByType(filterSettings, filteredPayments);
		filteredPayments = filterByCategory(filterSettings, filteredPayments);
		filteredPayments = filterByName(filterSettings, filteredPayments);
		
		payments = filteredPayments;
	}
	
	public String toString()
	{
		return "PaymentHandler [payments=" + payments + "]";
	}
}
