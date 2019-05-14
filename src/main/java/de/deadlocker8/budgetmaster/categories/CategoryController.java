package de.deadlocker8.budgetmaster.categories;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.utils.Colors;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import de.thecodelabs.utils.util.ColorUtilsNonJavaFX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;


@Controller
public class CategoryController extends BaseController
{
	private final CategoryRepository categoryRepository;
	private final CategoryService categoryService;
	private final HelpersService helpers;
	private final SettingsService settingsService;

	@Autowired
	public CategoryController(CategoryRepository categoryRepository, CategoryService categoryService, HelpersService helpers, SettingsService settingsService)
	{
		this.categoryRepository = categoryRepository;
		this.categoryService = categoryService;
		this.helpers = helpers;
		this.settingsService = settingsService;
	}

	@RequestMapping("/categories")
	public String categories(Model model)
	{
		model.addAttribute("categories", categoryRepository.findAllByOrderByNameAsc());
		model.addAttribute("settings", settingsService.getSettings());
		return "categories/categories";
	}

	@RequestMapping("/categories/{ID}/requestDelete")
	public String requestDeleteCategory(Model model, @PathVariable("ID") Integer ID)
	{
		if(!isDeletable(ID))
		{
			return "redirect:/categories";
		}

		List<Category> allCategories = categoryService.getRepository().findAllByOrderByNameAsc();
		List<Category> availableCategories = allCategories.stream().filter(category -> !category.getID().equals(ID)).collect(Collectors.toList());

		model.addAttribute("categories", allCategories);
		model.addAttribute("availableCategories", availableCategories);
		model.addAttribute("preselectedCategory", categoryService.getRepository().findByType(CategoryType.NONE));

		model.addAttribute("currentCategory", categoryRepository.getOne(ID));
		model.addAttribute("settings", settingsService.getSettings());
		return "categories/categories";
	}

	@RequestMapping(value = "/categories/{ID}/delete", method = RequestMethod.POST)
	public String deleteCategory(Model model, @PathVariable("ID") Integer ID, @ModelAttribute("DestinationCategory") DestinationCategory destinationCategory)
	{
		if(isDeletable(ID))
		{
			categoryService.deleteCategory(ID, destinationCategory.getCategory());
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
		Category emptyCategory = new Category(null, ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREY).toLowerCase(), CategoryType.CUSTOM);
		model.addAttribute("category", emptyCategory);
		model.addAttribute("settings", settingsService.getSettings());
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
		model.addAttribute("settings", settingsService.getSettings());
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
				category.setColor(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREY).toLowerCase());
			}
			model.addAttribute("category", category);
			model.addAttribute("settings", settingsService.getSettings());
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