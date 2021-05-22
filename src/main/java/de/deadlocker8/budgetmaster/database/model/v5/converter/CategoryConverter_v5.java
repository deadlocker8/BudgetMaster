package de.deadlocker8.budgetmaster.database.model.v5.converter;

import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v5.BackupCategory_v5;

public class CategoryConverter_v5 implements Converter<Category, BackupCategory_v5>
{
	public Category convertToInternalForm(BackupCategory_v5 backupCategory)
	{
		if(backupCategory == null)
		{
			return null;
		}

		final Category category = new Category();
		category.setID(backupCategory.getID());
		category.setName(backupCategory.getName());
		category.setColor(backupCategory.getColor());
		category.setType(backupCategory.getType());
		category.setIcon(backupCategory.getIcon());
		return category;
	}

	@Override
	public BackupCategory_v5 convertToExternalForm(Category internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		final BackupCategory_v5 category = new BackupCategory_v5();
		category.setID(internalItem.getID());
		category.setName(internalItem.getName());
		category.setColor(internalItem.getColor());
		category.setType(internalItem.getType());
		category.setIcon(internalItem.getIcon());
		return category;
	}
}
