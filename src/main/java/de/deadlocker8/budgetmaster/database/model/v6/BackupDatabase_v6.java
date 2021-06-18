package de.deadlocker8.budgetmaster.database.model.v6;

import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.JSONIdentifier;
import de.deadlocker8.budgetmaster.database.model.BackupDatabase;
import de.deadlocker8.budgetmaster.database.model.v5.BackupCategory_v5;
import de.deadlocker8.budgetmaster.database.model.v5.BackupChart_v5;
import de.deadlocker8.budgetmaster.database.model.v5.BackupImage_v5;
import de.deadlocker8.budgetmaster.database.model.v7.BackupDatabase_v7;
import de.deadlocker8.budgetmaster.database.model.v7.BackupIcon_v7;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BackupDatabase_v6 implements BackupDatabase
{
	@SuppressWarnings("unused")
	private final String TYPE = JSONIdentifier.BUDGETMASTER_DATABASE.toString();

	@SuppressWarnings("FieldCanBeLocal")
	private final int VERSION = 6;

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

		final List<BackupIcon_v7> newIcons = new ArrayList<>();

		final List<String> builtInIcons = categories.stream()
				.filter(category -> category.getIcon() != null && !category.getIcon().isEmpty())
				.map(BackupCategory_v5::getIcon)
				.collect(Collectors.toList());

		for(String builtInIcon : builtInIcons)
		{
			final BackupIcon_v7 icon = new BackupIcon_v7(newIcons.size(), null, builtInIcon);
			newIcons.add(icon);
		}

		final Set<Integer> usedImageIDs = accounts.stream()
				.map(BackupAccount_v6::getIconID)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());

		usedImageIDs.addAll(templates.stream()
				.map(BackupTemplate_v6::getIconID)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet()));

		for(Integer imageID : usedImageIDs)
		{
			final BackupIcon_v7 icon = new BackupIcon_v7(newIcons.size(), imageID, null);
			newIcons.add(icon);
		}

		upgradedDatabase.setCategories(upgradeItems(categories, newIcons));
		upgradedDatabase.setAccounts(upgradeItems(accounts, newIcons));
		upgradedDatabase.setTransactions(transactions);
		upgradedDatabase.setTemplates(upgradeItems(templates, newIcons));
		upgradedDatabase.setCharts(charts);
		upgradedDatabase.setImages(images);
		upgradedDatabase.setIcons(newIcons);

		return upgradedDatabase;
	}
}