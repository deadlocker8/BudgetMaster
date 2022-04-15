package de.deadlocker8.budgetmaster.database.importer;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatch;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatchList;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconRepository;
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

	private final IconRepository iconRepository;

	public AccountImporter(AccountRepository accountRepository, IconRepository iconRepository)
	{
		super(accountRepository, EntityType.ACCOUNT);
		this.iconRepository = iconRepository;
	}

	public ImportResultItem importItems(List<Account> accounts, AccountMatchList accountMatchList)
	{
		LOGGER.debug(MessageFormat.format("Importing {0} {1}...", accountMatchList.getAccountMatches().size(), entityType.getAllItemsName()));
		final List<String> collectedErrorMessages = new ArrayList<>();
		int numberOfImportedItems = 0;

		final List<Account> accountsToUpdate = new ArrayList<>(accounts);

		for(AccountMatch accountMatch : accountMatchList.getAccountMatches())
		{
			LOGGER.debug(MessageFormat.format("Importing account {0} -> {1}", accountMatch.getAccountSource().getName(), accountMatch.getAccountDestination().getName()));

			try
			{
				final Account sourceAccount = getSourceAccountForMatch(accountsToUpdate, accountMatch);
				final Account destinationAccount = repository.findById(accountMatch.getAccountDestination().getID()).orElseThrow();

				final Icon sourceIcon = sourceAccount.getIconReference();
				if(sourceIcon != null)
				{
					overwriteIcon(destinationAccount, sourceIcon);
				}

				accountsToUpdate.remove(sourceAccount);
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

	private void overwriteIcon(Account destinationAccount, Icon sourceIcon)
	{
		LOGGER.debug("Overwriting destination account icon");

		// explicitly delete old icon to avoid remaining references
		final Icon existingIcon = destinationAccount.getIconReference();
		destinationAccount.setIconReference(null);
		iconRepository.delete(existingIcon);

		destinationAccount.setIconReference(sourceIcon);
		repository.save(destinationAccount);
	}

	private Account getSourceAccountForMatch(List<Account> accountsToUpdate, AccountMatch accountMatch)
	{
		return accountsToUpdate.stream()
				.filter(account -> account.getID().equals(accountMatch.getAccountSource().getID()))
				.findFirst()
				.orElseThrow();
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
