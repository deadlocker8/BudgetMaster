package de.deadlocker8.budgetmaster.categories;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.utils.*;
import de.deadlocker8.budgetmaster.utils.notification.Notification;
import de.deadlocker8.budgetmaster.utils.notification.NotificationLinkBuilder;
import de.deadlocker8.budgetmaster.utils.notification.NotificationType;
import de.thecodelabs.utils.util.ColorUtilsNonJavaFX;
import de.thecodelabs.utils.util.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping(Mappings.CATEGORIES)
public class CategoryController extends BaseController
{
	private static class ModelAttributes
	{
		public static final String ALL_ENTITIES = "categories";
		public static final String ONE_ENTITY = "category";
		public static final String ENTITY_TO_DELETE = "categoryToDelete";
		public static final String CUSTOM_COLOR = "customColor";
		public static final String FONTAWESOME_ICONS = "fontawesomeIcons";
		public static final String AVAILABLE_CATEGORIES = "availableCategories";
		public static final String PRESELECTED_CATEGORY = "preselectedCategory";
		public static final String ERROR = "error";
	}

	private static class ReturnValues
	{
		public static final String SHOW_ALL = "categories/categories";
		public static final String REDIRECT_SHOW_ALL = "redirect:/categories";
		public static final String NEW_ENTITY = "categories/newCategory";
		public static final String DELETE_ENTITY = "categories/deleteCategoryModal";
	}

	private static final String WHITE = "#FFFFFF";

	private final CategoryService categoryService;
	private final HelpersService helpers;
	private final IconService iconService;

	@Autowired
	public CategoryController(CategoryService categoryService, HelpersService helpers, IconService iconService)
	{
		this.categoryService = categoryService;
		this.helpers = helpers;
		this.iconService = iconService;
	}

	@GetMapping
	public String categories(Model model)
	{
		model.addAttribute(ModelAttributes.ALL_ENTITIES, categoryService.getAllEntitiesAsc());
		return ReturnValues.SHOW_ALL;
	}

	@GetMapping("/{ID}/requestDelete")
	public String requestDeleteCategory(Model model, @PathVariable("ID") Integer ID)
	{
		if(!categoryService.isDeletable(ID))
		{
			return ReturnValues.REDIRECT_SHOW_ALL;
		}

		List<Category> allCategories = categoryService.getAllEntitiesAsc();
		List<Category> availableCategories = allCategories.stream().filter(category -> !category.getID().equals(ID)).toList();

		model.addAttribute(ModelAttributes.ALL_ENTITIES, allCategories);
		model.addAttribute(ModelAttributes.AVAILABLE_CATEGORIES, availableCategories);
		model.addAttribute(ModelAttributes.PRESELECTED_CATEGORY, categoryService.findByType(CategoryType.NONE));

		model.addAttribute(ModelAttributes.ENTITY_TO_DELETE, categoryService.findById(ID).orElseThrow());
		return ReturnValues.DELETE_ENTITY;
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

		return ReturnValues.REDIRECT_SHOW_ALL;
	}

	@GetMapping("/newCategory")
	public String newCategory(Model model)
	{
		//add custom color (defaults to white here because we are adding a new category instead of editing an existing)
		model.addAttribute(ModelAttributes.CUSTOM_COLOR, WHITE);
		Category emptyCategory = new Category(null, ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREY).toLowerCase(), CategoryType.CUSTOM);
		model.addAttribute(ModelAttributes.ONE_ENTITY, emptyCategory);
		model.addAttribute(ModelAttributes.FONTAWESOME_ICONS, FontAwesomeIcons.ICONS);

		return ReturnValues.NEW_ENTITY;
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
		prepareModel(model, category);
		return ReturnValues.NEW_ENTITY;
	}

	@PostMapping(value = "/newCategory")
	public String post(HttpServletRequest servletRequest,
					   WebRequest request,
					   Model model, @ModelAttribute("NewCategory") Category category, BindingResult bindingResult,
					   @RequestParam(value = "iconImageID", required = false) Integer iconImageID,
					   @RequestParam(value = "builtinIconIdentifier", required = false) String builtinIconIdentifier,
					   @RequestParam(value = "fontColor", required = false) String fontColor)
	{
		CategoryValidator userValidator = new CategoryValidator();
		userValidator.validate(category, bindingResult);

		category.updateIcon(iconService, iconImageID, builtinIconIdentifier, fontColor, categoryService);

		if(bindingResult.hasErrors())
		{
			model.addAttribute(ModelAttributes.ERROR, bindingResult);

			prepareModel(model, category);
			return ReturnValues.NEW_ENTITY;
		}

		if(category.getType() == null)
		{
			category.setType(CategoryType.CUSTOM);
		}

		category = categoryService.save(category);

		final String link = NotificationLinkBuilder.buildEditLink(servletRequest, category.getName(), Mappings.CATEGORIES, category.getID());
		WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.category.save.success", link), NotificationType.SUCCESS));

		return ReturnValues.REDIRECT_SHOW_ALL;
	}

	private void prepareModel(Model model, Category category)
	{
		if(helpers.getCategoryColorList().contains(category.getColor()))
		{
			model.addAttribute(ModelAttributes.CUSTOM_COLOR, WHITE);
		}
		else
		{
			model.addAttribute(ModelAttributes.CUSTOM_COLOR, category.getColor());
		}

		if(category.getColor() == null)
		{
			category.setColor(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREY).toLowerCase());
		}

		model.addAttribute(ModelAttributes.ONE_ENTITY, category);
		model.addAttribute(ModelAttributes.FONTAWESOME_ICONS, FontAwesomeIcons.ICONS);
	}
}