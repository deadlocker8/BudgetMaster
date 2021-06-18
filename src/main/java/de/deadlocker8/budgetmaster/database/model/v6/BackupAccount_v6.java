package de.deadlocker8.budgetmaster.database.model.v6;

import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.database.model.BackupInfo;
import de.deadlocker8.budgetmaster.database.model.Upgradeable;
import de.deadlocker8.budgetmaster.database.model.v7.BackupAccount_v7;
import de.deadlocker8.budgetmaster.database.model.v7.BackupIcon_v7;

import java.util.List;
import java.util.Objects;

public class BackupAccount_v6 implements Upgradeable<BackupAccount_v7>
{
	private Integer ID;
	private String name;
	private AccountState accountState;
	private AccountType type;
	private Integer iconID;

	public BackupAccount_v6()
	{
		// for GSON
	}

	public BackupAccount_v6(Integer ID, String name, AccountState accountState, AccountType type, Integer iconID)
	{
		this.ID = ID;
		this.name = name;
		this.accountState = accountState;
		this.type = type;
		this.iconID = iconID;
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

	public Integer getIconID()
	{
		return iconID;
	}

	public void setIconID(Integer iconID)
	{
		this.iconID = iconID;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupAccount_v6 that = (BackupAccount_v6) o;
		return Objects.equals(ID, that.ID) && Objects.equals(name, that.name) && accountState == that.accountState && type == that.type && Objects.equals(iconID, that.iconID);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, accountState, type, iconID);
	}

	@Override
	public String toString()
	{
		return "BackupAccount_v6{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", accountState=" + accountState +
				", type=" + type +
				", iconID=" + iconID +
				'}';
	}

	@Override
	public BackupAccount_v7 upgrade(List<? extends BackupInfo> backupInfoItems)
	{
		Integer iconReferenceID = null;
		if(iconID != null)
		{
			final BackupIcon_v7 iconReference = backupInfoItems.stream()
					.map(BackupIcon_v7.class::cast)
					.filter(icon -> icon.getImageID() != null)
					.filter(icon -> icon.getImageID().equals(iconID))
					.findFirst()
					.orElseThrow();

			iconReferenceID = iconReference.getID();
		}

		return new BackupAccount_v7(ID, name, accountState, type, iconReferenceID);
	}


}
