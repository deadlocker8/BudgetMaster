package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.authentication.User;
import de.deadlocker8.budgetmaster.authentication.UserRepository;
import de.deadlocker8.budgetmaster.entities.Account;
import de.deadlocker8.budgetmaster.repositories.AccountRepository;
import de.deadlocker8.budgetmaster.repositories.TransactionRepository;
import de.deadlocker8.budgetmaster.utils.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.Localization;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService implements Resetable
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private AccountRepository accountRepository;
	private TransactionRepository transactionRepository;
	private UserRepository userRepository;

	@Autowired
	public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository, UserRepository userRepository)
	{
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		this.userRepository = userRepository;

		createDefaults();
	}

	public AccountRepository getRepository()
	{
		return accountRepository;
	}

	public void deleteAccount(int ID)
	{
		Account accountToDelete = accountRepository.findOne(ID);
		transactionRepository.delete(accountToDelete.getReferringTransactions());
		accountToDelete.setReferringTransactions(new ArrayList<>());

		List<Account> accounts = accountRepository.findAll();
		accounts.remove(accountToDelete);

		Account newSelectedAccount = accounts.get(0);
		selectAccount(newSelectedAccount.getID());

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

	public void selectAccount(int ID)
	{
		List<Account> accounts = accountRepository.findAll();
		for(Account currentAccount : accounts)
		{
			currentAccount.setSelected(false);
			accountRepository.save(currentAccount);
		}

		Account accountToSelect = accountRepository.findOne(ID);
		accountToSelect.setSelected(true);
		accountRepository.save(accountToSelect);

		User user = userRepository.findByName("Default");
		user.setSelectedAccount(accountToSelect);
		userRepository.save(user);
	}
}
