package de.deadlocker8.budgetmaster.database.model.converter;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v4.BackupTag_v4;
import de.deadlocker8.budgetmaster.database.model.v8.BackupTemplate_v8;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.templates.Template;

import java.util.ArrayList;
import java.util.List;

public class TemplateConverter implements Converter<Template, BackupTemplate_v8>
{
	private final List<Icon> availableIcons;
	private final List<Category> availableCategories;
	private final List<Account> availableAccounts;
	private final List<TemplateGroup> availableTemplateGroups;

	public TemplateConverter(List<Icon> availableIcons, List<Category> availableCategories, List<Account> availableAccounts, List<TemplateGroup> availableTemplateGroups)
	{
		this.availableIcons = availableIcons;
		this.availableCategories = availableCategories;
		this.availableAccounts = availableAccounts;
		this.availableTemplateGroups = availableTemplateGroups;
	}

	public Template convertToInternalForm(BackupTemplate_v8 backupTemplate)
	{
		if(backupTemplate == null)
		{
			return null;
		}

		final Template template = new Template();
		template.setAmount(backupTemplate.getAmount());
		template.setName(backupTemplate.getName());
		template.setCategory(getItemById(availableCategories, backupTemplate.getCategoryID()));
		template.setDescription(backupTemplate.getDescription());

		if(backupTemplate.getExpenditure() == null)
		{
			template.setIsExpenditure(true);
		}
		else
		{
			template.setIsExpenditure(backupTemplate.getExpenditure());
		}

		template.setAccount(getItemById(availableAccounts, backupTemplate.getAccountID()));
		template.setTransferAccount(getItemById(availableAccounts, backupTemplate.getTransferAccountID()));

		List<Tag> convertedTags = new ArrayList<>();
		TagConverter tagConverter = new TagConverter();
		for(BackupTag_v4 tag : backupTemplate.getTags())
		{
			convertedTags.add(tagConverter.convertToInternalForm(tag));
		}
		template.setTags(convertedTags);

		template.setTemplateName(backupTemplate.getTemplateName());
		template.setIconReference(getItemById(availableIcons, backupTemplate.getIconReferenceID()));
		template.setTemplateGroup(getItemById(availableTemplateGroups, backupTemplate.getTemplateGroupID()));

		return template;
	}

	@Override
	public BackupTemplate_v8 convertToExternalForm(Template internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		final BackupTemplate_v8 template = new BackupTemplate_v8();
		template.setAmount(internalItem.getAmount());
		template.setName(internalItem.getName());

		if(internalItem.getCategory() != null)
		{
			template.setCategoryID(internalItem.getCategory().getID());
		}

		template.setDescription(internalItem.getDescription());
		template.setExpenditure(internalItem.getExpenditure());

		if(internalItem.getAccount() != null)
		{
			template.setAccountID(internalItem.getAccount().getID());
		}

		if(internalItem.getTransferAccount() != null)
		{
			template.setTransferAccountID(internalItem.getTransferAccount().getID());
		}

		List<BackupTag_v4> convertedTags = new ArrayList<>();
		TagConverter tagConverter = new TagConverter();
		for(Tag tag : internalItem.getTags())
		{
			convertedTags.add(tagConverter.convertToExternalForm(tag));
		}
		template.setTags(convertedTags);

		template.setTemplateName(internalItem.getTemplateName());

		final Icon icon = internalItem.getIconReference();
		if(icon != null)
		{
			template.setIconReferenceID(icon.getID());
		}

		template.setTemplateGroupID(internalItem.getTemplateGroup().getID());

		return template;
	}
}
