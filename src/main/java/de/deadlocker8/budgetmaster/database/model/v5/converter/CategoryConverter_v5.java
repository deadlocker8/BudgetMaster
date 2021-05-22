package de.deadlocker8.budgetmaster.database.model.v5.converter;

import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.database.model.v5.BackupCategory_v5;

public class CategoryConverter_v5 implements Converter<Category, BackupCategory_v5>
{
	public Category convert(BackupCategory_v5 backupCategory)
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
}
