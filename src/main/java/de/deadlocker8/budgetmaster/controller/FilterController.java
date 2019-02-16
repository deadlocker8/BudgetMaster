package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.entities.category.Category;
import de.deadlocker8.budgetmaster.filter.FilterCategory;
import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import de.deadlocker8.budgetmaster.services.CategoryService;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.services.TransactionService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@Controller
public class FilterController extends BaseController
{
	@Autowired
	private TransactionService transactionService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private HelpersService helpers;

	@RequestMapping("/filter")
	public String filter(Model model)
	{
		FilterConfiguration filterConfiguration = FilterConfiguration.DEFAULT;
		filterConfiguration.setFilterCategories(getFilterCategories());
		model.addAttribute("filterConfiguration", filterConfiguration);
		return "filter/filter";
	}

	@RequestMapping(value = "/filter/apply", method = RequestMethod.POST)
	public String post(HttpServletResponse response, @ModelAttribute("NewFilterConfiguration") FilterConfiguration filterConfiguration)
	{
		System.out.println(filterConfiguration);
		return "redirect:/filter";
	}

	@RequestMapping("/filter/reset")
	public String reset(Model model)
	{
		FilterConfiguration filterConfiguration = FilterConfiguration.DEFAULT;
		filterConfiguration.setFilterCategories(getFilterCategories());
		model.addAttribute("filterConfiguration", filterConfiguration);
		return "filter/filter";
	}

	private List<FilterCategory> getFilterCategories()
	{
		List<Category> categories = categoryService.getRepository().findAllByOrderByNameAsc();
		List<FilterCategory> filterCategories = new ArrayList<>();
		for(Category category : categories)
		{
			filterCategories.add(new FilterCategory(category.getID(), category.getName(), false));
		}

		return filterCategories;
	}
}