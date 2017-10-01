package de.deadlocker8.budgetmaster.logic.database;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmaster.logic.payment.NormalPayment;
import de.deadlocker8.budgetmaster.logic.payment.RepeatingPayment;

/**
 * this class is only used for backwards compatibility while importing database jsons files
 */
@Deprecated
public class OldDatabase
{
	private ArrayList<Category> categories;
	private ArrayList<NormalPayment> normalPayments;
	private ArrayList<RepeatingPayment> repeatingPayments;

	public OldDatabase()
	{

	}

	public OldDatabase(ArrayList<Category> categories, ArrayList<NormalPayment> normalPayments, ArrayList<RepeatingPayment> repeatingPayments)
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