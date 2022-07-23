package de.deadlocker8.budgetmaster.database.model.v9;

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
import de.deadlocker8.budgetmaster.database.model.v7.BackupAccount_v7;
import de.deadlocker8.budgetmaster.database.model.v7.BackupCategory_v7;
import de.deadlocker8.budgetmaster.database.model.v8.BackupIcon_v8;
import de.deadlocker8.budgetmaster.database.model.v8.BackupTemplateGroup_v8;
import de.deadlocker8.budgetmaster.database.model.v8.BackupTemplate_v8;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeyword;

import java.util.List;

public class BackupDatabase_v9 implements BackupDatabase
{
	@SuppressWarnings("unused")
	private final String TYPE = JSONIdentifier.BUDGETMASTER_DATABASE.toString();

	@SuppressWarnings({"FieldCanBeLocal", "squid:S1170"})
	// field can not be static, since static field won't be exported to JSON by GSON
	private final int VERSION = 9;

	@SuppressWarnings({"unused", "squid:S2065", "squid:S1170"})
	// field can not be static, since static field won't be exported to JSON by GSON
	private final transient String INTRODUCED_IN_VERSION = "v2.11.0";

	private List<BackupCategory_v7> categories;
	private List<BackupAccount_v7> accounts;
	private List<BackupTransaction_v6> transactions;
	private List<BackupTemplateGroup_v8> templateGroups;
	private List<BackupTemplate_v8> templates;
	private List<BackupChart_v5> charts;
	private List<BackupImage_v5> images;
	private List<BackupIcon_v8> icons;
	private List<BackupTransactionNameKeyword_v9> transactionNameKeywords;

	public BackupDatabase_v9()
	{
		// for GSON
	}

	public BackupDatabase_v9(List<BackupCategory_v7> categories, List<BackupAccount_v7> accounts, List<BackupTransaction_v6> transactions, List<BackupTemplateGroup_v8> templateGroups, List<BackupTemplate_v8> templates, List<BackupChart_v5> charts, List<BackupImage_v5> images, List<BackupIcon_v8> icons, List<BackupTransactionNameKeyword_v9> transactionNameKeywords)
	{
		this.categories = categories;
		this.accounts = accounts;
		this.transactions = transactions;
		this.templateGroups = templateGroups;
		this.templates = templates;
		this.charts = charts;
		this.images = images;
		this.icons = icons;
		this.transactionNameKeywords = transactionNameKeywords;
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

	public List<BackupTemplate_v8> getTemplates()
	{
		return templates;
	}

	public void setTemplates(List<BackupTemplate_v8> templates)
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

	public List<BackupIcon_v8> getIcons()
	{
		return icons;
	}

	public void setIcons(List<BackupIcon_v8> icons)
	{
		this.icons = icons;
	}

	public List<BackupTemplateGroup_v8> getTemplateGroups()
	{
		return templateGroups;
	}

	public void setTemplateGroups(List<BackupTemplateGroup_v8> templateGroups)
	{
		this.templateGroups = templateGroups;
	}

	public List<BackupTransactionNameKeyword_v9> getTransactionNameKeywords()
	{
		return transactionNameKeywords;
	}

	public void setTransactionNameKeywords(List<BackupTransactionNameKeyword_v9> transactionNameKeywords)
	{
		this.transactionNameKeywords = transactionNameKeywords;
	}

	public InternalDatabase convertToInternal()
	{
		final List<Image> convertedImages = convertItemsToInternal(this.images, new ImageConverter());
		final List<Icon> convertedIcons = convertItemsToInternal(this.icons, new IconConverter(convertedImages));
		final List<Category> convertedCategories = convertItemsToInternal(categories, new CategoryConverter(convertedIcons));
		final List<Account> convertedAccounts = convertItemsToInternal(accounts, new AccountConverter(convertedIcons));
		final List<Transaction> convertedTransactions = convertItemsToInternal(this.transactions, new TransactionConverter(convertedCategories, convertedAccounts));
		final List<TemplateGroup> convertedTemplateGroups = convertItemsToInternal(this.templateGroups, new TemplateGroupConverter());
		final List<Template> convertedTemplates = convertItemsToInternal(this.templates, new TemplateConverter(convertedIcons, convertedCategories, convertedAccounts, convertedTemplateGroups));
		final List<Chart> convertedCharts = convertItemsToInternal(this.charts, new ChartConverter());
		final List<TransactionNameKeyword> convertedKeywords = convertItemsToInternal(this.transactionNameKeywords, new TransactionNameKeywordConverter());

		return new InternalDatabase(convertedCategories, convertedAccounts, convertedTransactions, convertedTemplateGroups, convertedTemplates, convertedCharts, convertedImages, convertedIcons, convertedKeywords);
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

	public static BackupDatabase_v9 createFromInternalEntities(InternalDatabase database)
	{
		final BackupDatabase_v9 externalDatabase = new BackupDatabase_v9();

		externalDatabase.setIcons(externalDatabase.convertItemsToExternal(database.getIcons(), new IconConverter(null)));
		externalDatabase.setCategories(externalDatabase.convertItemsToExternal(database.getCategories(), new CategoryConverter(null)));
		externalDatabase.setAccounts(externalDatabase.convertItemsToExternal(database.getAccounts(), new AccountConverter(null)));
		externalDatabase.setTransactions(externalDatabase.convertItemsToExternal(database.getTransactions(), new TransactionConverter(null, null)));
		externalDatabase.setTemplateGroups(externalDatabase.convertItemsToExternal(database.getTemplateGroups(), new TemplateGroupConverter()));
		externalDatabase.setTemplates(externalDatabase.convertItemsToExternal(database.getTemplates(), new TemplateConverter(null, null, null, null)));
		externalDatabase.setCharts(externalDatabase.convertItemsToExternal(database.getCharts(), new ChartConverter()));
		externalDatabase.setImages(externalDatabase.convertItemsToExternal(database.getImages(), new ImageConverter()));
		externalDatabase.setTransactionNameKeywords(externalDatabase.convertItemsToExternal(database.getTransactionNameKeywords(), new TransactionNameKeywordConverter()));

		return externalDatabase;
	}
}
