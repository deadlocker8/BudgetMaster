package de.deadlocker8.budgetmasterserver.main;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.NormalPayment;
import de.deadlocker8.budgetmaster.logic.RepeatingPayment;

public class DatabaseImporter
{
	private DatabaseHandler handler;
	
	public DatabaseImporter(DatabaseHandler handler) throws IllegalStateException
    {
        this.handler = handler;
    }
	
	public void importDatabase(Database database)
	{		
		importCategories(database.getCategories());
		importNormalPayments(database.getNormalPayments());
		importRepeatingPayments(database.getRepeatingPayments());
	}
	
	private void importCategories(ArrayList<Category> categories)
	{	   
        for(Category currentCategory : categories)
        {
        	handler.importCategory(currentCategory);
        }
	}
	
	private void importNormalPayments(ArrayList<NormalPayment> normalPayments)
    {      
		for(NormalPayment currentPayment : normalPayments)
        {
        	handler.importNormalPayment(currentPayment);
        }
    }
	
	private void importRepeatingPayments(ArrayList<RepeatingPayment> repeatingPayments)
    {      
		for(RepeatingPayment currentPayment : repeatingPayments)
        {
        	handler.importRepeatingPayment(currentPayment);
        }
    }
}