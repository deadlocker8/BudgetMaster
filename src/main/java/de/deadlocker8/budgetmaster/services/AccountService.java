package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.entities.Account;
import de.deadlocker8.budgetmaster.repositories.AccountRepository;
import de.deadlocker8.budgetmaster.repositories.PaymentRepository;
import de.deadlocker8.budgetmaster.utils.Strings;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.Localization;

@Service
public class AccountService implements Resetable
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private AccountRepository accountRepository;
	private PaymentRepository paymentRepository;

	@Autowired
	public AccountService(AccountRepository accountRepository, PaymentRepository paymentRepository, LocalizationService localizationService)
	{
		this.accountRepository = accountRepository;
		this.paymentRepository = paymentRepository;

		createDefaults();
	}

	public AccountRepository getRepository()
	{
		return accountRepository;
	}

	public void deleteAccount(int ID)
	{
		Account accountToDelete = accountRepository.findOne(ID);
		paymentRepository.delete(accountToDelete.getReferringPayments());
		accountRepository.delete(ID);
	}

	@Override
	public void deleteAll()
	{
		accountRepository.deleteAll();
	}

	@Override
	public void createDefaults()
	{
		if(accountRepository.findAll().size() == 0)
		{
			Account account = new Account(Localization.getString(Strings.ACCOUNT_DEFAULT_NAME));
			account.setSelected(true);
			accountRepository.save(account);
			LOGGER.debug("Created default account");
		}
	}
}
