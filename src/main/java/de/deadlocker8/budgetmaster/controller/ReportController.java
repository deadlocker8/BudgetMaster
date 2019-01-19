package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.reports.ReportSettings;
import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import de.deadlocker8.budgetmaster.services.HelpersService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


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

		ReportSettings reportSettings = new ReportSettings();

		model.addAttribute("reportSettings", reportSettings);
		model.addAttribute("currentDate", date);
		return "reports/reports";
	}

	@RequestMapping(value = "/reports/generate", method = RequestMethod.POST)
	public String post(Model model,
					   @CookieValue(value = "currentDate", required = false) String cookieDate,
					   @ModelAttribute("NewReportSettings") ReportSettings reportSettings,
					   BindingResult bindingResult)
	{
		DateTime date = helpers.getDateTimeFromCookie(cookieDate);

		System.out.println(reportSettings);

		return "redirect:/reports";
	}
}