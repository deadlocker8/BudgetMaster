package de.deadlocker8.budgetmaster.advices;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class GlobalControllerAdvice
{
	@InitBinder
	public void initBinder(WebDataBinder binder)
	{
		StringTrimmerEditor trimmer = new StringTrimmerEditor(false);
		binder.registerCustomEditor(String.class, trimmer);
	}
}
