package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import de.deadlocker8.budgetmaster.services.HelpersService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ReportController extends BaseController
{
	@Autowired
	private SettingsRepository settingsRepository;

	@Autowired
	private HelpersService helpers;

	@RequestMapping("/reports")
	public String reports(Model model, @CookieValue(value = "currentDate", required = false) String cookieDate)
	{
		DateTime date = helpers.getDateTimeFromCookie(cookieDate);

		model.addAttribute("currentDate", date);
		return "reports/reports";
	}

	@RequestMapping("/reports/generate")
	public String generate(Model model, @CookieValue(value = "currentDate", required = false) String cookieDate)
	{
		DateTime date = helpers.getDateTimeFromCookie(cookieDate);

		model.addAttribute("currentDate", date);
		return "reports/reports";
	}
}