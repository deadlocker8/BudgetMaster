package de.deadlocker8.budgetmaster.database;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.transactions.Transaction;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InternalDatabase
{
	private List<Category> categories;
	private List<Account> accounts;
	private List<Transaction> transactions;
	private List<TemplateGroup> templateGroups;
	private List<Template> templates;
	private List<Chart> charts;
	private List<Image> images;
	private List<Icon> icons;

	public InternalDatabase()
	{
	}

	public InternalDatabase(List<Category> categories, List<Account> accounts, List<Transaction> transactions, List<TemplateGroup> templateGroups, List<Template> templates, List<Chart> charts, List<Image> images, List<Icon> icons)
	{
		this.categories = categories;
		this.accounts = accounts;
		this.transactions = transactions;
		this.templateGroups = templateGroups;
		this.templates = templates;
		this.charts = charts;
		this.images = images;
		this.icons = icons;
	}

	public List<Category> getCategories()
	{
		return categories;
	}

	public List<Account> getAccounts()
	{
		return accounts;
	}

	public List<Transaction> getTransactions()
	{
		return transactions;
	}

	public List<TemplateGroup> getTemplateGroups()
	{
		return templateGroups;
	}

	public List<Template> getTemplates()
	{
		return templates;
	}

	public List<Chart> getCharts()
	{
		return charts;
	}

	public List<Image> getImages()
	{
		return images;
	}

	public List<Icon> getIcons()
	{
		return icons;
	}

	public Map<EntityType, Integer> getNumberOfEntitiesByType()
	{
		final Map<EntityType, Integer> numberOfEntitiesByType = new LinkedHashMap<>();

		numberOfEntitiesByType.put(EntityType.CATEGORY, categories.size());

		final List<Account> customAccounts = accounts.stream()
				.filter(account -> account.getType() == AccountType.CUSTOM)
				.collect(Collectors.toList());
		numberOfEntitiesByType.put(EntityType.ACCOUNT, customAccounts.size());

		numberOfEntitiesByType.put(EntityType.TRANSACTION, transactions.size());
		numberOfEntitiesByType.put(EntityType.TEMPLATE, templates.size());
		numberOfEntitiesByType.put(EntityType.TEMPLATE_GROUP, templateGroups.size());
		numberOfEntitiesByType.put(EntityType.IMAGE, images.size());
		numberOfEntitiesByType.put(EntityType.CHART, charts.size());
		return numberOfEntitiesByType;
	}
}