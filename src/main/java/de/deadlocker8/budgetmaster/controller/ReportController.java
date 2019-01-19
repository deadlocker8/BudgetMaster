package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.entities.report.ReportColumn;
import de.deadlocker8.budgetmaster.entities.report.ReportSettings;
import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.services.report.ReportColumnService;
import de.deadlocker8.budgetmaster.services.report.ReportSettingsService;
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
	private ReportSettingsService reportSettingsService;

	@Autowired
	private ReportColumnService reportColumnService;

	@Autowired
	private HelpersService helpers;

	@RequestMapping("/reports")
	public String reports(Model model, @CookieValue(value = "currentDate", required = false) String cookieDate)
	{
		DateTime date = helpers.getDateTimeFromCookie(cookieDate);

		model.addAttribute("reportSettings", reportSettingsService.getReportSettings());
		model.addAttribute("currentDate", date);
		return "reports/reports";
	}

	@RequestMapping(value = "/reports/generate", method = RequestMethod.POST)
	public String post(Model model,
					   @ModelAttribute("NewReportSettings") ReportSettings reportSettings,
					   BindingResult bindingResult)
	{
		reportSettingsService.getRepository().delete(0);
		for(ReportColumn reportColumn : reportSettings.getColumns())
		{
			reportColumnService.getRepository().save(reportColumn);
		}
		reportSettingsService.getRepository().save(reportSettings);

		return "redirect:/reports";
	}
}