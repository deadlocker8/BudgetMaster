package de.deadlocker8.budgetmaster.logic.payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import de.deadlocker8.budgetmaster.logic.FilterSettings;
import de.deadlocker8.budgetmaster.logic.tag.TagHandler;

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
	
	public int getPosition(Payment payment)
	{
		if(payment == null)
			return -1;
		
		for(int i = 0; i < payments.size(); i++)
		{
			if(payment.getID() == payments.get(i).getID())
			{
				return i;
			}
		}
		
		return -1;
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
			return paymentsList;
		}
		
		ArrayList<Payment> filteredPayments = new ArrayList<>();
		for(Payment currentPayment : paymentsList)
		{			
			//NormalPayment or rest
			if(currentPayment instanceof NormalPayment || currentPayment.getID() == -1)
			{						
				if(filterSettings.isNoRepeatingAllowed())
				{
					filteredPayments.add(currentPayment);
				}
			}
			//RepeatingPayment
			else			
			{
				RepeatingPaymentEntry repeatingPayment = (RepeatingPaymentEntry)currentPayment;
				if((repeatingPayment.getRepeatInterval() != 0 && filterSettings.isRepeatingEveryXDaysAllowed()) ||
					(repeatingPayment.getRepeatMonthDay() != 0 && filterSettings.isMonthlyRepeatingAllowed()))
				{
					filteredPayments.add(currentPayment);
				}
			}
		}
		
		return filteredPayments;		
	}
	
	private ArrayList<Payment> filterByCategory(FilterSettings filterSettings, ArrayList<Payment> paymentsList)
	{		
		if(filterSettings.getAllowedCategoryIDs() == null)			
		{
			return paymentsList;
		}
		
		if(filterSettings.getAllowedCategoryIDs().size() == 0)
		{
			return new ArrayList<>();
		}
		
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
			if(currentPayment.getName().toLowerCase().contains(filterSettings.getName().toLowerCase()))
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
	
	private ArrayList<Payment> filterByTag(FilterSettings filterSettings, ArrayList<Payment> paymentsList, TagHandler tagHandler) throws Exception
	{		
		if(filterSettings.getAllowedTagIDs() == null)			
		{
			return paymentsList;
		}
		
		if(filterSettings.getAllowedTagIDs().size() == 0)
		{
			return new ArrayList<>();
		}
		
		ArrayList<Payment> filteredPayments = new ArrayList<>();
		for(Payment currentPayment : paymentsList)
		{
			ArrayList<Integer> paymentTagIDs = tagHandler.getTagIDs(currentPayment);
			
			for(Integer tagID : filterSettings.getAllowedTagIDs())
			{
				if(paymentTagIDs.contains(tagID))
				{
					filteredPayments.add(currentPayment);
					break;
				}
			}
		}
		
		return filteredPayments;		
	}
	
	public void filter(FilterSettings filterSettings, TagHandler tagHandler) throws Exception
	{
		ArrayList<Payment> filteredPayments = filterByType(filterSettings, payments);
		filteredPayments = filterByType(filterSettings, filteredPayments);
		filteredPayments = filterByRepeating(filterSettings, filteredPayments);
		filteredPayments = filterByCategory(filterSettings, filteredPayments);
		filteredPayments = filterByName(filterSettings, filteredPayments);
		filteredPayments = filterByTag(filterSettings, filteredPayments, tagHandler);
		
		payments = filteredPayments;
	}
	
	public String toString()
	{
		return "PaymentHandler [payments=" + payments + "]";
	}
}
