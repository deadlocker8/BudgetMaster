package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.entities.Category;
import de.deadlocker8.budgetmaster.entities.CategoryType;
import de.deadlocker8.budgetmaster.entities.Payment;
import de.deadlocker8.budgetmaster.entities.Settings;
import de.deadlocker8.budgetmaster.repositories.CategoryRepository;
import de.deadlocker8.budgetmaster.repositories.PaymentRepository;
import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.utils.Colors;
import de.deadlocker8.budgetmaster.validators.CategoryValidator;
import de.deadlocker8.budgetmaster.validators.PaymentValidator;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tools.ConvertTo;

import java.util.List;


@Controller
public class PaymentController extends BaseController
{
	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SettingsRepository settingsRepository;

	@Autowired
	private HelpersService helpers;

	@RequestMapping("/payments")
	public String payments(Model model, @CookieValue(value = "currentDate", required = false) String cookieDate)
	{
		DateTime date;
		if(cookieDate == null)
		{
			date = DateTime.now();
		}
		else
		{
			date = DateTime.parse(cookieDate, DateTimeFormat.forPattern("dd.MM.yy").withLocale(getSettings().getLanguage().getLocale()));
		}

		List<Payment> payments = getPaymentsForMonthAndYear(date.getMonthOfYear(), date.getYear());
		model.addAttribute("payments", payments);
		model.addAttribute("incomeSum", getIncomeSum(payments));
		model.addAttribute("paymentSum", getPaymentSum(payments));
		model.addAttribute("currentDate", date);
		return "payments/payments";
	}

	@RequestMapping("/payments/{ID}/requestDelete")
	public String requestDeletePayment(Model model, @PathVariable("ID") Integer ID, @CookieValue("currentDate") String cookieDate)
	{
		if(!isDeletable(ID))
		{
			return "redirect:/payments";
		}

		DateTime date;
		if(cookieDate == null)
		{
			date = DateTime.now();
		}
		else
		{
			date = DateTime.parse(cookieDate, DateTimeFormat.forPattern("dd.MM.yy").withLocale(getSettings().getLanguage().getLocale()));
		}

		List<Payment> payments = getPaymentsForMonthAndYear(date.getMonthOfYear(), date.getYear());
		model.addAttribute("payments", payments);
		model.addAttribute("incomeSum", getIncomeSum(payments));
		model.addAttribute("paymentSum", getPaymentSum(payments));
		model.addAttribute("currentDate", date);
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

	@RequestMapping("/payments/newPayment")
	public String newCategory(Model model)
	{
		Payment emptyPayment = new Payment();
		model.addAttribute("categories", categoryRepository.findAllByOrderByNameAsc());
		model.addAttribute("payment", emptyPayment);
		return "payments/newPayment";
	}

	@RequestMapping(value = "/payments/newPayment", method = RequestMethod.POST)
	public String post(Model model, @ModelAttribute("NewPayment") Payment payment, BindingResult bindingResult)
	{
		PaymentValidator userValidator = new PaymentValidator();
		userValidator.validate(payment, bindingResult);

		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			model.addAttribute("payment", payment);
			return "payments/newPayment";
		}
		else
		{
			paymentRepository.save(payment);
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
		DateTime startDate = DateTime.now().withYear(year).withMonthOfYear(month).minusMonths(1).dayOfMonth().withMaximumValue();
		DateTime endDate = DateTime.now().withYear(year).withMonthOfYear(month).dayOfMonth().withMaximumValue();
		return paymentRepository.findAllByDateBetweenOrderByDateDesc(startDate, endDate);
	}

	private int getIncomeSum(List<Payment> payments)
	{
		int sum = 0;
		for(Payment payment : payments)
		{
			if(payment.getAmount() > 0)
			{
				sum += payment.getAmount();
			}
		}
		return sum;
	}

	private int getPaymentSum(List<Payment> payments)
	{
		int sum = 0;
		for(Payment payment : payments)
		{
			if(payment.getAmount() < 0)
			{
				sum += payment.getAmount();
			}
		}
		return sum;
	}
}