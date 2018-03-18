package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.entities.Category;
import de.deadlocker8.budgetmaster.entities.CategoryType;
import de.deadlocker8.budgetmaster.entities.Payment;
import de.deadlocker8.budgetmaster.repositories.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private CategoryRepository categoryRepository;

	@Autowired
	public CategoryService(CategoryRepository categoryRepository)
	{
		this.categoryRepository = categoryRepository;

		if(categoryRepository.findByType(CategoryType.NONE) == null)
		{
			categoryRepository.save(new Category("Keine Kategorie", "#FFFFFF", CategoryType.NONE));
			LOGGER.debug("Created default category NONE");
		}

		if(categoryRepository.findByType(CategoryType.REST) == null)
		{
			categoryRepository.save(new Category("Übertrag", "#FFFF00", CategoryType.REST));
			LOGGER.debug("Created default category REST");
		}
	}

	public void deleteCategory(int ID)
	{
		Category categoryToDelete = categoryRepository.findOne(ID);
		for(Payment payment : categoryToDelete.getReferringPayments())
		{
			payment.setCategory(categoryRepository.findByType(CategoryType.NONE));
		}

		categoryRepository.delete(ID);
	}
}
