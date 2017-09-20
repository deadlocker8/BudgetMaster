package de.deadlocker8.budgetmasterserver.logic.database;

import java.util.ArrayList;
import java.util.Iterator;

import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmaster.logic.payment.NormalPayment;
import de.deadlocker8.budgetmaster.logic.payment.RepeatingPayment;
import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmaster.logic.tag.TagMatch;

public class DatabaseImporter
{
	private DatabaseHandler handler;
	private DatabaseTagHandler tagHandler;
	private ArrayList<Category> categories;
	private ArrayList<NormalPayment> normalPayments;
	private ArrayList<RepeatingPayment> repeatingPayments;
	private ArrayList<NormalPayment> changedNormalPayments;
	private ArrayList<RepeatingPayment> changedRepeatingPayments;
	private ArrayList<Tag> tags;
	private ArrayList<TagMatch> tagMatches;
	private ArrayList<TagMatch> changedTagMatches;
	
	public DatabaseImporter(DatabaseHandler handler, DatabaseTagHandler tagHandler) throws IllegalStateException
    {
        this.handler = handler;
        this.tagHandler = tagHandler;
    }
	
	public void importDatabase(Database database)
	{		
		this.categories = database.getCategories();
		this.normalPayments = database.getNormalPayments();
		this.repeatingPayments = database.getRepeatingPayments();	
		this.changedNormalPayments = new ArrayList<>();
		this.changedRepeatingPayments = new ArrayList<>();
		this.tags = database.getTags();
		this.tagMatches = database.getTagMatches();
		this.changedTagMatches = new ArrayList<>();
		
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
        tagMatches.addAll(changedTagMatches);
        changedTagMatches = new ArrayList<>();
        
        importRepeatingPayments(repeatingPayments);
        tagMatches.addAll(changedTagMatches);     
        changedTagMatches = new ArrayList<>();
        
        // import tags
        for(Tag currentTag : tags)
        {
        	int tagID = currentTag.getID();
        
        	Tag existingTag = tagHandler.getTagByName(currentTag.getName());
        	if(existingTag == null)
        	{
        		tagHandler.addTag(currentTag.getName());
        		int newID = tagHandler.getLastInsertID();
        		
        		updateTagMatchesByTagID(tagID, newID);
        	}
        	else
        	{
        		updateTagMatchesByTagID(tagID, existingTag.getID());
        	}
        }
        
        tagMatches.addAll(changedTagMatches);
        importTagMatches(tagMatches);
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
				/*
				 * remove payment from payments list to avoid overriding category ID again on future calls of this method
				 * e.g.:	call 1 = replace ID 2 with 3
				 * 			call 2 = replace ID 3 with 4
				 * --> would replace category IDs in payments where category ID has already been replaced 
				 * --> would lead to wrong import
				 * --> remove payment from list but add to "changedPayments" in order not to loose the payment completly
				 * --> remaining payments in list and all payments from "changedPayments" will be merged after all categories are imported
				 */				
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
				/*
				 * see explanation in NormalPayments loop
				 */
				changedRepeatingPayments.add(currentPayment);
				iterator2.remove();
			}
		}
	}
		
	private void importNormalPayments(ArrayList<NormalPayment> normalPayments)
    {      
		for(NormalPayment currentPayment : normalPayments)
        {
        	int newID = handler.addNormalPayment(currentPayment.getAmount(), currentPayment.getDate(), currentPayment.getCategoryID(), currentPayment.getName(), currentPayment.getDescription());
        	updateTagMatchesByPaymentID(currentPayment.getID(), newID);
        }
    }
	
	private void importRepeatingPayments(ArrayList<RepeatingPayment> repeatingPayments)
    {      
		for(RepeatingPayment currentPayment : repeatingPayments)
        {
        	int newID = handler.addRepeatingPayment(currentPayment.getAmount(), currentPayment.getDate(), currentPayment.getCategoryID(), currentPayment.getName(), currentPayment.getDescription(), currentPayment.getRepeatInterval(), currentPayment.getRepeatEndDate(), currentPayment.getRepeatMonthDay());
        	updateTagMatchesByRepeatingPaymentID(currentPayment.getID(), newID);
        }
    }	
	
	private void updateTagMatchesByTagID(int oldID, int newID)
	{
		//check tag matches for old tag ID
		Iterator<TagMatch> iterator = tagMatches.iterator();        		
		while(iterator.hasNext())
		{
			TagMatch currentTagMatch = iterator.next();
			if(currentTagMatch.getTagID() == oldID)
			{
				currentTagMatch.setTagID(newID);
				/*
				 * see explanation in updatePayments()
				 */				
				changedTagMatches.add(currentTagMatch);
				iterator.remove();
			}
		}
	}
	
	private void updateTagMatchesByPaymentID(int oldID, int newID)
	{
		//check tag matches for old payment ID
		Iterator<TagMatch> iterator = tagMatches.iterator();
		while(iterator.hasNext())
		{
			TagMatch currentTagMatch = iterator.next();
			if(currentTagMatch.getPaymentID() == oldID)
			{
				currentTagMatch.setPaymentID(newID);
				/*
				 * see explanation in updatePayments()
				 */				
				changedTagMatches.add(currentTagMatch);
				iterator.remove();
			}
		}
	}
	
	private void updateTagMatchesByRepeatingPaymentID(int oldID, int newID)
	{
		//check tag matches for old payment ID
		Iterator<TagMatch> iterator = tagMatches.iterator();        		
		while(iterator.hasNext())
		{
			TagMatch currentTagMatch = iterator.next();
			if(currentTagMatch.getRepeatingPaymentID() == oldID)
			{
				currentTagMatch.setRepeatingPaymentID(newID);
				/*
				 * see explanation in updatePayments()
				 */				
				changedTagMatches.add(currentTagMatch);
				iterator.remove();
			}
		}
	}
	
	private void importTagMatches(ArrayList<TagMatch> tagMatches)
	{
		for(TagMatch currentTagMatch : tagMatches)
		{
			if(currentTagMatch.getRepeatingPaymentID() == -1)
			{
				tagHandler.addTagMatchForPayment(currentTagMatch.getTagID(), currentTagMatch.getPaymentID());
			}
			else
			{
				tagHandler.addTagMatchForRepeatingPayment(currentTagMatch.getTagID(), currentTagMatch.getRepeatingPaymentID());
			}
		}
	}
}