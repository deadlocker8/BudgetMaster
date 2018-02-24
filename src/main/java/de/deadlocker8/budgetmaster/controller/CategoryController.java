package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class CategoryController extends BaseController
{
	@Autowired
	private CategoryRepository categoryRepository;

	@RequestMapping("/categories")
	public String index(@ModelAttribute("model") ModelMap model)
	{
		model.addAttribute("categories", categoryRepository.findAll());
		return "categories";
	}

	@RequestMapping("/categories/{ID}/requestDelete")
	public String requestDeleteCategory(@ModelAttribute("model") ModelMap model, @PathVariable("ID") Integer ID)
	{
		model.addAttribute("confirm", true);
		model.addAttribute("categories", categoryRepository.findAll());
		model.addAttribute("currentCategory", categoryRepository.getOne(ID));
		return "categories";

		//TODO: default categories are not allowed for deletion --> deny (also hide delete buttons in html)
	}

	@RequestMapping("/categories/{ID}/delete")
	public String deleteCategory(@ModelAttribute("model") ModelMap model, @PathVariable("ID") Integer ID)
	{
		categoryRepository.delete(ID);
		return "redirect:/categories";
	}
}