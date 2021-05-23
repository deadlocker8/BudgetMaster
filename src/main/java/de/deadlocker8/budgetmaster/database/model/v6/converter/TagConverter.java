package de.deadlocker8.budgetmaster.database.model.v6.converter;

import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v4.BackupTag_v4;
import de.deadlocker8.budgetmaster.tags.Tag;

public class TagConverter implements Converter<Tag, BackupTag_v4>
{
	public Tag convertToInternalForm(BackupTag_v4 backupTag)
	{
		if(backupTag == null)
		{
			return null;
		}

		return new Tag(backupTag.getName());
	}

	@Override
	public BackupTag_v4 convertToExternalForm(Tag internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		return new BackupTag_v4(internalItem.getName());
	}
}
