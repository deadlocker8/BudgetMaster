package de.deadlocker8.budgetmaster.advices;

import de.deadlocker8.budgetmaster.utils.Helpers;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;

@ControllerAdvice
public class ColorAdvice
{
	@ModelAttribute("categoryColors")
	public ArrayList<String> getCategoryColors()
	{
		return Helpers.getCategoryColorList();
	}
}