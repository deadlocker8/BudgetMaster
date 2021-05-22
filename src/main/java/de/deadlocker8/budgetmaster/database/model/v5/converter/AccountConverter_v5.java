package de.deadlocker8.budgetmaster.database.model.v5.converter;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.database.model.v5.BackupAccount_v5;

public class AccountConverter_v5 implements Converter<Account, BackupAccount_v5>
{
	public Account convert(BackupAccount_v5 backupAccount)
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
		account.setIcon(new ImageConverter_v5().convert(backupAccount.getIcon()));
		return account;
	}
}
