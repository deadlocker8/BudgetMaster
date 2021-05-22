package de.deadlocker8.budgetmaster.database.model.v5.converter;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v5.BackupAccount_v5;

public class AccountConverter_v5 implements Converter<Account, BackupAccount_v5>
{
	public Account convertToInternalForm(BackupAccount_v5 backupAccount)
	{
		if(backupAccount == null)
		{
			return null;
		}

		final Account account = new Account();
		account.setID(backupAccount.getID());
		account.setName(backupAccount.getName());
		account.setDefault(false);
		account.setSelected(false);
		account.setAccountState(backupAccount.getAccountState());
		account.setType(backupAccount.getType());
		account.setIcon(new ImageConverter_v5().convertToInternalForm(backupAccount.getIcon()));
		return account;
	}

	@Override
	public BackupAccount_v5 convertToExternalForm(Account internalAccount)
	{
		if(internalAccount == null)
		{
			return null;
		}

		final BackupAccount_v5 account = new BackupAccount_v5();
		account.setID(internalAccount.getID());
		account.setName(internalAccount.getName());
		account.setAccountState(internalAccount.getAccountState());
		account.setType(internalAccount.getType());
		account.setIcon(new ImageConverter_v5().convertToExternalForm(internalAccount.getIcon()));
		return account;
	}
}
