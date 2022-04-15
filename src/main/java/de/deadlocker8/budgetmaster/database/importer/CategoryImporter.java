package de.deadlocker8.budgetmaster.database.importer;

import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.services.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Optional;

public class CategoryImporter extends ItemImporter<Category>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryImporter.class);

	public CategoryImporter(CategoryRepository categoryRepository)
	{
		super(categoryRepository, EntityType.CATEGORY);
	}

	@Override
	protected int importSingleItem(Category categoryToImport)
	{
		if(!(repository instanceof CategoryRepository repository))
		{
			throw new IllegalArgumentException("Invalid repository type");
		}

		LOGGER.debug(MessageFormat.format("Importing category {0}", categoryToImport.getName()));

		final Optional<Category> existingCategoryOptional = findExistingCategory(categoryToImport, repository);

		int newCategoryID;
		if(existingCategoryOptional.isEmpty())
		{
			// category does not exist --> create it
			final Category newCategory = createCategory(categoryToImport, repository);
			newCategoryID = newCategory.getID();
		}
		else
		{
			// category already exists
			final Category existingCategory = existingCategoryOptional.get();
			newCategoryID = existingCategory.getID();
			categoryToImport.setIconReference(existingCategory.getIconReference());
			LOGGER.debug(MessageFormat.format("Found matching category with ID: {0} for category \"{1}\".", newCategoryID, categoryToImport.getName()));
		}
		return newCategoryID;
	}

	private Optional<Category> findExistingCategory(Category categoryToImport, CategoryRepository repository)
	{
		if(categoryToImport.getType().equals(CategoryType.NONE) || categoryToImport.getType().equals(CategoryType.REST))
		{
			return Optional.of(repository.findByType(categoryToImport.getType()));
		}
		else
		{
			return Optional.ofNullable(repository.findByNameAndColorAndType(categoryToImport.getName(), categoryToImport.getColor(), categoryToImport.getType()));
		}
	}

	private Category createCategory(Category categoryToImport, CategoryRepository repository)
	{
		LOGGER.debug(MessageFormat.format("No matching category found for category \"{0}\". Creating new one...", categoryToImport.getName()));

		final Category categoryToCreate = new Category(categoryToImport.getName(), categoryToImport.getColor(), categoryToImport.getType(), categoryToImport.getIconReference());
		repository.save(categoryToCreate);

		return repository.findByNameAndColorAndType(categoryToImport.getName(), categoryToImport.getColor(), categoryToImport.getType());
	}

	@Override
	protected String getNameForItem(Category item)
	{
		return item.getName();
	}
}
