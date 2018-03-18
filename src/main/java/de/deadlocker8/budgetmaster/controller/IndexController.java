package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.entities.Settings;
import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IndexController extends BaseController
{
	@Autowired
	SettingsRepository settingsRepository;

	@RequestMapping("/")
	public String index(Model model, @CookieValue(value = "currentDate", required = false) String cookieDate)
	{
		DateTime date;
		if(cookieDate == null)
		{
			date = DateTime.now();
		}
		else
		{
			Settings settings = settingsRepository.findOne(0);
			date = DateTime.parse(cookieDate, DateTimeFormat.forPattern("dd.MM.yy").withLocale(settings.getLanguage().getLocale()));
		}

		model.addAttribute("currentDate", date);
		return "index";
	}
}