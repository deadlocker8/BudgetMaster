package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.services.DateService;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsRepository;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@Controller
public class DatePickerController extends BaseController
{
	private final String DATE_FORMAT = "dd.MM.yy";

	private final SettingsRepository settingsRepository;
	private final DateService dateService;

	@Autowired
	public DatePickerController(SettingsRepository settingsRepository, DateService dateService)
	{
		this.settingsRepository = settingsRepository;
		this.dateService = dateService;
	}

	@RequestMapping(value = "/previousMonth")
	public String previousMonth(HttpServletResponse response, @CookieValue("currentDate") String date, @RequestParam("target") String target)
	{
		Settings settings = settingsRepository.findOne(0);
		DateTime currentDate = DateTime.parse(date, DateTimeFormat.forPattern(DATE_FORMAT).withLocale(settings.getLanguage().getLocale()));
		currentDate = currentDate.minusMonths(1);

		response.addCookie(new Cookie("currentDate", dateService.getDateStringNormal(currentDate)));
		return "redirect:" + target;
	}

	@RequestMapping(value = "/nextMonth")
	public String nextMonth(HttpServletResponse response, @CookieValue("currentDate") String date, @RequestParam("target") String target)
	{
		Settings settings = settingsRepository.findOne(0);
		DateTime currentDate = DateTime.parse(date, DateTimeFormat.forPattern(DATE_FORMAT).withLocale(settings.getLanguage().getLocale()));
		currentDate = currentDate.plusMonths(1);

		response.addCookie(new Cookie("currentDate", dateService.getDateStringNormal(currentDate)));
		return "redirect:" + target;
	}

	@RequestMapping(value = "/setDate")
	public String setDate(HttpServletResponse response, @CookieValue("currentDate") String date, @RequestParam("target") String target)
	{
		Settings settings = settingsRepository.findOne(0);
		DateTime currentDate = DateTime.parse(date, DateTimeFormat.forPattern(DATE_FORMAT).withLocale(settings.getLanguage().getLocale()));

		response.addCookie(new Cookie("currentDate", dateService.getDateStringNormal(currentDate)));
		return "redirect:" + target;
	}

	@RequestMapping(value = "/today")
	public String today(HttpServletResponse response, @RequestParam("target") String target)
	{
		DateTime currentDate = DateTime.now();
		response.addCookie(new Cookie("currentDate", dateService.getDateStringNormal(currentDate)));
		return "redirect:" + target;
	}
}