package de.deadlocker8.budgetmaster.database.model.v4;

import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.model.BackupDatabase;
import de.deadlocker8.budgetmaster.database.model.Upgradeable;
import de.deadlocker8.budgetmaster.database.model.v5.BackupDatabase_v5;

import java.util.ArrayList;
import java.util.List;

public class BackupDatabase_v4 implements BackupDatabase
{
	private final int VERSION = 4;

	private List<BackupCategory_v4> categories;
	private List<BackupAccount_v4> accounts;
	private List<BackupTransaction_v4> transactions;
	private List<BackupTemplate_v4> templates;

	public BackupDatabase_v4()
	{
	}

	public BackupDatabase_v4(List<BackupCategory_v4> categories, List<BackupAccount_v4> accounts, List<BackupTransaction_v4> transactions, List<BackupTemplate_v4> templates)
	{
		this.categories = categories;
		this.accounts = accounts;
		this.transactions = transactions;
		this.templates = templates;
	}

	public List<BackupCategory_v4> getCategories()
	{
		return categories;
	}

	public void setCategories(List<BackupCategory_v4> categories)
	{
		this.categories = categories;
	}

	public List<BackupAccount_v4> getAccounts()
	{
		return accounts;
	}

	public void setAccounts(List<BackupAccount_v4> accounts)
	{
		this.accounts = accounts;
	}

	public List<BackupTransaction_v4> getTransactions()
	{
		return transactions;
	}

	public void setTransactions(List<BackupTransaction_v4> transactions)
	{
		this.transactions = transactions;
	}

	public List<BackupTemplate_v4> getTemplates()
	{
		return templates;
	}

	public void setTemplates(List<BackupTemplate_v4> templates)
	{
		this.templates = templates;
	}

	@Override
	public int getVersion()
	{
		return this.VERSION;
	}

	public BackupDatabase upgrade()
	{
		final BackupDatabase_v5 upgradedDatabase = new BackupDatabase_v5();

		upgradedDatabase.setCategories(upgradeItems(categories));
		upgradedDatabase.setAccounts(upgradeItems(accounts));
		upgradedDatabase.setTransactions(upgradeItems(transactions));
		upgradedDatabase.setTemplates(upgradeItems(templates));
		upgradedDatabase.setCharts(new ArrayList<>());
		upgradedDatabase.setImages(new ArrayList<>());

		return upgradedDatabase;
	}

	private <T> List<T> upgradeItems(List<? extends Upgradeable<T>> items)
	{
		List<T> upgradedItems = new ArrayList<>();
		for(Upgradeable<T> item : items)
		{
			upgradedItems.add(item.upgrade());
		}
		return upgradedItems;
	}

	@Override
	public InternalDatabase convertToInternal()
	{
		return null;
	}
}
