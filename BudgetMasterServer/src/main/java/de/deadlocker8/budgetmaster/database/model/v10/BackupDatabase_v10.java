package de.deadlocker8.budgetmaster.database.model.v10;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.JSONIdentifier;
import de.deadlocker8.budgetmaster.database.model.BackupDatabase;
import de.deadlocker8.budgetmaster.database.model.converter.*;
import de.deadlocker8.budgetmaster.database.model.v11.BackupDatabase_v11;
import de.deadlocker8.budgetmaster.database.model.v5.BackupChart_v5;
import de.deadlocker8.budgetmaster.database.model.v5.BackupImage_v5;
import de.deadlocker8.budgetmaster.database.model.v6.BackupTransaction_v6;
import de.deadlocker8.budgetmaster.database.model.v7.BackupAccount_v7;
import de.deadlocker8.budgetmaster.database.model.v7.BackupCategory_v7;
import de.deadlocker8.budgetmaster.database.model.v8.BackupIcon_v8;
import de.deadlocker8.budgetmaster.database.model.v8.BackupTemplateGroup_v8;
import de.deadlocker8.budgetmaster.database.model.v8.BackupTemplate_v8;
import de.deadlocker8.budgetmaster.database.model.v9.BackupTransactionNameKeyword_v9;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.csvimport.CsvImportSettings;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeyword;

import java.util.List;

public class BackupDatabase_v10 implements BackupDatabase
{
	@SuppressWarnings("unused")
	private final String TYPE = JSONIdentifier.BUDGETMASTER_DATABASE.toString();

	@SuppressWarnings({"FieldCanBeLocal", "squid:S1170"})
	// field can not be static, since static field won't be exported to JSON by GSON
	private final int VERSION = 10;

	@SuppressWarnings({"unused", "squid:S2065", "squid:S1170"})
	// field can not be static, since static field won't be exported to JSON by GSON
	private final transient String INTRODUCED_IN_VERSION = "v2.14.0";

	private List<BackupCategory_v7> categories;
	private List<BackupAccount_v7> accounts;
	private List<BackupTransaction_v6> transactions;
	private List<BackupTemplateGroup_v8> templateGroups;
	private List<BackupTemplate_v8> templates;
	private List<BackupChart_v5> charts;
	private List<BackupImage_v5> images;
	private List<BackupIcon_v8> icons;
	private List<BackupTransactionNameKeyword_v9> transactionNameKeywords;
	private List<BackupCsvImportSettings_v10> csvImportSettings;

	public BackupDatabase_v10()
	{
		// for GSON
	}

	public BackupDatabase_v10(List<BackupCategory_v7> categories, List<BackupAccount_v7> accounts, List<BackupTransaction_v6> transactions, List<BackupTemplateGroup_v8> templateGroups, List<BackupTemplate_v8> templates, List<BackupChart_v5> charts, List<BackupImage_v5> images, List<BackupIcon_v8> icons, List<BackupTransactionNameKeyword_v9> transactionNameKeywords, List<BackupCsvImportSettings_v10> csvImportSettings)
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
		this.csvImportSettings = csvImportSettings;
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

	public List<BackupCsvImportSettings_v10> getCsvImportSettings()
	{
		return csvImportSettings;
	}

	public void setCsvImportSettings(List<BackupCsvImportSettings_v10> csvImportSettings)
	{
		this.csvImportSettings = csvImportSettings;
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
		final BackupDatabase_v11 upgradedDatabase = new BackupDatabase_v11();

		upgradedDatabase.setCategories(categories);
		upgradedDatabase.setAccounts(upgradeItems(accounts, List.of()));
		upgradedDatabase.setTransactions(transactions);
		upgradedDatabase.setTemplateGroups(templateGroups);
		upgradedDatabase.setTemplates(templates);
		upgradedDatabase.setCharts(charts);
		upgradedDatabase.setImages(images);
		upgradedDatabase.setIcons(icons);
		upgradedDatabase.setTransactionNameKeywords(transactionNameKeywords);
		upgradedDatabase.setCsvImportSettings(csvImportSettings);

		return upgradedDatabase;
	}
}
