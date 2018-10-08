package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.entities.Category;
import de.deadlocker8.budgetmaster.entities.CategoryType;
import de.deadlocker8.budgetmaster.entities.Transaction;
import de.deadlocker8.budgetmaster.repositories.CategoryRepository;
import de.deadlocker8.budgetmaster.utils.Strings;
import de.tobias.logger.Logger;
import de.tobias.utils.util.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements Resetable
{
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
			Logger.debug("Created default category NONE");
		}

		if(categoryRepository.findByType(CategoryType.REST) == null)
		{
			categoryRepository.save(new Category(Localization.getString(Strings.CATEGORY_REST), "#FFFF00", CategoryType.REST));
			Logger.debug("Created default category REST");
		}
	}
}
