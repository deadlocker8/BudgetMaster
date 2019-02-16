package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.entities.category.Category;
import de.deadlocker8.budgetmaster.entities.category.CategoryType;
import de.deadlocker8.budgetmaster.filter.FilterCategory;
import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilterHelpersService
{
	@Autowired
	private CategoryService categoryService;

	public FilterConfiguration getFilterConfiguration(HttpServletRequest request)
	{
		FilterConfiguration filterConfiguration;
		Object sessionFilterConfiguration = request.getSession().getAttribute("filterConfiguration");
		if(sessionFilterConfiguration == null)
		{
			filterConfiguration = FilterConfiguration.DEFAULT;
			filterConfiguration.setFilterCategories(getFilterCategories());
			return filterConfiguration;
		}

		return (FilterConfiguration)sessionFilterConfiguration;
	}

	public List<FilterCategory> getFilterCategories()
	{
		List<Category> categories = categoryService.getRepository().findAllByOrderByNameAsc();
		List<FilterCategory> filterCategories = new ArrayList<>();
		for(Category category : categories)
		{
			if(!category.getType().equals(CategoryType.REST))
			{
				filterCategories.add(new FilterCategory(category.getID(), category.getName(), true));
			}
		}

		return filterCategories;
	}
}