package de.deadlocker8.budgetmaster.database.importer;

import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.services.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class CategoryImporter extends ItemImporter<Category>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryImporter.class);

	public CategoryImporter(CategoryRepository categoryRepository)
	{
		super(categoryRepository, EntityType.CATEGORY);
	}

	@Override
	protected int importSingleItem(Category category)
	{
		if(!(repository instanceof CategoryRepository repository))
		{
			throw new IllegalArgumentException("Invalid repository type");
		}

		LOGGER.debug(MessageFormat.format("Importing category {0}", category.getName()));

		Category existingCategory;
		if(category.getType().equals(CategoryType.NONE) || category.getType().equals(CategoryType.REST))
		{
			existingCategory = repository.findByType(category.getType());
		}
		else
		{
			existingCategory = repository.findByNameAndColorAndType(category.getName(), category.getColor(), category.getType());
		}

		int newCategoryID;
		if(existingCategory == null)
		{
			//category does not exist --> create it
			LOGGER.debug(MessageFormat.format("No matching category found for category \"{0}\". Creating new one...", category.getName()));

			Category categoryToCreate = new Category(category.getName(), category.getColor(), category.getType(), category.getIconReference());
			repository.save(categoryToCreate);

			Category newCategory = repository.findByNameAndColorAndType(category.getName(), category.getColor(), category.getType());
			newCategoryID = newCategory.getID();
		}
		else
		{
			//category already exists
			newCategoryID = existingCategory.getID();
			category.setIconReference(existingCategory.getIconReference());
			LOGGER.debug(MessageFormat.format("Found matching category with ID: {0} for category \"{1}\".", newCategoryID, category.getName()));
		}
		return newCategoryID;
	}

	@Override
	protected String getNameForItem(Category item)
	{
		return item.getName();
	}
}
