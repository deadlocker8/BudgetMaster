package de.deadlocker8.budgetmaster.categories;

import de.deadlocker8.budgetmaster.services.Resetable;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.utils.Strings;
import de.thecodelabs.utils.util.Localization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService implements Resetable
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private CategoryRepository categoryRepository;

	@Autowired
	public CategoryService(CategoryRepository categoryRepository)
	{
		this.categoryRepository = categoryRepository;

		createDefaults();
	}

	public CategoryRepository getRepository()
	{
		return categoryRepository;
	}

	public void deleteCategory(int ID, Category newCategory)
	{
		Optional<Category> categoryOptional = categoryRepository.findById(ID);
		if(categoryOptional.isEmpty())
		{
			throw new RuntimeException("Can't delete non-existing category with ID: " + ID);
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
		Optional<Category> categoryOptional = getRepository().findById(ID);
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

	public List<Category> getAllCategories()
	{
		localizeDefaultCategories();
		return categoryRepository.findAllByOrderByNameAsc().stream()
				.sorted(Comparator.comparing(c -> c.getName().toLowerCase()))
				.collect(Collectors.toList());
	}

	public void localizeDefaultCategories()
	{
		LOGGER.debug("Updating localization for default categories");

		final Category categoryNone = categoryRepository.findByType(CategoryType.NONE);
		categoryNone.setName(Localization.getString(Strings.CATEGORY_NONE));
		categoryRepository.save(categoryNone);

		final Category categoryRest = categoryRepository.findByType(CategoryType.REST);
		categoryRest.setName(Localization.getString(Strings.CATEGORY_REST));
		categoryRepository.save(categoryRest);
	}
}
