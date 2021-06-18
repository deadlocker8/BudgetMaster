package de.deadlocker8.budgetmaster.database.model.v7;

import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.accounts.AccountType;

import java.util.Objects;

public class BackupAccount_v7
{
	private Integer ID;
	private String name;
	private AccountState accountState;
	private AccountType type;
	private Integer iconReferenceID;

	public BackupAccount_v7()
	{
		// for GSON
	}

	public BackupAccount_v7(Integer ID, String name, AccountState accountState, AccountType type, Integer iconReferenceID)
	{
		this.ID = ID;
		this.name = name;
		this.accountState = accountState;
		this.type = type;
		this.iconReferenceID = iconReferenceID;
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

	public Integer getIconReferenceID()
	{
		return iconReferenceID;
	}

	public void setIconReferenceID(Integer iconReferenceID)
	{
		this.iconReferenceID = iconReferenceID;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupAccount_v7 that = (BackupAccount_v7) o;
		return Objects.equals(ID, that.ID) && Objects.equals(name, that.name) && accountState == that.accountState && type == that.type && Objects.equals(iconReferenceID, that.iconReferenceID);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, accountState, type, iconReferenceID);
	}

	@Override
	public String toString()
	{
		return "BackupAccount_v7{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", accountState=" + accountState +
				", type=" + type +
				", iconReferenceID=" + iconReferenceID +
				'}';
	}
}
