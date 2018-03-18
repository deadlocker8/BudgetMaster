package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.entities.CategoryType;
import de.deadlocker8.budgetmaster.entities.Payment;
import de.deadlocker8.budgetmaster.entities.Settings;
import de.deadlocker8.budgetmaster.repositories.PaymentRepository;
import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import de.deadlocker8.budgetmaster.utils.Helpers;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


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
	public String previousMonth(RedirectAttributes redirectAttributes, @CookieValue("currentDate") String date)
	{
		Settings settings = getSettings();
		DateTime currentDate = DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd").withLocale(settings.getLanguage().getLocale()));
		currentDate = currentDate.minusMonths(1);

		redirectAttributes.addFlashAttribute("date", currentDate);
		return "redirect:/payments";
	}

	@RequestMapping(value = "/nextMonth")
	public String nextMonth(RedirectAttributes redirectAttributes, @CookieValue("currentDate") String date)
	{
		Settings settings = getSettings();
		DateTime currentDate = DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd").withLocale(settings.getLanguage().getLocale()));
		currentDate = currentDate.plusMonths(1);

		redirectAttributes.addFlashAttribute("date", currentDate);
		return "redirect:/payments";
	}

	@RequestMapping("/payments/{ID}/requestDelete")
	public String requestDeletePayment(Model model, @PathVariable("ID") Integer ID)
	{
		if(!isDeletable(ID))
		{
			return "redirect:/payments";
		}

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
		model.addAttribute("currentPayment", paymentRepository.getOne(ID));
		return "payments/payments";
	}

	@RequestMapping("/payments/{ID}/delete")
	public String deletePayment(Model model, @PathVariable("ID") Integer ID)
	{
		if(isDeletable(ID))
		{
			paymentRepository.delete(ID);
		}

		return "redirect:/payments";
	}

	private boolean isDeletable(Integer ID)
	{
		Payment paymentToDelete = paymentRepository.getOne(ID);
		return paymentToDelete != null && paymentToDelete.getCategory().getType() != CategoryType.REST;
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