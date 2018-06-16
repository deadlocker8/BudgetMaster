package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.entities.Account;
import de.deadlocker8.budgetmaster.entities.CategoryType;
import de.deadlocker8.budgetmaster.entities.Payment;
import de.deadlocker8.budgetmaster.repositories.CategoryRepository;
import de.deadlocker8.budgetmaster.repositories.PaymentRepository;
import de.deadlocker8.budgetmaster.repositories.RepeatingOptionRepository;
import de.deadlocker8.budgetmaster.utils.Strings;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.Localization;

import java.util.List;

@Service
public class PaymentService implements Resetable
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private PaymentRepository paymentRepository;
	private RepeatingOptionRepository repeatingOptionRepository;
	private CategoryRepository categoryRepository;

	@Autowired
	public PaymentService(PaymentRepository paymentRepository, RepeatingOptionRepository repeatingOptionRepository, CategoryRepository categoryRepository)
	{
		this.paymentRepository = paymentRepository;
		this.repeatingOptionRepository = repeatingOptionRepository;
		this.categoryRepository = categoryRepository;
	}

	public PaymentRepository getRepository()
	{
		return paymentRepository;
	}

	public List<Payment> getPaymentsForMonthAndYear(Account account, int month, int year, boolean isRestActivated)
	{
		List<Payment> payments;
		if(isRestActivated)
		{
			payments = getPaymentsForMonthAndYearWithRest(account, month, year);
		}
		else
		{
			payments = getPaymentsForMonthAndYearWithoutRest(account, month, year);
		}

		return payments;
	}

	private List<Payment> getPaymentsForMonthAndYearWithRest(Account account, int month, int year)
	{
		DateTime startDate = DateTime.now().withYear(year).withMonthOfYear(month).minusMonths(1).dayOfMonth().withMaximumValue();
		DateTime endDate = DateTime.now().withYear(year).withMonthOfYear(month).dayOfMonth().withMaximumValue();
		List<Payment> payments = paymentRepository.findAllByAccountAndDateBetweenOrderByDateDesc(account, startDate, endDate);

		Payment paymentRest = new Payment();
		paymentRest.setCategory(categoryRepository.findByType(CategoryType.REST));
		paymentRest.setName(Localization.getString(Strings.CATEGORY_REST));
		paymentRest.setDate(DateTime.now().withYear(year).withMonthOfYear(month).withDayOfMonth(1));
		paymentRest.setAmount(getRest(account, startDate));
		payments.add(paymentRest);

		return payments;
	}

	private List<Payment> getPaymentsForMonthAndYearWithoutRest(Account account, int month, int year)
	{
		DateTime startDate = DateTime.now().withYear(year).withMonthOfYear(month).minusMonths(1).dayOfMonth().withMaximumValue();
		DateTime endDate = DateTime.now().withYear(year).withMonthOfYear(month).dayOfMonth().withMaximumValue();
		return paymentRepository.findAllByAccountAndDateBetweenOrderByDateDesc(account, startDate, endDate);
	}

	private int getRest(Account account, DateTime endDate)
	{
		DateTime startDate = DateTime.now().withYear(2000).withMonthOfYear(1).withDayOfMonth(1);
		Integer rest = paymentRepository.getRest(account.getID(), ISODateTimeFormat.date().print(startDate), ISODateTimeFormat.date().print(endDate));
		if(rest == null)
		{
			return 0;
		}
		return rest;
	}

	public void deletePayment(Integer ID)
	{
		if(isDeletable(ID))
		{
			Payment paymentToDelete = paymentRepository.findOne(ID);
			// handle repeating payments
			if(paymentToDelete.getRepeatingOption() != null)
			{
				repeatingOptionRepository.delete(paymentToDelete.getRepeatingOption().getID());
			}
			else
			{
				paymentRepository.delete(ID);
			}
		}
	}

	public boolean isDeletable(Integer ID)
	{
		Payment paymentToDelete = paymentRepository.getOne(ID);
		return paymentToDelete != null && paymentToDelete.getCategory().getType() != CategoryType.REST;
	}

	@Override
	public void deleteAll()
	{
		paymentRepository.deleteAll();
	}

	@Override
	public void createDefaults()
	{
	}
}
