package de.deadlocker8.budgetmaster.database.model.converter;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v11.BackupAccount_v11;
import de.deadlocker8.budgetmaster.icon.Icon;

import java.util.List;

public class AccountConverter implements Converter<Account, BackupAccount_v11>
{
	private final List<Icon> availableIcons;

	public AccountConverter(List<Icon> availableIcons)
	{
		this.availableIcons = availableIcons;
	}

	public Account convertToInternalForm(BackupAccount_v11 backupAccount)
	{
		if(backupAccount == null)
		{
			return null;
		}

		final Account account = new Account();
		account.setID(backupAccount.getID());
		account.setName(backupAccount.getName());
		account.setDescription(backupAccount.getDescription());
		account.setDefault(false);
		account.setSelected(false);
		account.setAccountState(backupAccount.getAccountState());
		account.setType(backupAccount.getType());
		account.setIconReference(getItemById(availableIcons, backupAccount.getIconReferenceID()));
		return account;
	}

	@Override
	public BackupAccount_v11 convertToExternalForm(Account internalAccount)
	{
		if(internalAccount == null)
		{
			return null;
		}

		final BackupAccount_v11 account = new BackupAccount_v11();
		account.setID(internalAccount.getID());
		account.setName(internalAccount.getName());
		account.setDescription(internalAccount.getDescription());
		account.setAccountState(internalAccount.getAccountState());
		account.setType(internalAccount.getType());

		final Icon icon = internalAccount.getIconReference();
		if(icon != null)
		{
			account.setIconReferenceID(icon.getID());
		}
		return account;
	}
}
