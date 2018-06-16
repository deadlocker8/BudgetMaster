package de.deadlocker8.budgetmaster.database.legacy;

@Deprecated
public class TagMatch
{
	private String tagName;
	private int paymentID;
	private int repeatingPaymentID;

	public TagMatch(String tagName, int paymentID, int repeatingPaymentID)
	{
		this.tagName = tagName;
		this.paymentID = paymentID;
		this.repeatingPaymentID = repeatingPaymentID;
	}

	public String getTagName()
	{
		return tagName;
	}

	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}

	public int getPaymentID()
	{
		return paymentID;
	}

	public int getRepeatingPaymentID()
	{
		return repeatingPaymentID;
	}
}