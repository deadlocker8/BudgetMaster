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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class CategoryController extends BaseController
{
	private static final String WHITE = "#FFFFFF";

	private final CategoryService categoryService;
	private final HelpersService helpers;
	private final SettingsService settingsService;

	@Autowired
	public CategoryController(CategoryService categoryService, HelpersService helpers, SettingsService settingsService)
	{
		this.categoryService = categoryService;
		this.helpers = helpers;
		this.settingsService = settingsService;
	}

	@GetMapping("/categories")
	public String categories(Model model)
	{
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("settings", settingsService.getSettings());
		return "categories/categories";
	}

	@GetMapping("/categories/{ID}/requestDelete")
	public String requestDeleteCategory(Model model, @PathVariable("ID") Integer ID)
	{
		if(!categoryService.isDeletable(ID))
		{
			return "redirect:/categories";
		}

		List<Category> allCategories = categoryService.getAllCategories();
		List<Category> availableCategories = allCategories.stream().filter(category -> !category.getID().equals(ID)).collect(Collectors.toList());

		model.addAttribute("categories", allCategories);
		model.addAttribute("availableCategories", availableCategories);
		model.addAttribute("preselectedCategory", categoryService.getRepository().findByType(CategoryType.NONE));

		model.addAttribute("currentCategory", categoryService.getRepository().getOne(ID));
		model.addAttribute("settings", settingsService.getSettings());
		return "categories/categories";
	}

	@PostMapping(value = "/categories/{ID}/delete")
	public String deleteCategory(Model model, @PathVariable("ID") Integer ID, @ModelAttribute("DestinationCategory") DestinationCategory destinationCategory)
	{
		if(categoryService.isDeletable(ID))
		{
			categoryService.deleteCategory(ID, destinationCategory.getCategory());
		}

		return "redirect:/categories";
	}

	@GetMapping("/categories/newCategory")
	public String newCategory(Model model)
	{
		//add custom color (defaults to white here because we are adding a new category instead of editing an existing)
		model.addAttribute("customColor", WHITE);
		Category emptyCategory = new Category(null, ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREY).toLowerCase(), CategoryType.CUSTOM);
		model.addAttribute("category", emptyCategory);
		model.addAttribute("settings", settingsService.getSettings());
		return "categories/newCategory";
	}

	@GetMapping("/categories/{ID}/edit")
	public String editCategory(Model model, @PathVariable("ID") Integer ID)
	{
		Optional<Category> categoryOptional = categoryService.getRepository().findById(ID);
		if(!categoryOptional.isPresent())
		{
			throw new ResourceNotFoundException();
		}

		Category category = categoryOptional.get();

		if(helpers.getCategoryColorList().contains(category.getColor()))
		{
			model.addAttribute("customColor", WHITE);
		}
		else
		{
			model.addAttribute("customColor", category.getColor());
		}

		model.addAttribute("category", category);
		model.addAttribute("settings", settingsService.getSettings());
		return "categories/newCategory";
	}

	@PostMapping(value = "/categories/newCategory")
	public String post(Model model, @ModelAttribute("NewCategory") Category category, BindingResult bindingResult)
	{
		CategoryValidator userValidator = new CategoryValidator();
		userValidator.validate(category, bindingResult);

		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);

			if(helpers.getCategoryColorList().contains(category.getColor()))
			{
				model.addAttribute("customColor", WHITE);
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
			categoryService.getRepository().save(category);
		}

		return "redirect:/categories";
	}
}