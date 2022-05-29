package de.deadlocker8.budgetmaster.database.model.converter;

import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v7.BackupCategory_v7;
import de.deadlocker8.budgetmaster.icon.Icon;

import java.util.List;

public class CategoryConverter implements Converter<Category, BackupCategory_v7>
{
	private final List<Icon> availableIcons;

	public CategoryConverter(List<Icon> availableIcons)
	{
		this.availableIcons = availableIcons;
	}


	public Category convertToInternalForm(BackupCategory_v7 backupCategory)
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
		category.setIconReference(getItemById(availableIcons, backupCategory.getIconReferenceID()));

		return category;
	}

	@Override
	public BackupCategory_v7 convertToExternalForm(Category internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		final BackupCategory_v7 category = new BackupCategory_v7();
		category.setID(internalItem.getID());
		category.setName(internalItem.getName());
		category.setColor(internalItem.getColor());
		category.setType(internalItem.getType());

		final Icon icon = internalItem.getIconReference();
		if(icon != null)
		{
			category.setIconReferenceID(icon.getID());
		}

		return category;
	}
}
