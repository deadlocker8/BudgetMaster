package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.entities.category.Category;
import de.deadlocker8.budgetmaster.entities.category.CategoryType;
import de.deadlocker8.budgetmaster.entities.Transaction;
import de.deadlocker8.budgetmaster.repositories.CategoryRepository;
import de.deadlocker8.budgetmaster.utils.Strings;
import de.thecodelabs.utils.util.Localization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

	public void deleteCategory(int ID)
	{
		Category categoryToDelete = categoryRepository.findOne(ID);
		List<Transaction> referringTransactions = categoryToDelete.getReferringTransactions();
		if(referringTransactions != null)
		{
			for(Transaction transaction : referringTransactions)
			{
				transaction.setCategory(categoryRepository.findByType(CategoryType.NONE));
			}
		}

		categoryRepository.delete(ID);
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
}
