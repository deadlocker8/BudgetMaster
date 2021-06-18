package de.deadlocker8.budgetmaster.database.model.v7;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.JSONIdentifier;
import de.deadlocker8.budgetmaster.database.model.BackupDatabase;
import de.deadlocker8.budgetmaster.database.model.converter.*;
import de.deadlocker8.budgetmaster.database.model.v5.BackupChart_v5;
import de.deadlocker8.budgetmaster.database.model.v5.BackupImage_v5;
import de.deadlocker8.budgetmaster.database.model.v6.BackupTransaction_v6;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.transactions.Transaction;

import java.util.List;

public class BackupDatabase_v7 implements BackupDatabase
{
	@SuppressWarnings("unused")
	private final String TYPE = JSONIdentifier.BUDGETMASTER_DATABASE.toString();

	@SuppressWarnings("FieldCanBeLocal")
	private final int VERSION = 7;

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
		final List<Image> convertedImages = convertItemsToInternal(this.images, new ImageConverter());
		final List<Icon> convertedIcons = convertItemsToInternal(this.icons, new IconConverter(convertedImages));
		final List<Category> convertedCategories = convertItemsToInternal(categories, new CategoryConverter(convertedIcons));
		final List<Account> convertedAccounts = convertItemsToInternal(accounts, new AccountConverter(convertedIcons));
		final List<Transaction> convertedTransactions = convertItemsToInternal(this.transactions, new TransactionConverter(convertedCategories, convertedAccounts));
		final List<Template> convertedTemplates = convertItemsToInternal(this.templates, new TemplateConverter(convertedIcons, convertedCategories, convertedAccounts));
		final List<Chart> convertedCharts = convertItemsToInternal(this.charts, new ChartConverter());

		return new InternalDatabase(convertedCategories, convertedAccounts, convertedTransactions, convertedTemplates, convertedCharts, convertedImages, convertedIcons);
	}

	@Override
	public int getVersion()
	{
		return VERSION;
	}

	@Override
	public BackupDatabase upgrade()
	{
		throw new UnsupportedOperationException();
	}

	public static BackupDatabase_v7 createFromInternalEntities(InternalDatabase database)
	{
		final BackupDatabase_v7 externalDatabase = new BackupDatabase_v7();

		externalDatabase.setIcons(externalDatabase.convertItemsToExternal(database.getIcons(), new IconConverter(null)));
		externalDatabase.setCategories(externalDatabase.convertItemsToExternal(database.getCategories(), new CategoryConverter(null)));
		externalDatabase.setAccounts(externalDatabase.convertItemsToExternal(database.getAccounts(), new AccountConverter(null)));
		externalDatabase.setTransactions(externalDatabase.convertItemsToExternal(database.getTransactions(), new TransactionConverter(null, null)));
		externalDatabase.setTemplates(externalDatabase.convertItemsToExternal(database.getTemplates(), new TemplateConverter(null, null, null)));
		externalDatabase.setCharts(externalDatabase.convertItemsToExternal(database.getCharts(), new ChartConverter()));
		externalDatabase.setImages(externalDatabase.convertItemsToExternal(database.getImages(), new ImageConverter()));

		return externalDatabase;
	}
}
