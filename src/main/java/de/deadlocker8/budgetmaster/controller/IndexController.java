package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.entities.Payment;
import de.deadlocker8.budgetmaster.entities.Settings;
import de.deadlocker8.budgetmaster.repositories.PaymentRepository;
import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.services.PaymentService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
public class IndexController extends BaseController
{
	@Autowired
	SettingsRepository settingsRepository;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	private HelpersService helpers;

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

		List<Payment> payments = paymentService.getPaymentsForMonthAndYear(helpers.getCurrentAccount(), date.getMonthOfYear(), date.getYear());

		int incomeSum = helpers.getIncomeSumForPaymentList(payments);
		int paymentSum = helpers.getPaymentSumForPaymentList(payments);
		int rest = incomeSum + paymentSum;
		model.addAttribute("incomeSum", incomeSum);
		model.addAttribute("paymentSum", paymentSum);
		model.addAttribute("rest", rest);

		return "index";
	}
}