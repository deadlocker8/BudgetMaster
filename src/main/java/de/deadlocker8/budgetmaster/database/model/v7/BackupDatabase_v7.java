package de.deadlocker8.budgetmaster.database.model.v7;

import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.JSONIdentifier;
import de.deadlocker8.budgetmaster.database.model.BackupDatabase;
import de.deadlocker8.budgetmaster.database.model.v5.BackupChart_v5;
import de.deadlocker8.budgetmaster.database.model.v5.BackupImage_v5;
import de.deadlocker8.budgetmaster.database.model.v6.BackupTransaction_v6;
import de.deadlocker8.budgetmaster.database.model.v8.BackupDatabase_v8;

import java.util.List;

public class BackupDatabase_v7 implements BackupDatabase
{
	@SuppressWarnings("unused")
	private final String TYPE = JSONIdentifier.BUDGETMASTER_DATABASE.toString();

	@SuppressWarnings("FieldCanBeLocal")
	private final int VERSION = 7;

	@SuppressWarnings("unused")
	private final transient String INTRODUCED_IN_VERSION = "v2.8.0";

	private List<BackupCategory_v7> categories;
	private List<BackupAccount_v7> accounts;
	private List<BackupTransaction_v6> transactions;
	private List<BackupTemplate_v7> templates;
	private List<BackupChart_v5> charts;
	private List<BackupImage_v5> images;
	private List<BackupIcon_v7> icons;

	public BackupDatabase_v7()
	{
		// for GSON
	}

	public BackupDatabase_v7(List<BackupCategory_v7> categories, List<BackupAccount_v7> accounts, List<BackupTransaction_v6> transactions, List<BackupTemplate_v7> templates, List<BackupChart_v5> charts, List<BackupImage_v5> images, List<BackupIcon_v7> icons)
	{
		this.categories = categories;
		this.accounts = accounts;
		this.transactions = transactions;
		this.templates = templates;
		this.charts = charts;
		this.images = images;
		this.icons = icons;
	}

	public List<BackupCategory_v7> getCategories()
	{
		return categories;
	}

	public void setCategories(List<BackupCategory_v7> categories)
	{
		this.categories = categories;
	}

	public List<BackupAccount_v7> getAccounts()
	{
		return accounts;
	}

	public void setAccounts(List<BackupAccount_v7> accounts)
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

	public List<BackupTemplate_v7> getTemplates()
	{
		return templates;
	}

	public void setTemplates(List<BackupTemplate_v7> templates)
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

	public List<BackupIcon_v7> getIcons()
	{
		return icons;
	}

	public void setIcons(List<BackupIcon_v7> icons)
	{
		this.icons = icons;
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
		final BackupDatabase_v8 upgradedDatabase = new BackupDatabase_v8();

		upgradedDatabase.setCategories(categories);
		upgradedDatabase.setAccounts(accounts);
		upgradedDatabase.setTransactions(transactions);
		upgradedDatabase.setTemplateGroups(List.of());
		upgradedDatabase.setTemplates(upgradeItems(templates, List.of()));
		upgradedDatabase.setCharts(charts);
		upgradedDatabase.setImages(images);
		upgradedDatabase.setIcons(upgradeItems(icons, List.of()));

		return upgradedDatabase;
	}
}
