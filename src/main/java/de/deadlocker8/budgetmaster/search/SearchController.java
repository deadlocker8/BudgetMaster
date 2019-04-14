package de.deadlocker8.budgetmaster.search;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.services.HelpersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class SearchController extends BaseController
{
	@Autowired
	private HelpersService helpers;

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String post(@ModelAttribute("NewSearch") Search search)
	{
		System.out.println(search);
		return "redirect:/";
	}
}