package de.deadlocker8.budgetmaster.database.model.v5;

import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.database.model.BackupInfo;
import de.deadlocker8.budgetmaster.database.model.Upgradeable;
import de.deadlocker8.budgetmaster.database.model.v6.BackupAccount_v6;

import java.util.List;
import java.util.Objects;

public class BackupAccount_v5 implements Upgradeable<BackupAccount_v6>
{
	private Integer ID;
	private String name;
	private AccountState accountState;
	private AccountType type;
	private BackupImage_v5 icon;

	public BackupAccount_v5()
	{
		// for GSON
	}

	public BackupAccount_v5(Integer ID, String name, AccountState accountState, AccountType type, BackupImage_v5 icon)
	{
		this.ID = ID;
		this.name = name;
		this.accountState = accountState;
		this.type = type;
		this.icon = icon;
	}

	public Integer getID()
	{
		return ID;
	}

	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public AccountState getAccountState()
	{
		return accountState;
	}

	public void setAccountState(AccountState accountState)
	{
		this.accountState = accountState;
	}

	public AccountType getType()
	{
		return type;
	}

	public void setType(AccountType type)
	{
		this.type = type;
	}

	public BackupImage_v5 getIcon()
	{
		return icon;
	}

	public void setIcon(BackupImage_v5 icon)
	{
		this.icon = icon;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupAccount_v5 that = (BackupAccount_v5) o;
		return Objects.equals(ID, that.ID) && Objects.equals(name, that.name) && accountState == that.accountState && type == that.type && Objects.equals(icon, that.icon);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, accountState, type, icon);
	}

	@Override
	public String toString()
	{
		return "BackupAccount_v5{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", accountState=" + accountState +
				", type=" + type +
				", icon=" + icon +
				'}';
	}

	@Override
	public BackupAccount_v6 upgrade(List<BackupInfo> backupInfoItems)
	{
		Integer iconID = null;
		if(icon != null)
		{
			iconID = icon.getID();
		}

		return new BackupAccount_v6(ID, name, accountState, type, iconID);
	}
}
