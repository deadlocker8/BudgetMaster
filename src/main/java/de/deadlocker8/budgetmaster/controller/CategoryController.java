package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.entities.category.Category;
import de.deadlocker8.budgetmaster.entities.category.CategoryType;
import de.deadlocker8.budgetmaster.repositories.CategoryRepository;
import de.deadlocker8.budgetmaster.services.CategoryService;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.utils.Colors;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import de.deadlocker8.budgetmaster.validators.CategoryValidator;
import de.thecodelabs.utils.util.ColorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class CategoryController extends BaseController
{
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private HelpersService helpers;

	@RequestMapping("/categories")
	public String categories(Model model)
	{
		model.addAttribute("categories", categoryRepository.findAllByOrderByNameAsc());
		return "categories/categories";
	}

	@RequestMapping("/categories/{ID}/requestDelete")
	public String requestDeleteCategory(Model model, @PathVariable("ID") Integer ID)
	{
		if(!isDeletable(ID))
		{
			return "redirect:/categories";
		}

		model.addAttribute("categories", categoryRepository.findAllByOrderByNameAsc());
		model.addAttribute("currentCategory", categoryRepository.getOne(ID));
		return "categories/categories";
	}

	@RequestMapping("/categories/{ID}/delete")
	public String deleteCategory(Model model, @PathVariable("ID") Integer ID)
	{
		if(isDeletable(ID))
		{
			categoryService.deleteCategory(ID);
		}

		return "redirect:/categories";
	}

	private boolean isDeletable(Integer ID)
	{
		Category categoryToDelete = categoryRepository.getOne(ID);
		return categoryToDelete != null && categoryToDelete.getType() == CategoryType.CUSTOM;
	}

	@RequestMapping("/categories/newCategory")
	public String newCategory(Model model)
	{
		//add custom color (defaults to white here because we are adding a new category instead of editing an existing)
		model.addAttribute("customColor", "#FFFFFF");
		Category emptyCategory = new Category(null, ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREY).toLowerCase(), CategoryType.CUSTOM);
		model.addAttribute("category", emptyCategory);
		return "categories/newCategory";
	}

	@RequestMapping("/categories/{ID}/edit")
	public String editCategory(Model model, @PathVariable("ID") Integer ID)
	{
		Category category = categoryRepository.findOne(ID);
		if(category == null)
		{
			throw new ResourceNotFoundException();
		}

		if(helpers.getCategoryColorList().contains(category.getColor()))
		{
			model.addAttribute("customColor", "#FFFFFF");
		}
		else
		{
			model.addAttribute("customColor", category.getColor());
		}

		model.addAttribute("category", category);
		return "categories/newCategory";
	}

	@RequestMapping(value = "/categories/newCategory", method = RequestMethod.POST)
	public String post(Model model, @ModelAttribute("NewCategory") Category category, BindingResult bindingResult)
	{
		CategoryValidator userValidator = new CategoryValidator();
		userValidator.validate(category, bindingResult);

		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);

			if(helpers.getCategoryColorList().contains(category.getColor()))
			{
				model.addAttribute("customColor", "#FFFFFF");
			}
			else
			{
				model.addAttribute("customColor", category.getColor());
			}

			if(category.getColor() == null)
			{
				category.setColor(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREY).toLowerCase());
			}
			model.addAttribute("category", category);
			return "categories/newCategory";
		}
		else
		{
			if(category.getType() == null)
			{
				category.setType(CategoryType.CUSTOM);
			}
			categoryRepository.save(category);
		}

		return "redirect:/categories";
	}
}