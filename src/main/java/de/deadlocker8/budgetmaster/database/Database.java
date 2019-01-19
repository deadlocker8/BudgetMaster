package de.deadlocker8.budgetmaster.database;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.entities.account.Account;
import de.deadlocker8.budgetmaster.entities.category.Category;
import de.deadlocker8.budgetmaster.entities.Transaction;

import java.util.List;

public class Database
{
	@Expose
	private final String TYPE = "BUDGETMASTER_DATABASE";

	@Expose
	private final int VERSION = 3;

	@Expose
	private List<Category> categories;

	@Expose
	private List<Account> accounts;

	@Expose
	private List<Transaction> transactions;

	public Database()
	{
	}

	public Database(List<Category> categories, List<Account> accounts, List<Transaction> transactions)
	{
		this.categories = categories;
		this.accounts = accounts;
		this.transactions = transactions;
	}

	public List<Category> getCategories()
	{
		return categories;
	}

	public List<Account> getAccounts()
	{
		return accounts;
	}

	public List<Transaction> getTransactions()
	{
		return transactions;
	}
}