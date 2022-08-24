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
	protected int importSingleItem(Category categoryToImport) throws ImportException
	{
		if(!(repository instanceof CategoryRepository repository))
		{
			throw new IllegalArgumentException("Invalid repository type");
		}

		LOGGER.debug(MessageFormat.format("Importing category {0}", categoryToImport.getName()));


		if(categoryToImport.getType().equals(CategoryType.NONE) || categoryToImport.getType().equals(CategoryType.REST))
		{
			// is default category
			final Category existingCategory = repository.findByType(categoryToImport.getType());
			categoryToImport.setIconReference(existingCategory.getIconReference());
			LOGGER.debug(MessageFormat.format("Found matching category with ID: {0} for default category \"{1}\".", existingCategory.getID(), categoryToImport.getName()));
			return existingCategory.getID();
		}

		final Category categoryToCreate = new Category(categoryToImport.getName(), categoryToImport.getColor(), categoryToImport.getType(), categoryToImport.getIconReference());
		repository.save(categoryToCreate);

		return repository.findByNameAndColorAndType(categoryToImport.getName(), categoryToImport.getColor(), categoryToImport.getType()).getID();
	}

	@Override
	protected String getNameForItem(Category item)
	{
		return item.getName();
	}
}
