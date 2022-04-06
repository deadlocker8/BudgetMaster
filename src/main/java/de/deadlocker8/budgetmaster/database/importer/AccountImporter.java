package de.deadlocker8.budgetmaster.database.importer;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatch;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatchList;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.services.ImportResultItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class AccountImporter extends ItemImporter<Account>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountImporter.class);

	public AccountImporter(AccountRepository accountRepository)
	{
		super(accountRepository, EntityType.ACCOUNT);
	}

	public ImportResultItem importItems(List<Account> accounts, AccountMatchList accountMatchList)
	{
		LOGGER.debug(MessageFormat.format("Importing {0} {1}...", accountMatchList.getAccountMatches().size(), entityType.getAllItemsName()));
		final List<String> collectedErrorMessages = new ArrayList<>();
		int numberOfImportedItems = 0;

		for(AccountMatch accountMatch : accountMatchList.getAccountMatches())
		{
			LOGGER.debug(MessageFormat.format("Importing account {0} -> {1}", accountMatch.getAccountSource().getName(), accountMatch.getAccountDestination().getName()));

			try
			{
				final Account sourceAccount = accounts.stream()
						.filter(account -> account.getID().equals(accountMatch.getAccountSource().getID()))
						.findFirst()
						.orElseThrow();

				final Account destinationAccount = repository.findById(accountMatch.getAccountDestination().getID()).orElseThrow();

				final Icon sourceIcon = sourceAccount.getIconReference();
				if(sourceIcon != null)
				{
					LOGGER.debug("Overwriting destination account icon");
					destinationAccount.setIconReference(sourceIcon);
					repository.save(destinationAccount);
				}

				sourceAccount.updateFromOtherAccount(destinationAccount);
				numberOfImportedItems++;
			}
			catch(Exception e)
			{
				final String errorMessage = MessageFormat.format("Error while importing {0} \"{1}\"", entityType.getSingleItemName(), getNameForItem(accountMatch.getAccountSource()));
				LOGGER.error(errorMessage, e);
				collectedErrorMessages.add(formatErrorMessage(errorMessage, e));
			}
		}

		LOGGER.debug(MessageFormat.format("Importing accounts DONE ({0}/{1})", numberOfImportedItems, accountMatchList.getAccountMatches().size()));
		return new ImportResultItem(entityType, numberOfImportedItems, accountMatchList.getAccountMatches().size(), collectedErrorMessages);
	}

	@Override
	protected int importSingleItem(Account account)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected String getNameForItem(Account item)
	{
		return item.getName();
	}
}
