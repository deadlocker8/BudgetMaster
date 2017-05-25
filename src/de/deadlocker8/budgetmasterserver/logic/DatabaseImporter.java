package de.deadlocker8.budgetmasterserver.logic;

import java.util.ArrayList;
import java.util.Iterator;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.NormalPayment;
import de.deadlocker8.budgetmaster.logic.RepeatingPayment;

public class DatabaseImporter
{
	private DatabaseHandler handler;
	private ArrayList<Category> categories;
	private ArrayList<NormalPayment> normalPayments;
	private ArrayList<RepeatingPayment> repeatingPayments;
	private ArrayList<NormalPayment> changedNormalPayments;
	private ArrayList<RepeatingPayment> changedRepeatingPayments;
	
	public DatabaseImporter(DatabaseHandler handler) throws IllegalStateException
    {
        this.handler = handler;
    }
	
	public void importDatabase(Database database)
	{		
		this.categories = database.getCategories();
		this.normalPayments = database.getNormalPayments();
		this.repeatingPayments = database.getRepeatingPayments();	
		this.changedNormalPayments = new ArrayList<>();
		this.changedRepeatingPayments = new ArrayList<>();
		
		importAll();	
	}
	
	private void importAll()
	{	   	
        for(Category currentCategory : categories)
        {
        	Category existingCategory = handler.getCategory(currentCategory.getName(), currentCategory.getColor());
        	if(existingCategory == null)
        	{
        		handler.addCategory(currentCategory.getName(), currentCategory.getColor());
        		int newID = handler.getLastInsertID();
        		
        		updatePayments(currentCategory.getID(), newID);
        	}
        	else
        	{
        		updatePayments(currentCategory.getID(), existingCategory.getID()); 
        	}
        }
        
        //merge changed and remaining payments
        normalPayments.addAll(changedNormalPayments);
        repeatingPayments.addAll(changedRepeatingPayments);
        
        importNormalPayments(normalPayments);
        importRepeatingPayments(repeatingPayments);
	}
	
	private void updatePayments(int oldID, int newID)
	{
		//check normal payments for old category ID
		Iterator<NormalPayment> iterator = normalPayments.iterator();        		
		while(iterator.hasNext())
		{
			NormalPayment currentPayment = iterator.next();
			if(currentPayment.getCategoryID() == oldID)
			{
				currentPayment.setCategoryID(newID);
				//remove payment from list to avoid overriding category ID again in the future
				changedNormalPayments.add(currentPayment);
				iterator.remove();
			}
		}
		
		//check repeating payments for old category ID
		Iterator<RepeatingPayment> iterator2 = repeatingPayments.iterator();        		
		while(iterator2.hasNext())
		{
			RepeatingPayment currentPayment = iterator2.next();
			if(currentPayment.getCategoryID() == oldID)
			{
				currentPayment.setCategoryID(newID);
				//remove payment from list to avoid overriding category ID again in the future
				changedRepeatingPayments.add(currentPayment);
				iterator2.remove();
			}
		}
	}
		
	private void importNormalPayments(ArrayList<NormalPayment> normalPayments)
    {      
		for(NormalPayment currentPayment : normalPayments)
        {
        	handler.addNormalPayment(currentPayment.getAmount(), currentPayment.getDate(), currentPayment.getCategoryID(), currentPayment.getName(), currentPayment.getDescription());
        }
    }
	
	private void importRepeatingPayments(ArrayList<RepeatingPayment> repeatingPayments)
    {      
		for(RepeatingPayment currentPayment : repeatingPayments)
        {
        	handler.addRepeatingPayment(currentPayment.getAmount(), currentPayment.getDate(), currentPayment.getCategoryID(), currentPayment.getName(), currentPayment.getDescription(), currentPayment.getRepeatInterval(), currentPayment.getRepeatEndDate(), currentPayment.getRepeatMonthDay());
        }
    }
}