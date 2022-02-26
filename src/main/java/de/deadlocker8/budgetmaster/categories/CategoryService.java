package de.deadlocker8.budgetmaster.categories;

import de.deadlocker8.budgetmaster.services.AccessAllEntities;
import de.deadlocker8.budgetmaster.services.AccessEntityByID;
import de.deadlocker8.budgetmaster.services.Resettable;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.utils.Strings;
import de.thecodelabs.utils.util.Localization;
import org.padler.natorder.NaturalOrderComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CategoryService implements Resettable, AccessAllEntities<Category>, AccessEntityByID<Category>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
	private final CategoryRepository categoryRepository;

	@Autowired
	public CategoryService(CategoryRepository categoryRepository)
	{
		this.categoryRepository = categoryRepository;

		createDefaults();
	}

	@Override
	public Optional<Category> findById(Integer ID)
	{
		return categoryRepository.findById(ID);
	}

	public Category findByType(CategoryType type)
	{
		return categoryRepository.findByType(type);
	}

	public Category save(Category category)
	{
		return categoryRepository.save(category);
	}

	public void deleteCategory(int ID, Category newCategory)
	{
		Optional<Category> categoryOptional = categoryRepository.findById(ID);
		if(categoryOptional.isEmpty())
		{
			throw new NoSuchElementException("Can't delete non-existing category with ID: " + ID);
		}

		Category categoryToDelete = categoryOptional.get();
		List<Transaction> referringTransactions = categoryToDelete.getReferringTransactions();
		if(referringTransactions != null)
		{
			for(Transaction transaction : referringTransactions)
			{
				transaction.setCategory(newCategory);
			}
		}

		categoryRepository.deleteById(ID);
	}

	@SuppressWarnings("OptionalIsPresent")
	public boolean isDeletable(Integer ID)
	{
		Optional<Category> categoryOptional = findById(ID);
		if(categoryOptional.isPresent())
		{
			return categoryOptional.get().getType() == CategoryType.CUSTOM;
		}

		return false;
	}

	@Override
	public void deleteAll()
	{
		categoryRepository.deleteAll();
	}

	@Override
	public void createDefaults()
	{
		if(categoryRepository.findByType(CategoryType.NONE) == null)
		{
			categoryRepository.save(new Category(Localization.getString(Strings.CATEGORY_NONE), "#FFFFFF", CategoryType.NONE));
			LOGGER.debug("Created default category NONE");
		}

		if(categoryRepository.findByType(CategoryType.REST) == null)
		{
			categoryRepository.save(new Category(Localization.getString(Strings.CATEGORY_REST), "#FFFF00", CategoryType.REST));
			LOGGER.debug("Created default category REST");
		}
	}

	@Override
	public List<Category> getAllEntitiesAsc()
	{
		localizeDefaultCategories();
		final List<Category> categories = categoryRepository.findAllByOrderByNameAsc();
		categories.sort((c1, c2) -> new NaturalOrderComparator().compare(c1.getName(), c2.getName()));
		return categories;
	}

	public List<Category> getAllCustomCategories()
	{
		final List<Category> categories = categoryRepository.findAllByTypeOrderByNameAsc(CategoryType.CUSTOM);
		categories.sort((c1, c2) -> new NaturalOrderComparator().compare(c1.getName(), c2.getName()));
		return categories;
	}

	public void localizeDefaultCategories()
	{
		LOGGER.trace("Updating localization for default categories");

		final Category categoryNone = categoryRepository.findByType(CategoryType.NONE);
		categoryNone.setName(Localization.getString(Strings.CATEGORY_NONE));
		categoryRepository.save(categoryNone);

		final Category categoryRest = categoryRepository.findByType(CategoryType.REST);
		categoryRest.setName(Localization.getString(Strings.CATEGORY_REST));
		categoryRepository.save(categoryRest);
	}
}
