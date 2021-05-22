package de.deadlocker8.budgetmaster.database.model.v5.converter;

import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v5.BackupTemplate_v5;
import de.deadlocker8.budgetmaster.templates.Template;

public class TemplateConverter_v5 implements Converter<Template, BackupTemplate_v5>
{
	public Template convertToInternalForm(BackupTemplate_v5 backupTemplate)
	{
		if(backupTemplate == null)
		{
			return null;
		}

		final Template template = new Template();
		template.setAmount(backupTemplate.getAmount());
		template.setName(backupTemplate.getName());
		template.setCategory(new CategoryConverter_v5().convertToInternalForm(backupTemplate.getCategory()));
		template.setDescription(backupTemplate.getDescription());

		if(backupTemplate.getExpenditure() == null)
		{
			template.setIsExpenditure(true);
		}
		else
		{
			template.setIsExpenditure(backupTemplate.getExpenditure());
		}

		template.setAccount(new AccountConverter_v5().convertToInternalForm(backupTemplate.getAccount()));
		template.setTransferAccount(new AccountConverter_v5().convertToInternalForm(backupTemplate.getTransferAccount()));
		template.setTags(backupTemplate.getTags());
		template.setTemplateName(backupTemplate.getTemplateName());
		template.setIcon(new ImageConverter_v5().convertToInternalForm(backupTemplate.getIcon()));
		return template;
	}

	@Override
	public BackupTemplate_v5 convertToExternalForm(Template internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		final BackupTemplate_v5 template = new BackupTemplate_v5();
		template.setAmount(internalItem.getAmount());
		template.setName(internalItem.getName());
		template.setCategory(new CategoryConverter_v5().convertToExternalForm(internalItem.getCategory()));
		template.setDescription(internalItem.getDescription());
		template.setExpenditure(internalItem.getExpenditure());
		template.setAccount(new AccountConverter_v5().convertToExternalForm(internalItem.getAccount()));
		template.setTransferAccount(new AccountConverter_v5().convertToExternalForm(internalItem.getTransferAccount()));
		template.setTags(internalItem.getTags());
		template.setTemplateName(internalItem.getTemplateName());
		template.setIcon(new ImageConverter_v5().convertToExternalForm(internalItem.getIcon()));
		return template;
	}
}
