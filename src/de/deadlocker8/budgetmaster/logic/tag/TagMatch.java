package de.deadlocker8.budgetmaster.logic.tag;

import de.deadlocker8.budgetmasterserver.logic.DatabaseImportExport;

@DatabaseImportExport
public class TagMatch
{
	private int tagID;
	private int paymentID;
	private int repeatingPaymentID;
	
	public TagMatch(int tagID, int paymentID, int repeatingPaymentID)
	{		
		this.tagID = tagID;
		this.paymentID = paymentID;
		this.repeatingPaymentID = repeatingPaymentID;
	}

	public TagMatch()
	{
		
	}
	
	public int getTagID()	
	{
		return tagID;
	}
	
	public void setTagID(int tagID)
	{
		this.tagID = tagID;
	}
	
	public int getPaymentID()
	{
		return paymentID;
	}
	
	public void setPaymentID(int paymentID)
	{
		this.paymentID = paymentID;
	}
	
	public int getRepeatingPaymentID()
	{
		return repeatingPaymentID;
	}
	
	public void setRepeatingPaymentID(int repeatingPaymentID)
	{
		this.repeatingPaymentID = repeatingPaymentID;
	}

	@Override
	public String toString()
	{
		return "TagMatch [tagID=" + tagID + ", paymentID=" + paymentID + ", repeatingPaymentID=" + repeatingPaymentID + "]";
	}
}