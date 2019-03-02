package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.entities.category.Category;
import de.deadlocker8.budgetmaster.entities.category.CategoryType;
import de.deadlocker8.budgetmaster.entities.tag.Tag;
import de.deadlocker8.budgetmaster.filter.FilterObject;
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

	@Autowired
	private TagService tagService;

	public FilterConfiguration getFilterConfiguration(HttpServletRequest request)
	{
		Object sessionFilterConfiguration = request.getSession().getAttribute("filterConfiguration");
		if(sessionFilterConfiguration == null)
		{
			FilterConfiguration filterConfiguration = FilterConfiguration.DEFAULT;
			filterConfiguration.setFilterCategories(getFilterCategories());
			filterConfiguration.setFilterTags(getFilterTags());
			return filterConfiguration;
		}

		// update categories and tags
		FilterConfiguration filterConfiguration = (FilterConfiguration)sessionFilterConfiguration;
		filterConfiguration.setFilterCategories(updateObjects(filterConfiguration.getFilterCategories(), getFilterCategories()));
		filterConfiguration.setFilterTags(updateObjects(filterConfiguration.getFilterTags(), getFilterTags()));

		return filterConfiguration;
	}

	private List<FilterObject> updateObjects(List<FilterObject> oldObjects, List<FilterObject> newObjects)
	{
		for(FilterObject newObject : newObjects)
		{
			for(FilterObject existingObject : oldObjects)
			{
				if(existingObject.getID().equals(newObject.getID()))
				{
					newObject.setInclude(existingObject.isInclude());
					break;
				}
			}
		}

		return newObjects;
	}

	public List<FilterObject> getFilterCategories()
	{
		List<Category> categories = categoryService.getRepository().findAllByOrderByNameAsc();
		List<FilterObject> filterCategories = new ArrayList<>();
		for(Category category : categories)
		{
			if(!category.getType().equals(CategoryType.REST))
			{
				filterCategories.add(new FilterObject(category.getID(), category.getName(), true));
			}
		}

		return filterCategories;
	}

	public List<FilterObject> getFilterTags()
	{
		List<Tag> tags = tagService.getRepository().findAllByOrderByNameAsc();
		List<FilterObject> filterTags = new ArrayList<>();
		for(Tag tag : tags)
		{
			filterTags.add(new FilterObject(tag.getID(), tag.getName(), true));
		}

		return filterTags;
	}
}