package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.entities.Category;
import de.deadlocker8.budgetmaster.entities.Payment;
import de.deadlocker8.budgetmaster.entities.Settings;
import de.deadlocker8.budgetmaster.repositories.PaymentRepository;
import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import de.deadlocker8.budgetmaster.utils.Helpers;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;


@Controller
public class PaymentController extends BaseController
{
	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private SettingsRepository settingsRepository;

	@RequestMapping("/payments")
	public String payments(Model model)
	{
		DateTime date;
		if(model.containsAttribute("date"))
		{
			date = (DateTime)model.asMap().get("date");
		}
		else
		{
			date = DateTime.now();
		}

		model.addAttribute("currentDate", Helpers.getDateString(date, getSettings()));
		model.addAttribute("currentDateFormatted", Helpers.getDateStringWithMonthAndYear(date, getSettings()));
		model.addAttribute("payments", getPaymentsForMonthAndYear(date.getMonthOfYear(), date.getYear()));
		return "payments/payments";
	}

	@RequestMapping(value = "/previousMonth")
	public String previousMonth(@RequestParam(value = "currentDate") String date, RedirectAttributes redirectAttributes)
	{
		Settings settings = getSettings();
		DateTime currentDate = DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd").withLocale(settings.getLanguage().getLocale()));
		currentDate = currentDate.minusMonths(1);

		redirectAttributes.addFlashAttribute("date", currentDate);
		return "redirect:/payments";
	}

	@RequestMapping(value = "/nextMonth")
	public String nextMonth(@RequestParam(value = "currentDate") String date, RedirectAttributes redirectAttributes)
	{
		Settings settings = getSettings();
		DateTime currentDate = DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd").withLocale(settings.getLanguage().getLocale()));
		currentDate = currentDate.plusMonths(1);

		redirectAttributes.addFlashAttribute("date", currentDate);
		return "redirect:/payments";
	}

	private Settings getSettings()
	{
		return settingsRepository.findOne(0);
	}

	private List<Payment> getPaymentsForMonthAndYear(int month, int year)
	{
		DateTime startDate = DateTime.now().withYear(year).withMonthOfYear(month).withDayOfMonth(1);
		DateTime endDate = DateTime.now().withYear(year).withMonthOfYear(month).dayOfMonth().withMaximumValue();
		return paymentRepository.findAllByDateBetween(startDate, endDate);
	}
}