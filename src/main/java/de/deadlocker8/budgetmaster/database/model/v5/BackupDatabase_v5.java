package de.deadlocker8.budgetmaster.database.model.v5;

import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.JSONIdentifier;
import de.deadlocker8.budgetmaster.database.model.BackupDatabase;
import de.deadlocker8.budgetmaster.database.model.v6.BackupDatabase_v6;

import java.util.List;

public class BackupDatabase_v5 implements BackupDatabase
{
	@SuppressWarnings("unused")
	private final String TYPE = JSONIdentifier.BUDGETMASTER_DATABASE.toString();

	@SuppressWarnings("FieldCanBeLocal")
	private final int VERSION = 5;

	private final String INTRODUCED_IN_VERSION = "v2.6.0";

	private List<BackupCategory_v5> categories;
	private List<BackupAccount_v5> accounts;
	private List<BackupTransaction_v5> transactions;
	private List<BackupTemplate_v5> templates;
	private List<BackupChart_v5> charts;
	private List<BackupImage_v5> images;

	public BackupDatabase_v5()
	{
		// for GSON
	}

	public BackupDatabase_v5(List<BackupCategory_v5> categories, List<BackupAccount_v5> accounts, List<BackupTransaction_v5> transactions, List<BackupTemplate_v5> templates, List<BackupChart_v5> charts, List<BackupImage_v5> images)
	{
		this.categories = categories;
		this.accounts = accounts;
		this.transactions = transactions;
		this.templates = templates;
		this.charts = charts;
		this.images = images;
	}

	public List<BackupCategory_v5> getCategories()
	{
		return categories;
	}

	public void setCategories(List<BackupCategory_v5> categories)
	{
		this.categories = categories;
	}

	public List<BackupAccount_v5> getAccounts()
	{
		return accounts;
	}

	public void setAccounts(List<BackupAccount_v5> accounts)
	{
		this.accounts = accounts;
	}

	public List<BackupTransaction_v5> getTransactions()
	{
		return transactions;
	}

	public void setTransactions(List<BackupTransaction_v5> transactions)
	{
		this.transactions = transactions;
	}

	public List<BackupTemplate_v5> getTemplates()
	{
		return templates;
	}

	public void setTemplates(List<BackupTemplate_v5> templates)
	{
		this.templates = templates;
	}

	public List<BackupChart_v5> getCharts()
	{
		return charts;
	}

	public void setCharts(List<BackupChart_v5> charts)
	{
		this.charts = charts;
	}

	public List<BackupImage_v5> getImages()
	{
		return images;
	}

	public void setImages(List<BackupImage_v5> images)
	{
		this.images = images;
	}

	public InternalDatabase convertToInternal()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int getVersion()
	{
		return VERSION;
	}

	@Override
	public BackupDatabase upgrade()
	{
		final BackupDatabase_v6 upgradedDatabase = new BackupDatabase_v6();

		upgradedDatabase.setCategories(categories);
		upgradedDatabase.setAccounts(upgradeItems(accounts, List.of()));
		upgradedDatabase.setTransactions(upgradeItems(transactions, List.of()));
		upgradedDatabase.setTemplates(upgradeItems(templates, List.of()));
		upgradedDatabase.setCharts(charts);
		upgradedDatabase.setImages(images);

		return upgradedDatabase;
	}
}
