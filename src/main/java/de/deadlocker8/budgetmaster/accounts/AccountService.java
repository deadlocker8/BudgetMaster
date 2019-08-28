package de.deadlocker8.budgetmaster.accounts;

import de.deadlocker8.budgetmaster.authentication.User;
import de.deadlocker8.budgetmaster.authentication.UserRepository;
import de.deadlocker8.budgetmaster.services.Resetable;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.utils.Strings;
import de.thecodelabs.utils.util.Localization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService implements Resetable
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private AccountRepository accountRepository;
	private TransactionService transactionService;
	private UserRepository userRepository;

	@Autowired
	public AccountService(AccountRepository accountRepository, TransactionService transactionService, UserRepository userRepository)
	{
		this.accountRepository = accountRepository;
		this.transactionService = transactionService;
		this.userRepository = userRepository;

		createDefaults();
	}

	public AccountRepository getRepository()
	{
		return accountRepository;
	}

	public List<Account> getAllAccountsAsc()
	{
		List<Account> accounts = accountRepository.findAllByType(AccountType.ALL);
		accounts.addAll(accountRepository.findAllByTypeOrderByNameAsc(AccountType.CUSTOM));
		return accounts;
	}

	public void deleteAccount(int ID)
	{
		Account accountToDelete = accountRepository.findOne(ID);
		transactionService.deleteTransactionsWithAccount(accountToDelete);
		accountToDelete.setReferringTransactions(new ArrayList<>());

		// select "all accounts" as selected account
		selectAccount(accountRepository.findAllByType(AccountType.ALL).get(0).getID());

		// set new default if necessary
		if(accountToDelete.isDefault())
		{
			List<Account> accounts = accountRepository.findAllByType(AccountType.CUSTOM);
			accounts.remove(accountToDelete);
			setAsDefaultAccount(accounts.get(0).getID());
		}

		accountRepository.delete(ID);
	}

	@Override
	public void deleteAll()
	{
		deselectAllAccounts();
		User user = userRepository.findByName("Default");
		user.setSelectedAccount(null);
		userRepository.save(user);

		accountRepository.deleteAll();
	}

	@Override
	public void createDefaults()
	{
		if(accountRepository.findAll().isEmpty())
		{
			Account placeholder = new Account("Placeholder", AccountType.ALL);
			accountRepository.save(placeholder);
			LOGGER.debug("Created placeholder account");

			Account account =  accountRepository.save(new Account(Localization.getString(Strings.ACCOUNT_DEFAULT_NAME), AccountType.CUSTOM));
			selectAccount(account.getID());
			setAsDefaultAccount(account.getID());
			LOGGER.debug("Created default account");
		}

		Account defaultAccount = accountRepository.findByIsDefault(true);
		if(defaultAccount == null)
		{
			Account account = accountRepository.findAllByType(AccountType.CUSTOM).get(0);
			setAsDefaultAccount(account.getID());
		}
		setAsDefaultAccount(accountRepository.findByIsDefault(true).getID());
	}

	private void deselectAllAccounts()
	{
		List<Account> accounts = accountRepository.findAll();
		for(Account currentAccount : accounts)
		{
			currentAccount.setSelected(false);
			accountRepository.save(currentAccount);
		}
	}

	public void selectAccount(int ID)
	{
		deselectAllAccounts();

		Account accountToSelect = accountRepository.findOne(ID);
		accountToSelect.setSelected(true);
		accountRepository.save(accountToSelect);

		User user = userRepository.findByName("Default");
		if(user != null)
		{
			user.setSelectedAccount(accountToSelect);
			userRepository.save(user);
		}
	}

	public void setAsDefaultAccount(int ID)
	{
		unsetDefaultForAllAccounts();

		Account accountToSelect = accountRepository.findOne(ID);
		accountToSelect.setDefault(true);
		accountRepository.save(accountToSelect);
	}

	private void unsetDefaultForAllAccounts()
	{
		List<Account> accounts = accountRepository.findAll();
		for(Account currentAccount : accounts)
		{
			currentAccount.setDefault(false);
			accountRepository.save(currentAccount);
		}
	}
}
