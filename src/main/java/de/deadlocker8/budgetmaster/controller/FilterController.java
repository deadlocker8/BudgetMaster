package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import de.deadlocker8.budgetmaster.services.CategoryService;
import de.deadlocker8.budgetmaster.services.FilterHelpersService;
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


@Controller
public class FilterController extends BaseController
{
	@Autowired
	private TransactionService transactionService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private HelpersService helpers;

	@Autowired
	private FilterHelpersService filterHelpers;

	@RequestMapping(value = "/filter/apply", method = RequestMethod.POST)
	public String post(WebRequest request, @ModelAttribute("NewFilterConfiguration") FilterConfiguration filterConfiguration)
	{
		request.setAttribute("filterConfiguration", filterConfiguration, WebRequest.SCOPE_SESSION);
		return "redirect:" + request.getHeader("Referer");
	}

	@RequestMapping("/filter/reset")
	public String reset(WebRequest request)
	{
		FilterConfiguration filterConfiguration = FilterConfiguration.DEFAULT;
		filterConfiguration.setFilterCategories(filterHelpers.getFilterCategories());
		filterConfiguration.setFilterTags(filterHelpers.getFilterTags());
		request.setAttribute("filterConfiguration", filterConfiguration, WebRequest.SCOPE_SESSION);
		return "redirect:" + request.getHeader("Referer");
	}
}