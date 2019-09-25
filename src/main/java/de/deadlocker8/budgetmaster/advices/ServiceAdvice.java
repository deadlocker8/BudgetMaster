package de.deadlocker8.budgetmaster.advices;

import de.deadlocker8.budgetmaster.services.CurrencyService;
import de.deadlocker8.budgetmaster.services.DateService;
import de.deadlocker8.budgetmaster.services.HelpersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ServiceAdvice
{
	@Autowired
	HelpersService helpers;

	@Autowired
	DateService dateService;

	@Autowired
	CurrencyService currencyService;

	@ModelAttribute("helpers")
	public HelpersService getHelpers()
	{
		return helpers;
	}

	@ModelAttribute("dateService")
	public DateService getDateService()
	{
		return dateService;
	}

	@ModelAttribute("currencyService")
	public CurrencyService getCurrencyService()
	{
		return currencyService;
	}
}