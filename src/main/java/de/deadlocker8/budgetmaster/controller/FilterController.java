package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.entities.category.Category;
import de.deadlocker8.budgetmaster.entities.category.CategoryType;
import de.deadlocker8.budgetmaster.filter.FilterCategory;
import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import de.deadlocker8.budgetmaster.services.CategoryService;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
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
	public String filter(HttpServletRequest request, Model model)
	{
		FilterConfiguration filterConfiguration;
		Object sessionFilterConfiguration = request.getSession().getAttribute("filterConfiguration");
		if(sessionFilterConfiguration == null)
		{
			filterConfiguration = FilterConfiguration.DEFAULT;
			filterConfiguration.setFilterCategories(getFilterCategories());
		}
		else
		{
			filterConfiguration = (FilterConfiguration)sessionFilterConfiguration;
		}
		model.addAttribute("filterConfiguration", filterConfiguration);
		return "filter/filter";
	}

	@RequestMapping(value = "/filter/apply", method = RequestMethod.POST)
	public String post(WebRequest request, @ModelAttribute("NewFilterConfiguration") FilterConfiguration filterConfiguration)
	{
		request.setAttribute("filterConfiguration", filterConfiguration, WebRequest.SCOPE_SESSION);
		return "redirect:/filter";
	}

	@RequestMapping("/filter/reset")
	public String reset(WebRequest request, Model model)
	{
		FilterConfiguration filterConfiguration = FilterConfiguration.DEFAULT;
		filterConfiguration.setFilterCategories(getFilterCategories());
		request.setAttribute("filterConfiguration", filterConfiguration, WebRequest.SCOPE_SESSION);
		return "redirect:/filter";
	}

	private List<FilterCategory> getFilterCategories()
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