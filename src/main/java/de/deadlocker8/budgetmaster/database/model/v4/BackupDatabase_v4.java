package de.deadlocker8.budgetmaster.database.model.v4;

import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.JSONIdentifier;
import de.deadlocker8.budgetmaster.database.model.BackupDatabase;
import de.deadlocker8.budgetmaster.database.model.v5.BackupDatabase_v5;

import java.util.ArrayList;
import java.util.List;

public class BackupDatabase_v4 implements BackupDatabase
{
	@SuppressWarnings("unused")
	private final String TYPE = JSONIdentifier.BUDGETMASTER_DATABASE.toString();

	@SuppressWarnings("FieldCanBeLocal")
	private final int VERSION = 4;

	@SuppressWarnings("unused")
	private final transient String INTRODUCED_IN_VERSION = "v2.5.0";

	private List<BackupCategory_v4> categories;
	private List<BackupAccount_v4> accounts;
	private List<BackupTransaction_v4> transactions;
	private List<BackupTemplate_v4> templates;

	public BackupDatabase_v4()
	{
		// for GSON
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

	@Override
	public BackupDatabase upgrade()
	{
		final BackupDatabase_v5 upgradedDatabase = new BackupDatabase_v5();

		upgradedDatabase.setCategories(upgradeItems(categories, List.of()));
		upgradedDatabase.setAccounts(upgradeItems(accounts, List.of()));
		upgradedDatabase.setTransactions(upgradeItems(transactions, List.of()));
		upgradedDatabase.setTemplates(upgradeItems(templates, List.of()));
		upgradedDatabase.setCharts(new ArrayList<>());
		upgradedDatabase.setImages(new ArrayList<>());

		return upgradedDatabase;
	}

	@Override
	public InternalDatabase convertToInternal()
	{
		throw new UnsupportedOperationException();
	}
}
