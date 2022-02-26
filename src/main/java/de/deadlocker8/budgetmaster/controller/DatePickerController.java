package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.services.DateFormatStyle;
import de.deadlocker8.budgetmaster.services.DateService;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Controller
public class DatePickerController extends BaseController
{
	private static final String COOKIE_NAME = "currentDate";

	private final SettingsService settingsService;
	private final DateService dateService;

	@Autowired
	public DatePickerController(SettingsService settingsService, DateService dateService)
	{
		this.settingsService = settingsService;
		this.dateService = dateService;
	}

	@GetMapping(value = "/previousMonth")
	public String previousMonth(HttpServletResponse response, @CookieValue(COOKIE_NAME) String date, @RequestParam("target") String target)
	{
		Settings settings = settingsService.getSettings();
		LocalDate currentDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(DateFormatStyle.NORMAL.getKey()).withLocale(settings.getLanguage().getLocale()));
		currentDate = currentDate.minusMonths(1);

		response.addCookie(new Cookie(COOKIE_NAME, dateService.getDateStringNormal(currentDate)));
		return "redirect:" + target;
	}

	@GetMapping(value = "/nextMonth")
	public String nextMonth(HttpServletResponse response, @CookieValue(COOKIE_NAME) String date, @RequestParam("target") String target)
	{
		Settings settings = settingsService.getSettings();
		LocalDate currentDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(DateFormatStyle.NORMAL.getKey()).withLocale(settings.getLanguage().getLocale()));
		currentDate = currentDate.plusMonths(1);

		response.addCookie(new Cookie(COOKIE_NAME, dateService.getDateStringNormal(currentDate)));
		return "redirect:" + target;
	}

	@GetMapping(value = "/setDate")
	public String setDate(HttpServletResponse response, @CookieValue(COOKIE_NAME) String date, @RequestParam("target") String target)
	{
		Settings settings = settingsService.getSettings();
		LocalDate currentDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(DateFormatStyle.NORMAL.getKey()).withLocale(settings.getLanguage().getLocale()));

		response.addCookie(new Cookie(COOKIE_NAME, dateService.getDateStringNormal(currentDate)));
		return "redirect:" + target;
	}

	@GetMapping(value = "/today")
	public String today(HttpServletResponse response, @RequestParam("target") String target)
	{
		response.addCookie(new Cookie(COOKIE_NAME, dateService.getDateStringNormal(LocalDate.now())));
		return "redirect:" + target;
	}
}