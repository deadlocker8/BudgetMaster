package de.deadlocker8.budgetmaster.advices;

import de.deadlocker8.budgetmaster.services.HelpersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class ColorAdvice
{
	@Autowired
	private HelpersService helpers;

	@ModelAttribute("categoryColors")
	public List<String> getCategoryColors()
	{
		return helpers.getCategoryColorList();
	}
}