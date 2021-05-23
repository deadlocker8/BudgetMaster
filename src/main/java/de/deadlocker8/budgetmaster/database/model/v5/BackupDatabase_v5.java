package de.deadlocker8.budgetmaster.database.model.v5;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.JSONIdentifier;
import de.deadlocker8.budgetmaster.database.model.BackupDatabase;
import de.deadlocker8.budgetmaster.database.model.v5.converter.*;
import de.deadlocker8.budgetmaster.database.model.v6.BackupDatabase_v6;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.transactions.Transaction;

import java.util.List;

public class BackupDatabase_v5 implements BackupDatabase
{
	@SuppressWarnings("unused")
	private final String TYPE = JSONIdentifier.BUDGETMASTER_DATABASE.toString();

	@SuppressWarnings("FieldCanBeLocal")
	private final int VERSION = 5;

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
		final List<Category> convertedCategories = convertItemsToInternal(categories, new CategoryConverter_v5());
		final List<Account> convertedAccounts = convertItemsToInternal(accounts, new AccountConverter_v5());
		final List<Transaction> convertedTransactions = convertItemsToInternal(this.transactions, new TransactionConverter_v5());
		final List<Template> convertedTemplates = convertItemsToInternal(this.templates, new TemplateConverter_v5());
		final List<Chart> convertedCharts = convertItemsToInternal(this.charts, new ChartConverter_v5());
		final List<Image> convertedImages = convertItemsToInternal(this.images, new ImageConverter_v5());

		return new InternalDatabase(convertedCategories, convertedAccounts, convertedTransactions, convertedTemplates, convertedCharts, convertedImages);
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
		upgradedDatabase.setAccounts(upgradeItems(accounts));
		upgradedDatabase.setTransactions(upgradeItems(transactions));
		upgradedDatabase.setTemplates(upgradeItems(templates));
		upgradedDatabase.setCharts(charts);
		upgradedDatabase.setImages(images);

		return upgradedDatabase;
	}

	public static BackupDatabase_v5 createFromInternalEntities(InternalDatabase database)
	{
		final BackupDatabase_v5 externalDatabase = new BackupDatabase_v5();

		externalDatabase.setCategories(externalDatabase.convertItemsToExternal(database.getCategories(), new CategoryConverter_v5()));
		externalDatabase.setAccounts(externalDatabase.convertItemsToExternal(database.getAccounts(), new AccountConverter_v5()));
		externalDatabase.setTransactions(externalDatabase.convertItemsToExternal(database.getTransactions(), new TransactionConverter_v5()));
		externalDatabase.setTemplates(externalDatabase.convertItemsToExternal(database.getTemplates(), new TemplateConverter_v5()));
		externalDatabase.setCharts(externalDatabase.convertItemsToExternal(database.getCharts(), new ChartConverter_v5()));
		externalDatabase.setImages(externalDatabase.convertItemsToExternal(database.getImages(), new ImageConverter_v5()));

		return externalDatabase;
	}
}
