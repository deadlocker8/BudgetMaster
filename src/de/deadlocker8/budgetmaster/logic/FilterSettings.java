package de.deadlocker8.budgetmaster.logic;

import java.util.ArrayList;

public class FilterSettings
{
	private boolean isIncomeAllowed; 
	private boolean isPaymentAllowed; 
	private boolean isNoRepeatingAllowed;
	private boolean isMonthlyRepeatingAllowed;
	private boolean isRepeatingEveryXDaysAllowed;
	private ArrayList<Integer> allowedCategoryIDs;
	private String name;
	
	public FilterSettings(boolean isIncomeAllowed, boolean isPaymentAllowed, boolean isNoRepeatingAllowed, boolean isMonthlyRepeatingAllowed, boolean isRepeatingEveryXDaysAllowed, ArrayList<Integer> allowedCategoryIDs, String name)
	{
		this.isIncomeAllowed = isIncomeAllowed;
		this.isPaymentAllowed = isPaymentAllowed;
		this.isNoRepeatingAllowed = isNoRepeatingAllowed;
		this.isMonthlyRepeatingAllowed = isMonthlyRepeatingAllowed;
		this.isRepeatingEveryXDaysAllowed = isRepeatingEveryXDaysAllowed;
		this.allowedCategoryIDs = allowedCategoryIDs;
		this.name = name;
	}
	
	public FilterSettings()
	{
		this.isIncomeAllowed = true;
		this.isPaymentAllowed = true;
		this.isNoRepeatingAllowed = true;
		this.isMonthlyRepeatingAllowed = true;
		this.isRepeatingEveryXDaysAllowed = true;
		this.allowedCategoryIDs = null;
		this.name = null;
	}

	public boolean isIncomeAllowed()
	{
		return isIncomeAllowed;
	}

	public void setIncomeAllowed(boolean isIncomeAllowed)
	{
		this.isIncomeAllowed = isIncomeAllowed;
	}

	public boolean isPaymentAllowed()
	{
		return isPaymentAllowed;
	}

	public void setPaymentAllowed(boolean isPaymentAllowed)
	{
		this.isPaymentAllowed = isPaymentAllowed;
	}

	public boolean isNoRepeatingAllowed()
	{
		return isNoRepeatingAllowed;
	}

	public void setNoRepeatingAllowed(boolean isNoRepeatingAllowed)
	{
		this.isNoRepeatingAllowed = isNoRepeatingAllowed;
	}

	public boolean isMonthlyRepeatingAllowed()
	{
		return isMonthlyRepeatingAllowed;
	}

	public void setMonthlyRepeatingAllowed(boolean isMonthlyRepeatingAllowed)
	{
		this.isMonthlyRepeatingAllowed = isMonthlyRepeatingAllowed;
	}

	public boolean isRepeatingEveryXDaysAllowed()
	{
		return isRepeatingEveryXDaysAllowed;
	}

	public void setRepeatingEveryXDaysAllowed(boolean isRepeatingEveryXDaysAllowed)
	{
		this.isRepeatingEveryXDaysAllowed = isRepeatingEveryXDaysAllowed;
	}

	public ArrayList<Integer> getAllowedCategoryIDs()
	{
		return allowedCategoryIDs;
	}

	public void setAllowedCategoryIDs(ArrayList<Integer> allowedCategoryIDs)
	{
		this.allowedCategoryIDs = allowedCategoryIDs;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public String toString()
	{
		return "FilterSettings [isIncomeAllowed=" + isIncomeAllowed + ", isPaymentAllowed=" + isPaymentAllowed + ", isNoRepeatingAllowed=" + isNoRepeatingAllowed + ", isMonthlyRepeatingAllowed=" + isMonthlyRepeatingAllowed + ", isRepeatingEveryXDaysAllowed=" + isRepeatingEveryXDaysAllowed
				+ ", allowedCategoryIDs=" + allowedCategoryIDs + ", name=" + name + "]";
	}
}