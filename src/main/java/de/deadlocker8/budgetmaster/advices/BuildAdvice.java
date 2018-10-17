package de.deadlocker8.budgetmaster.advices;

import de.deadlocker8.budgetmaster.Build;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class BuildAdvice
{
	@ModelAttribute("build")
	public Build getBuild()
	{
		return Build.getInstance();
	}
}