package de.deadlocker8.budgetmaster.logic.tag;

import java.util.ArrayList;
import java.util.HashMap;

import de.deadlocker8.budgetmaster.logic.payment.Payment;
import de.deadlocker8.budgetmaster.logic.payment.RepeatingPaymentEntry;

public class TagCache
{
	private HashMap<Integer, ArrayList<Tag>> normalPaymentTagCache;
	private HashMap<Integer, ArrayList<Tag>> repeatingPaymentTagCache;
	
	public TagCache()
	{
		clear();
	}
	
	public void clear()
	{
		normalPaymentTagCache = new HashMap<>();
		repeatingPaymentTagCache = new HashMap<>();
	}
	
	public void addTags(Payment payment, ArrayList<Tag> tags)
	{
		if(payment instanceof RepeatingPaymentEntry)
		{
			repeatingPaymentTagCache.put(((RepeatingPaymentEntry)payment).getRepeatingPaymentID(), tags);
		}
		else
		{
			normalPaymentTagCache.put(payment.getID(), tags);
		}
	}
	
	public ArrayList<Tag> getTags(Payment payment)
	{
		if(payment instanceof RepeatingPaymentEntry)
		{
			RepeatingPaymentEntry repeatingPayment = (RepeatingPaymentEntry)payment;
			if(repeatingPaymentTagCache.containsKey(repeatingPayment.getRepeatingPaymentID()))
			{
				return repeatingPaymentTagCache.get(repeatingPayment.getRepeatingPaymentID());
			}
		}
		else
		{
			if(normalPaymentTagCache.containsKey(payment.getID()))
			{
				return normalPaymentTagCache.get(payment.getID());
			}			
		}
		
		return null;
	}

	public HashMap<Integer, ArrayList<Tag>> getNormalPaymentTagCache()
	{
		return normalPaymentTagCache;
	}

	public HashMap<Integer, ArrayList<Tag>> getRepeatingPaymentTagCache()
	{
		return repeatingPaymentTagCache;
	}
}