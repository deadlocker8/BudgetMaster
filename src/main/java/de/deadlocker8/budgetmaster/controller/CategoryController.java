package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.entities.Category;
import de.deadlocker8.budgetmaster.entities.CategoryType;
import de.deadlocker8.budgetmaster.repositories.CategoryRepository;
import de.deadlocker8.budgetmaster.utils.Colors;
import de.deadlocker8.budgetmaster.utils.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import tools.ConvertTo;

import java.util.ArrayList;


@Controller
public class CategoryController extends BaseController
{
	@Autowired
	private CategoryRepository categoryRepository;

	@RequestMapping("/categories")
	public String index(@ModelAttribute("model") ModelMap model)
	{
		model.addAttribute("categories", categoryRepository.findAllByOrderByNameAsc());
		return "categories";
	}

	@RequestMapping("/categories/{ID}/requestDelete")
	public String requestDeleteCategory(@ModelAttribute("model") ModelMap model, @PathVariable("ID") Integer ID)
	{
		if(!isDeletable(ID))
		{
			return "redirect:/categories";
		}

		model.addAttribute("confirm", true);
		model.addAttribute("categories", categoryRepository.findAll());
		model.addAttribute("currentCategory", categoryRepository.getOne(ID));
		return "categories";
	}

	@RequestMapping("/categories/{ID}/delete")
	public String deleteCategory(@ModelAttribute("model") ModelMap model, @PathVariable("ID") Integer ID)
	{
		if(isDeletable(ID))
		{
			categoryRepository.delete(ID);
		}

		return "redirect:/categories";
	}

	private boolean isDeletable(Integer ID)
	{
		Category categoryToDelete = categoryRepository.getOne(ID);
		return categoryToDelete != null && categoryToDelete.getType() == CategoryType.CUSTOM;
	}

	@RequestMapping("/categories/add")
	public String add(@ModelAttribute("model") ModelMap model)
	{
		//TODO: add color picker for custom colors
		//TODO: get values from ftl form
		//TODO: validate wrong inputs --> empty name --> message on page --> validate before send?

		ArrayList<String> categoryColors = Helpers.getCategoryColorList();
		//add custom color (defaults to white here because we are adding a new category instead of editing an existing)
		categoryColors.add("#FFFFFF");
		model.addAttribute("categoryColors", categoryColors);

		model.addAttribute("activeColor", ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREY));
		return "newCategory";
	}
}