package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.entities.Account;
import de.deadlocker8.budgetmaster.entities.Payment;
import de.deadlocker8.budgetmaster.repositories.PaymentRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private PaymentRepository paymentRepository;

	@Autowired
	public PaymentService(PaymentRepository categoryRepository)
	{
		this.paymentRepository = categoryRepository;
	}

	public List<Payment> getPaymentsForMonthAndYear(Account account, int month, int year)
	{
		DateTime startDate = DateTime.now().withYear(year).withMonthOfYear(month).minusMonths(1).dayOfMonth().withMaximumValue();
		DateTime endDate = DateTime.now().withYear(year).withMonthOfYear(month).dayOfMonth().withMaximumValue();
		return paymentRepository.findAllByAccountAndDateBetweenOrderByDateDesc(account, startDate, endDate);
	}
}
