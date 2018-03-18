package de.deadlocker8.budgetmaster.advices;

import de.deadlocker8.budgetmaster.services.HelpersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class HelpersAdvice
{
	@Autowired
	HelpersService helpers;

	@ModelAttribute("helpers")
	public HelpersService getHelpers()
	{
		return helpers;
	}
}