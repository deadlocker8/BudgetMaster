package de.deadlocker8.budgetmaster.database.model.converter;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v6.BackupAccount_v6;
import de.deadlocker8.budgetmaster.images.Image;

import java.util.List;

public class AccountConverter implements Converter<Account, BackupAccount_v6>
{
	private final List<Image> availableIcons;

	public AccountConverter(List<Image> availableIcons)
	{
		this.availableIcons = availableIcons;
	}

	public Account convertToInternalForm(BackupAccount_v6 backupAccount)
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
		account.setIcon(getIconById(backupAccount.getIconID()));
		return account;
	}

	private Image getIconById(Integer iconID)
	{
		return availableIcons.stream()
				.filter(icon -> icon.getID().equals(iconID))
				.findFirst().orElse(null);
	}

	@Override
	public BackupAccount_v6 convertToExternalForm(Account internalAccount)
	{
		if(internalAccount == null)
		{
			return null;
		}

		final BackupAccount_v6 account = new BackupAccount_v6();
		account.setID(internalAccount.getID());
		account.setName(internalAccount.getName());
		account.setAccountState(internalAccount.getAccountState());
		account.setType(internalAccount.getType());

		if(internalAccount.getIcon() != null)
		{
			account.setIconID(internalAccount.getIcon().getID());
		}
		return account;
	}
}
