package de.deadlocker8.budgetmaster.categories;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.utils.*;
import de.deadlocker8.budgetmaster.utils.notification.Notification;
import de.deadlocker8.budgetmaster.utils.notification.NotificationType;
import de.thecodelabs.utils.util.ColorUtilsNonJavaFX;
import de.thecodelabs.utils.util.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
@RequestMapping(Mappings.CATEGORIES)
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

	@GetMapping
	public String categories(Model model)
	{
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("settings", settingsService.getSettings());
		return "categories/categories";
	}

	@GetMapping("/{ID}/requestDelete")
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
		model.addAttribute("preselectedCategory", categoryService.findByType(CategoryType.NONE));

		model.addAttribute("currentCategory", categoryService.findById(ID).get());
		model.addAttribute("settings", settingsService.getSettings());
		return "categories/categories";
	}

	@PostMapping(value = "/{ID}/delete")
	public String deleteCategory(WebRequest request, @PathVariable("ID") Integer ID, @ModelAttribute("DestinationCategory") DestinationCategory destinationCategory)
	{
		if(categoryService.isDeletable(ID))
		{
			final Optional<Category> categoryOptional = categoryService.findById(ID);
			if(categoryOptional.isPresent())
			{
				final Category categoryToDelete = categoryOptional.get();
				categoryService.deleteCategory(ID, destinationCategory.getCategory());
				WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.category.delete.success", categoryToDelete.getName()), NotificationType.SUCCESS));
			}
		}
		else
		{
			WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.category.delete.not.deletable", String.valueOf(ID)), NotificationType.ERROR));
		}

		return "redirect:/categories";
	}

	@GetMapping("/newCategory")
	public String newCategory(Model model)
	{
		//add custom color (defaults to white here because we are adding a new category instead of editing an existing)
		model.addAttribute("customColor", WHITE);
		Category emptyCategory = new Category(null, ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREY).toLowerCase(), CategoryType.CUSTOM);
		model.addAttribute("category", emptyCategory);
		model.addAttribute("settings", settingsService.getSettings());

		return "categories/newCategory";
	}

	@GetMapping("/{ID}/edit")
	public String editCategory(Model model, @PathVariable("ID") Integer ID)
	{
		Optional<Category> categoryOptional = categoryService.findById(ID);
		if(categoryOptional.isEmpty())
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

	@PostMapping(value = "/newCategory")
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
			categoryService.save(category);
		}

		return "redirect:/categories";
	}
}