package de.deadlocker8.budgetmaster.database.importer;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.services.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class AccountImporter extends ItemImporter<Account>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountImporter.class);

	public AccountImporter(AccountRepository accountRepository)
	{
		super(accountRepository, EntityType.ACCOUNT);
	}

	@Override
	protected int importSingleItem(Account account)
	{
		if(!(repository instanceof AccountRepository repository))
		{
			throw new IllegalArgumentException("Invalid repository type");
		}

		if(account.getType().equals(AccountType.ALL))
		{
			return repository.findAllByType(AccountType.ALL).get(0).getID();
		}

		LOGGER.debug(MessageFormat.format("Importing account with name: {0}", account.getName()));

		account.setID(null);
		final Account savedAccount = repository.save(account);
		return savedAccount.getID();
	}

	@Override
	protected String getNameForItem(Account item)
	{
		return item.getName();
	}
}
