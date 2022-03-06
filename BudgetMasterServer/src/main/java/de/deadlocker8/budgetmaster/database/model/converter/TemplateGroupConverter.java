package de.deadlocker8.budgetmaster.database.model.converter;

import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v8.BackupTemplateGroup_v8;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;

public class TemplateGroupConverter implements Converter<TemplateGroup, BackupTemplateGroup_v8>
{
	public TemplateGroup convertToInternalForm(BackupTemplateGroup_v8 backupTemplateGroup)
	{
		if(backupTemplateGroup == null)
		{
			return null;
		}

		final TemplateGroup templateGroup = new TemplateGroup();
		templateGroup.setID(backupTemplateGroup.getID());
		templateGroup.setName(backupTemplateGroup.getName());
		templateGroup.setType(backupTemplateGroup.getType());

		return templateGroup;
	}

	@Override
	public BackupTemplateGroup_v8 convertToExternalForm(TemplateGroup internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		final BackupTemplateGroup_v8 templateGroup = new BackupTemplateGroup_v8();
		templateGroup.setID(internalItem.getID());
		templateGroup.setName(internalItem.getName());
		templateGroup.setType(internalItem.getType());

		return templateGroup;
	}
}
