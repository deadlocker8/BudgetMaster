package de.deadlocker8.budgetmaster.database;

import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatchList;
import de.deadlocker8.budgetmaster.entities.Category;
import de.deadlocker8.budgetmaster.entities.CategoryType;
import de.deadlocker8.budgetmaster.entities.Transaction;
import de.deadlocker8.budgetmaster.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class DatabaseImporter
{
	@Autowired
	private CategoryRepository categoryRepository;

	private Database database;
	private AccountMatchList accountMatchList;

	public DatabaseImporter(Database database, AccountMatchList accountMatchList)
	{
		this.database = database;
		this.accountMatchList = accountMatchList;
	}

	public void importDatabase()
	{
		importCategories();
//
//		for(AccountMatch accountMatch : accountMatchList.getAccountMatches())
//		{
//
//		}
	}

	private void importCategories()
	{
		List<Transaction> alreadyUpdatedTransactions = new ArrayList<>();

		for(Category category : database.getCategories())
		{
			if(!category.getType().equals(CategoryType.CUSTOM))
			{
				continue;
			}

			int newCategoryID = -1;

			System.out.println("Importing category: " + category);

			Category existingCategory = categoryRepository.findByNameAndColorAndType(category.getName(), category.getColor(), category.getType());
			if(existingCategory == null)
			{
				//category does not exist --> create it
				category.setID(null);
				categoryRepository.save(category);

				Category newCategory = categoryRepository.findByNameAndColorAndType(category.getName(), category.getColor(), category.getType());
				newCategoryID = newCategory.getID();
			}
			else
			{
				//category already exists
				newCategoryID = existingCategory.getID();
			}

			int oldCategoryID = category.getID();

			List<Transaction> transactions = database.getTransactions();
			transactions.removeAll(alreadyUpdatedTransactions);

			alreadyUpdatedTransactions.addAll(updateCategoriesForTransactions(transactions, oldCategoryID, newCategoryID));
		}
	}

	private List<Transaction> updateCategoriesForTransactions(List<Transaction> transactions, int oldCategoryID, int newCategoryID)
	{
		System.out.println("Replacing category id " + oldCategoryID + " with " + newCategoryID);
		List<Transaction> updatedTransactions = new ArrayList<>();
		for(Transaction transaction : transactions)
		{
			if(transaction.getCategory().getID() == oldCategoryID)
			{
				transaction.getCategory().setID(newCategoryID);
				updatedTransactions.add(transaction);
			}
		}

		return updatedTransactions;
	}
}
