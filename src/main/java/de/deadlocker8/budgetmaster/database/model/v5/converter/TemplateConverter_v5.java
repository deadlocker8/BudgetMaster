package de.deadlocker8.budgetmaster.database.model.v5.converter;

import de.deadlocker8.budgetmaster.database.model.v5.BackupTemplate_v5;
import de.deadlocker8.budgetmaster.templates.Template;

public class TemplateConverter_v5 implements Converter<Template, BackupTemplate_v5>
{
	public Template convert(BackupTemplate_v5 backupTemplate)
	{
		if(backupTemplate == null)
		{
			return null;
		}

		final Template template = new Template();
		template.setAmount(backupTemplate.getAmount());
		template.setName(backupTemplate.getName());
		template.setCategory(new CategoryConverter_v5().convert(backupTemplate.getCategory()));
		template.setDescription(backupTemplate.getDescription());

		if(backupTemplate.getExpenditure() == null)
		{
			template.setIsExpenditure(true);
		}
		else
		{
			template.setIsExpenditure(backupTemplate.getExpenditure());
		}

		template.setAccount(new AccountConverter_v5().convert(backupTemplate.getAccount()));
		template.setTransferAccount(new AccountConverter_v5().convert(backupTemplate.getTransferAccount()));
		template.setTags(backupTemplate.getTags());
		template.setTemplateName(backupTemplate.getTemplateName());
		template.setIcon(new ImageConverter_v5().convert(backupTemplate.getIcon()));
		return template;
	}
}
