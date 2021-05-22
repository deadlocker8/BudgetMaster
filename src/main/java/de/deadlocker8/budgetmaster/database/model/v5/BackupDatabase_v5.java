package de.deadlocker8.budgetmaster.database.model.v5;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.model.BackupDatabase;
import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v5.converter.*;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.transactions.Transaction;

import java.util.ArrayList;
import java.util.List;

public class BackupDatabase_v5 implements BackupDatabase
{
	private final int VERSION = 5;

	private List<BackupCategory_v5> categories;
	private List<BackupAccount_v5> accounts;
	private List<BackupTransaction_v5> transactions;
	private List<BackupTemplate_v5> templates;
	private List<BackupChart_v5> charts;
	private List<BackupImage_v5> images;

	public BackupDatabase_v5()
	{
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

	private <T, S> List<T> convertItemsToInternal(List<S> backupItems, Converter<T, S> converter)
	{
		List<T> convertedItems = new ArrayList<>();
		for(S backupItem : backupItems)
		{
			convertedItems.add(converter.convertToInternalForm(backupItem));
		}
		return convertedItems;
	}

	@Override
	public int getVersion()
	{
		return this.VERSION;
	}

	@Override
	public BackupDatabase upgrade()
	{
		return null;
	}

	public static BackupDatabase_v5 createFromInternalEntities(InternalDatabase database)
	{
		final List<BackupCategory_v5> convertedCategories = convertItemsToExternal(database.getCategories(), new CategoryConverter_v5());
		final List<BackupAccount_v5> convertedAccounts = convertItemsToExternal(database.getAccounts(), new AccountConverter_v5());
		final List<BackupTransaction_v5> convertedTransactions = convertItemsToExternal(database.getTransactions(), new TransactionConverter_v5());
		final List<BackupTemplate_v5> convertedTemplates = convertItemsToExternal(database.getTemplates(), new TemplateConverter_v5());
		final List<BackupChart_v5> convertedCharts = convertItemsToExternal(database.getCharts(), new ChartConverter_v5());
		final List<BackupImage_v5> convertedImages = convertItemsToExternal(database.getImages(), new ImageConverter_v5());

		return new BackupDatabase_v5(convertedCategories, convertedAccounts, convertedTransactions, convertedTemplates, convertedCharts, convertedImages);
	}

	private static <T, S> List<S> convertItemsToExternal(List<T> backupItems, Converter<T, S> converter)
	{
		List<S> convertedItems = new ArrayList<>();
		for(T backupItem : backupItems)
		{
			convertedItems.add(converter.convertToExternalForm(backupItem));
		}
		return convertedItems;
	}
}
