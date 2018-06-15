package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.entities.Account;
import de.deadlocker8.budgetmaster.entities.CategoryType;
import de.deadlocker8.budgetmaster.entities.Payment;
import de.deadlocker8.budgetmaster.repositories.PaymentRepository;
import de.deadlocker8.budgetmaster.repositories.RepeatingOptionRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService implements Resetable
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private PaymentRepository paymentRepository;
	private RepeatingOptionRepository repeatingOptionRepository;

	@Autowired
	public PaymentService(PaymentRepository paymentRepository, RepeatingOptionRepository repeatingOptionRepository)
	{
		this.paymentRepository = paymentRepository;
		this.repeatingOptionRepository = repeatingOptionRepository;
	}

	public PaymentRepository getRepository()
	{
		return paymentRepository;
	}

	public List<Payment> getPaymentsForMonthAndYear(Account account, int month, int year)
	{
		DateTime startDate = DateTime.now().withYear(year).withMonthOfYear(month).minusMonths(1).dayOfMonth().withMaximumValue();
		DateTime endDate = DateTime.now().withYear(year).withMonthOfYear(month).dayOfMonth().withMaximumValue();
		return paymentRepository.findAllByAccountAndDateBetweenOrderByDateDesc(account, startDate, endDate);
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
