package de.deadlocker8.budgetmaster.filter;

import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.TagService;
import de.thecodelabs.utils.util.Localization;
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
		return updateCategoriesAndTags((FilterConfiguration) sessionFilterConfiguration);
	}

	public FilterConfiguration updateCategoriesAndTags(FilterConfiguration filterConfiguration)
	{
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
		List<Category> categories = categoryService.getAllCategories();
		List<FilterObject> filterCategories = new ArrayList<>();
		for(Category category : categories)
		{
			switch(category.getType())
			{
				case NONE:
					filterCategories.add(new FilterObject(category.getID(), Localization.getString("category.none"), true));
					break;
				case REST:
					continue;
				case CUSTOM:
					filterCategories.add(new FilterObject(category.getID(), category.getName(), true));
					break;
			}
		}

		return filterCategories;
	}

	public List<FilterObject> getFilterTags()
	{
		List<Tag> tags = tagService.getAllTagsAsc();
		List<FilterObject> filterTags = new ArrayList<>();
		for(Tag tag : tags)
		{
			filterTags.add(new FilterObject(tag.getID(), tag.getName(), true));
		}

		return filterTags;
	}
}