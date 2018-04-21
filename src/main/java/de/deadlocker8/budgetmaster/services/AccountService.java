package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.entities.Account;
import de.deadlocker8.budgetmaster.repositories.AccountRepository;
import de.deadlocker8.budgetmaster.repositories.PaymentRepository;
import de.deadlocker8.budgetmaster.utils.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private AccountRepository accountRepository;
	private PaymentRepository paymentRepository;

	@Autowired
	public AccountService(AccountRepository accountRepository, PaymentRepository paymentRepository)
	{
		this.accountRepository = accountRepository;
		this.paymentRepository = paymentRepository;

		if(accountRepository.findAll().size() == 0)
		{
			accountRepository.save(new Account(Strings.ACCOUNT_DEFAULT_NAME));
			LOGGER.debug("Created default category NONE");
		}
	}

	public void deleteAccount(int ID)
	{
		Account accountToDelete = accountRepository.findOne(ID);
		paymentRepository.delete(accountToDelete.getReferringPayments());
		accountRepository.delete(ID);
	}
}
