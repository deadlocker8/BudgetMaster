package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.entities.Settings;
import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import de.deadlocker8.budgetmaster.services.HelpersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class SettingsController extends BaseController
{
	@Autowired
	private SettingsRepository settingsRepository;

	@Autowired
	private HelpersService helpers;

	@RequestMapping("/settings")
	public String settings(Model model)
	{
		model.addAttribute("settings", settingsRepository.findOne(0));
		return "settings";
	}

	@RequestMapping(value = "/settings/save", method = RequestMethod.POST)
	public String post(Model model, @ModelAttribute("Settings") Settings settings, BindingResult bindingResult)
	{
//		CategoryValidator userValidator = new CategoryValidator();
//		userValidator.validate(category, bindingResult);
//
//		if(bindingResult.hasErrors())
//		{
//			model.addAttribute("error", bindingResult);
//
//			if(helpers.getCategoryColorList().contains(category.getColor()))
//			{
//				model.addAttribute("customColor", "#FFFFFF");
//			}
//			else
//			{
//				model.addAttribute("customColor", category.getColor());
//			}
//
//			if(category.getColor() == null)
//			{
//				category.setColor(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREY).toLowerCase());
//			}
//			model.addAttribute("category", category);
//			return "categories/newCategory";
//		}
//		else
//		{
//			category.setType(CategoryType.CUSTOM);
//			categoryRepository.save(category);
//		}

		return "redirect:/settings";
	}
}