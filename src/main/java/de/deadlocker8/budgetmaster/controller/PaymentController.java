package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.entities.CategoryType;
import de.deadlocker8.budgetmaster.entities.Payment;
import de.deadlocker8.budgetmaster.entities.Settings;
import de.deadlocker8.budgetmaster.entities.Tag;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.endoption.*;
import de.deadlocker8.budgetmaster.repeating.modifier.*;
import de.deadlocker8.budgetmaster.repositories.*;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.services.PaymentService;
import de.deadlocker8.budgetmaster.validators.PaymentValidator;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
public class PaymentController extends BaseController
{
	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private SettingsRepository settingsRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private HelpersService helpers;

	@RequestMapping("/payments")
	public String payments(Model model, @CookieValue(value = "currentDate", required = false) String cookieDate)
	{
		DateTime date = getDateTimeFromCookie(cookieDate);
		List<Payment> payments = paymentService.getPaymentsForMonthAndYear(helpers.getCurrentAccount(), date.getMonthOfYear(), date.getYear());

		model.addAttribute("payments", payments);
		model.addAttribute("incomeSum", helpers.getIncomeSumForPaymentList(payments));
		model.addAttribute("paymentSum", helpers.getPaymentSumForPaymentList(payments));
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

		DateTime date = getDateTimeFromCookie(cookieDate);
		List<Payment> payments = paymentService.getPaymentsForMonthAndYear(helpers.getCurrentAccount(), date.getMonthOfYear(), date.getYear());
		model.addAttribute("payments", payments);
		model.addAttribute("incomeSum", helpers.getIncomeSumForPaymentList(payments));
		model.addAttribute("paymentSum", helpers.getPaymentSumForPaymentList(payments));
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
	public String newPayment(Model model, @CookieValue("currentDate") String cookieDate)
	{
		DateTime date = getDateTimeFromCookie(cookieDate);
		Payment emptyPayment = new Payment();
		model.addAttribute("currentDate", date);
		model.addAttribute("categories", categoryRepository.findAllByOrderByNameAsc());
		model.addAttribute("accounts", accountRepository.findAllByOrderByNameAsc());
		model.addAttribute("payment", emptyPayment);
		return "payments/newPayment";
	}

	@RequestMapping(value = "/payments/newPayment", method = RequestMethod.POST)
	public String post(Model model, @CookieValue("currentDate") String cookieDate,
					   @ModelAttribute("NewPayment") Payment payment, BindingResult bindingResult,
					   @RequestParam(value = "isPayment", required = false) boolean isPayment,
					   @RequestParam(value = "enableRepeating", required = false) boolean enableRepeating,
					   @RequestParam(value = "repeatingModifierNumber", required = false) int repeatingModifierNumber,
					   @RequestParam(value = "repeatingModifierType", required = false) String repeatingModifierType,
					   @RequestParam(value = "repeatingEndType", required = false) String repeatingEndType,
					   @RequestParam(value = "repeatingEndValue", required = false) String repeatingEndValue)
	{
		DateTime date = getDateTimeFromCookie(cookieDate);

		PaymentValidator paymentValidator = new PaymentValidator();
		paymentValidator.validate(payment, bindingResult);

		if(payment.getAmount() == null)
		{
			payment.setAmount(0);
		}

		if(isPayment)
		{
			payment.setAmount(-Math.abs(payment.getAmount()));
		}
		else
		{
			payment.setAmount(Math.abs(payment.getAmount()));
		}

		List<Tag> tags = payment.getTags();
		if(tags != null)
		{
			payment.setTags(new ArrayList<>());
			for(Tag currentTag : tags)
			{
				addTagToPayment(currentTag.getName(), payment);
			}
		}

		RepeatingOption repeatingOption = null;
		if(enableRepeating)
		{
			RepeatingModifier repeatingModifier = null;
			RepeatingModifierType type = RepeatingModifierType.getByLocalization(repeatingModifierType);
			switch(type)
			{
				case DAYS:
					repeatingModifier = new RepeatingModifierDays(repeatingModifierNumber);
					break;
				case MONTHS:
					repeatingModifier = new RepeatingModifierMonths(repeatingModifierNumber);
					break;
				case YEARS:
					repeatingModifier = new RepeatingModifierYears(repeatingModifierNumber);
					break;
			}

			RepeatingEnd repeatingEnd = null;
			RepeatingEndType endType = RepeatingEndType.getByLocalization(repeatingEndType);
			switch(endType)
			{
				case NEVER:
					repeatingEnd = new RepeatingEndNever();
					break;
				case AFTER_X_TIMES:
					repeatingEnd = new RepeatingEndAfterXTimes(Integer.parseInt(repeatingEndValue));
					break;
				case DATE:
					DateTime endDate = DateTime.parse(repeatingEndValue, DateTimeFormat.forPattern("dd.MM.yy").withLocale(getSettings().getLanguage().getLocale()));
					repeatingEnd = new RepeatingEndDate(endDate);
					break;
			}

			repeatingOption = new RepeatingOption(payment.getDate(), repeatingModifier, repeatingEnd);
		}
		payment.setRepeatingOption(repeatingOption);

		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			model.addAttribute("currentDate", date);
			model.addAttribute("categories", categoryRepository.findAllByOrderByNameAsc());
			model.addAttribute("accounts", accountRepository.findAllByOrderByNameAsc());
			model.addAttribute("payment", payment);
			return "payments/newPayment";
		}

		paymentRepository.save(payment);
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

	private DateTime getDateTimeFromCookie(String cookieDate)
	{
		if(cookieDate == null)
		{
			return DateTime.now();
		}
		else
		{
			return DateTime.parse(cookieDate, DateTimeFormat.forPattern("dd.MM.yy").withLocale(getSettings().getLanguage().getLocale()));
		}
	}

	private void addTagToPayment(String name, Payment payment)
	{
		if(tagRepository.findByName(name) == null)
		{
			tagRepository.save(new Tag(name));
		}

		List<Payment> referringPayments = tagRepository.findByName(name).getReferringPayments();
		if(referringPayments == null || !referringPayments.contains(payment))
		{
			payment.getTags().add(tagRepository.findByName(name));
			paymentRepository.save(payment);
		}
	}

	private void removeTagFromPayment(String name, Payment payment)
	{
		Tag currentTag = tagRepository.findByName(name);
		currentTag.getReferringPayments().remove(payment);
		tagRepository.save(currentTag);
	}
}