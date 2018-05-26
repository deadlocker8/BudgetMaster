package de.deadlocker8.budgetmaster.repeating;

import de.deadlocker8.budgetmaster.entities.Payment;
import de.deadlocker8.budgetmaster.repositories.PaymentRepository;
import de.deadlocker8.budgetmaster.repositories.RepeatingOptionRepository;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.services.PaymentService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RepeatingPaymentUpdater
{
	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private RepeatingOptionRepository repeatingOptionRepository;

	@Autowired
	private HelpersService helpers;

	public void updateRepeatingPayments(DateTime now)
	{
		List<RepeatingOption> repeatingOptions = repeatingOptionRepository.findAllByOrderByStartDateAsc();
		for(RepeatingOption option : repeatingOptions)
		{
			List<Payment> payments = option.getReferringPayments();
			List<DateTime> correctDates = option.getRepeatingDates(now);
			for(DateTime currentDate : correctDates)
			{
				if(!containsDate(payments, currentDate))
				{
					Payment newPayment = new Payment(payments.get(0));
					newPayment.setID(null);
					newPayment.setDate(currentDate);
					paymentRepository.save(newPayment);
				}
			}
		}
	}

	private boolean containsDate(List<Payment> payments, DateTime date)
	{
		for(Payment currentPayment : payments)
		{
			if(date.equals(currentPayment.getDate()))
			{
				return true;
			}
		}

		return false;
	}
}