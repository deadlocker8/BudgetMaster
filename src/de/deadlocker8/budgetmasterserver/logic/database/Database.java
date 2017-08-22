package de.deadlocker8.budgetmasterserver.logic.database;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.NormalPayment;
import de.deadlocker8.budgetmaster.logic.RepeatingPayment;

public class Database
{
	private ArrayList<Category> categories;
	private ArrayList<NormalPayment> normalPayments;
	private ArrayList<RepeatingPayment> repeatingPayments;
	
	public Database()
	{
	    
	}
	
	public Database(ArrayList<Category> categories, ArrayList<NormalPayment> normalPayments, ArrayList<RepeatingPayment> repeatingPayments)
	{
	    this.categories = categories;
	    this.normalPayments = normalPayments;
	    this.repeatingPayments = repeatingPayments;
	}
	
	public ArrayList<Category> getCategories()
	{
	    return categories;
	}
	
	public ArrayList<NormalPayment> getNormalPayments()
    {
        return normalPayments;
    }
	
	public ArrayList<RepeatingPayment> getRepeatingPayments()
    {
        return repeatingPayments;
    }
}