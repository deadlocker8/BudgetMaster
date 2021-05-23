package de.deadlocker8.budgetmaster.database.model.v6.converter;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v4.BackupTag_v4;
import de.deadlocker8.budgetmaster.database.model.v5.converter.TagConverter_v5;
import de.deadlocker8.budgetmaster.database.model.v6.BackupTemplate_v6;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.templates.Template;

import java.util.ArrayList;
import java.util.List;

public class TemplateConverter implements Converter<Template, BackupTemplate_v6>
{
	private final List<Image> availableIcons;
	private final List<Category> availableCategories;
	private final List<Account> availableAccounts;

	public TemplateConverter(List<Image> availableIcons, List<Category> availableCategories, List<Account> availableAccounts)
	{
		this.availableIcons = availableIcons;
		this.availableCategories = availableCategories;
		this.availableAccounts = availableAccounts;
	}

	public Template convertToInternalForm(BackupTemplate_v6 backupTemplate)
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
		TagConverter_v5 tagConverter = new TagConverter_v5();
		for(BackupTag_v4 tag : backupTemplate.getTags())
		{
			convertedTags.add(tagConverter.convertToInternalForm(tag));
		}
		template.setTags(convertedTags);

		template.setTemplateName(backupTemplate.getTemplateName());
		template.setIcon(getItemById(availableIcons, backupTemplate.getIconID()));
		return template;
	}

	@Override
	public BackupTemplate_v6 convertToExternalForm(Template internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		final BackupTemplate_v6 template = new BackupTemplate_v6();
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
		TagConverter_v5 tagConverter = new TagConverter_v5();
		for(Tag tag : internalItem.getTags())
		{
			convertedTags.add(tagConverter.convertToExternalForm(tag));
		}
		template.setTags(convertedTags);

		template.setTemplateName(internalItem.getTemplateName());

		if(internalItem.getIcon() != null)
		{
			template.setIconID(internalItem.getIcon().getID());
		}
		return template;
	}
}
