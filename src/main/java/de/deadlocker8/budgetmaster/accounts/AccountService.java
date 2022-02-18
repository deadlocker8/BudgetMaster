package de.deadlocker8.budgetmaster.accounts;

import de.deadlocker8.budgetmaster.authentication.User;
import de.deadlocker8.budgetmaster.authentication.UserRepository;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.services.AccessAllEntities;
import de.deadlocker8.budgetmaster.services.AccessEntityByID;
import de.deadlocker8.budgetmaster.services.Resettable;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.utils.Strings;
import de.thecodelabs.utils.util.Localization;
import org.padler.natorder.NaturalOrderComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements Resettable, AccessAllEntities<Account>, AccessEntityByID<Account>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

	private static final String PLACEHOLDER_ICON = "fas fa-landmark";

	private final AccountRepository accountRepository;
	private final TransactionService transactionService;
	private final UserRepository userRepository;
	private final IconService iconService;

	@Autowired
	public AccountService(AccountRepository accountRepository, TransactionService transactionService, UserRepository userRepository, IconService iconService)
	{
		this.accountRepository = accountRepository;
		this.transactionService = transactionService;
		this.userRepository = userRepository;
		this.iconService = iconService;

		createDefaults();
	}

	public AccountRepository getRepository()
	{
		return accountRepository;
	}

	@Override
	public List<Account> getAllEntitiesAsc()
	{
		final List<Account> accounts = accountRepository.findAllByTypeOrderByNameAsc(AccountType.CUSTOM);
		accounts.sort((a1, a2) -> new NaturalOrderComparator().compare(a1.getName(), a2.getName()));
		accounts.addAll(0, accountRepository.findAllByType(AccountType.ALL));
		return accounts;
	}

	public List<Account> getAllActivatedAccountsAsc()
	{
		List<Account> accounts = accountRepository.findAllByTypeAndAccountStateOrderByNameAsc(AccountType.CUSTOM, AccountState.FULL_ACCESS);
		accounts.sort((a1, a2) -> new NaturalOrderComparator().compare(a1.getName(), a2.getName()));
		accounts.addAll(0, accountRepository.findAllByType(AccountType.ALL));
		return accounts;
	}

	public List<Account> getAllReadableAccounts()
	{
		List<Account> accounts = accountRepository.findAllByTypeAndAccountStateOrderByNameAsc(AccountType.CUSTOM, AccountState.FULL_ACCESS);
		accounts.addAll(accountRepository.findAllByTypeAndAccountStateOrderByNameAsc(AccountType.CUSTOM, AccountState.READ_ONLY));
		accounts.sort((a1, a2) -> new NaturalOrderComparator().compare(a1.getName(), a2.getName()));
		accounts.addAll(0, accountRepository.findAllByType(AccountType.ALL));
		return accounts;
	}

	@Override
	public Optional<Account> findById(Integer ID)
	{
		return accountRepository.findById(ID);
	}

	@Transactional
	public void deleteAccount(int ID)
	{
		Optional<Account> accountToDeleteOptional = accountRepository.findById(ID);
		if(accountToDeleteOptional.isEmpty())
		{
			return;
		}

		Account accountToDelete = accountToDeleteOptional.get();
		transactionService.deleteTransactionsWithAccount(accountToDelete);
		accountToDelete.setReferringTransactions(new ArrayList<>());

		// select "all accounts" as selected account
		selectAccount(accountRepository.findAllByType(AccountType.ALL).get(0).getID());

		// set new default if necessary
		if(accountToDelete.isDefault())
		{
			List<Account> accounts = accountRepository.findAllByTypeAndAccountStateOrderByNameAsc(AccountType.CUSTOM, AccountState.FULL_ACCESS);
			accounts.remove(accountToDelete);
			setAsDefaultAccount(accounts.get(0).getID());
		}

		accountRepository.deleteById(ID);
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
	@Transactional
	public void createDefaults()
	{
		if(accountRepository.findAll().isEmpty())
		{
			Account placeholder = new Account("Placeholder", AccountType.ALL);
			final Icon newIcon = iconService.createIconReference(null, PLACEHOLDER_ICON, null);
			iconService.getRepository().save(newIcon);
			placeholder.setIconReference(newIcon);
			accountRepository.save(placeholder);
			LOGGER.debug("Created placeholder account");

			Account account = accountRepository.save(new Account(Localization.getString(Strings.ACCOUNT_DEFAULT_NAME), AccountType.CUSTOM));
			selectAccount(account.getID());
			setAsDefaultAccount(account.getID());
			LOGGER.debug("Created default account");
		}

		updateMissingAttributes();

		Account defaultAccount = accountRepository.findByIsDefault(true);
		if(defaultAccount == null)
		{
			Account account = accountRepository.findAllByTypeAndAccountStateOrderByNameAsc(AccountType.CUSTOM, AccountState.FULL_ACCESS).get(0);
			setAsDefaultAccount(account.getID());
		}
		setAsDefaultAccount(accountRepository.findByIsDefault(true).getID());
	}

	private void updateMissingAttributes()
	{
		for(Account account : accountRepository.findAll())
		{
			handleNullValuesForAccountState(account);
			accountRepository.save(account);
		}

		final Account placeholderAccount = accountRepository.findAllByType(AccountType.ALL).get(0);
		final Icon icon = placeholderAccount.getIconReference();
		if(icon == null)
		{
			final Icon newIcon = iconService.createIconReference(null, PLACEHOLDER_ICON, null);
			iconService.getRepository().save(newIcon);
			placeholderAccount.setIconReference(newIcon);
			LOGGER.debug(MessageFormat.format("Updated placeholder account: Created missing icon instance and set icon to \"{0}\"", PLACEHOLDER_ICON));
		}
		else if(icon.getBuiltinIdentifier() == null)
		{
			placeholderAccount.updateIcon(iconService, null, PLACEHOLDER_ICON, null, this);
			LOGGER.debug(MessageFormat.format("Updated placeholder account: Set missing icon to \"{0}\"", PLACEHOLDER_ICON));
		}
	}

	private void handleNullValuesForAccountState(Account account)
	{
		if(account.getAccountState() == null)
		{
			account.setAccountState(AccountState.FULL_ACCESS);
			LOGGER.debug(MessageFormat.format("Updated account {0}: Set missing attribute \"accountState\" to {1}", account.getName(), account.getAccountState()));
		}
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

	@Transactional
	public void selectAccount(int ID)
	{
		deselectAllAccounts();

		Optional<Account> accountToSelectOptional = accountRepository.findById(ID);
		if(accountToSelectOptional.isEmpty())
		{
			return;
		}

		Account accountToSelect = accountToSelectOptional.get();
		accountToSelect.setSelected(true);
		accountRepository.save(accountToSelect);

		User user = userRepository.findByName("Default");
		if(user != null)
		{
			user.setSelectedAccount(accountToSelect);
			userRepository.save(user);
		}
	}

	@Transactional
	public void setAsDefaultAccount(int ID)
	{
		Optional<Account> accountToSelectOptional = accountRepository.findById(ID);
		if(accountToSelectOptional.isEmpty())
		{
			return;
		}

		Account accountToSelect = accountToSelectOptional.get();
		if(accountToSelect.getAccountState() != AccountState.FULL_ACCESS)
		{
			return;
		}

		unsetDefaultForAllAccounts();

		accountToSelect.setDefault(true);
		accountRepository.save(accountToSelect);
	}

	@Transactional
	public void unsetDefaultForAllAccounts()
	{
		List<Account> accounts = accountRepository.findAll();
		for(Account currentAccount : accounts)
		{
			currentAccount.setDefault(false);
			accountRepository.save(currentAccount);
		}
	}

	@Transactional
	public void updateExistingAccount(Account newAccount)
	{
		Optional<Account> existingAccountOptional = accountRepository.findById(newAccount.getID());
		if(existingAccountOptional.isEmpty())
		{
			return;
		}

		Account existingAccount = existingAccountOptional.get();
		existingAccount.setName(newAccount.getName());
		existingAccount.setIconReference(newAccount.getIconReference());
		existingAccount.setType(AccountType.CUSTOM);
		existingAccount.setAccountState(newAccount.getAccountState());
		accountRepository.save(existingAccount);

		if(existingAccount.isDefault() && existingAccount.getAccountState() != AccountState.FULL_ACCESS)
		{
			// set any activated account as new default account
			unsetDefaultForAllAccounts();
			List<Account> activatedAccounts = accountRepository.findAllByTypeAndAccountStateOrderByNameAsc(AccountType.CUSTOM, AccountState.FULL_ACCESS);
			Account newDefaultAccount = activatedAccounts.get(0);
			setAsDefaultAccount(newDefaultAccount.getID());
		}

		if(existingAccount.isSelected() && existingAccount.getAccountState() == AccountState.HIDDEN)
		{
			// select "all accounts" as selected account
			selectAccount(accountRepository.findAllByType(AccountType.ALL).get(0).getID());
		}
	}

	public Account getSelectedAccountOrDefaultAsFallback()
	{
		final Account selectedAccount = accountRepository.findByIsSelected(true);
		if(selectedAccount != null)
		{
			return selectedAccount;
		}

		return accountRepository.findByIsDefault(true);
	}
}
