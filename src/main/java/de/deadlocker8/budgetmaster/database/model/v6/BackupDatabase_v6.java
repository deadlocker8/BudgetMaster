package de.deadlocker8.budgetmaster.database.model.v6;

import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.JSONIdentifier;
import de.deadlocker8.budgetmaster.database.model.BackupDatabase;
import de.deadlocker8.budgetmaster.database.model.BackupInfo;
import de.deadlocker8.budgetmaster.database.model.v5.BackupCategory_v5;
import de.deadlocker8.budgetmaster.database.model.v5.BackupChart_v5;
import de.deadlocker8.budgetmaster.database.model.v5.BackupImage_v5;
import de.deadlocker8.budgetmaster.database.model.v7.BackupDatabase_v7;
import de.deadlocker8.budgetmaster.database.model.v7.BackupIcon_v7;

import java.util.ArrayList;
import java.util.List;

public class BackupDatabase_v6 implements BackupDatabase
{
	@SuppressWarnings("unused")
	private final String TYPE = JSONIdentifier.BUDGETMASTER_DATABASE.toString();

	@SuppressWarnings({"FieldCanBeLocal", "squid:S1170"})
	// field can not be static, since static field won't be exported to JSON by GSON
	private final int VERSION = 6;

	@SuppressWarnings({"unused", "squid:S2065", "squid:S1170"})
	// field can not be static, since static field won't be exported to JSON by GSON
	private final transient String INTRODUCED_IN_VERSION = "v2.7.0";

	private List<BackupCategory_v5> categories;
	private List<BackupAccount_v6> accounts;
	private List<BackupTransaction_v6> transactions;
	private List<BackupTemplate_v6> templates;
	private List<BackupChart_v5> charts;
	private List<BackupImage_v5> images;

	public BackupDatabase_v6()
	{
		// for GSON
	}

	public BackupDatabase_v6(List<BackupCategory_v5> categories, List<BackupAccount_v6> accounts, List<BackupTransaction_v6> transactions, List<BackupTemplate_v6> templates, List<BackupChart_v5> charts, List<BackupImage_v5> images)
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

	public List<BackupAccount_v6> getAccounts()
	{
		return accounts;
	}

	public void setAccounts(List<BackupAccount_v6> accounts)
	{
		this.accounts = accounts;
	}

	public List<BackupTransaction_v6> getTransactions()
	{
		return transactions;
	}

	public void setTransactions(List<BackupTransaction_v6> transactions)
	{
		this.transactions = transactions;
	}

	public List<BackupTemplate_v6> getTemplates()
	{
		return templates;
	}

	public void setTemplates(List<BackupTemplate_v6> templates)
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
		final BackupDatabase_v7 upgradedDatabase = new BackupDatabase_v7();

		final List<BackupInfo> newIcons = new ArrayList<>();

		upgradedDatabase.setCategories(upgradeItems(categories, newIcons));
		upgradedDatabase.setAccounts(upgradeItems(accounts, newIcons));
		upgradedDatabase.setTransactions(transactions);
		upgradedDatabase.setTemplates(upgradeItems(templates, newIcons));
		upgradedDatabase.setCharts(charts);
		upgradedDatabase.setImages(images);

		List<BackupIcon_v7> castedIcons = newIcons.stream()
				.map(BackupIcon_v7.class::cast)
				.toList();
		upgradedDatabase.setIcons(castedIcons);

		return upgradedDatabase;
	}
}