package de.deadlocker8.budgetmaster.database.model.v5;

import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.accounts.AccountType;

import java.util.Objects;

public class BackupAccount_v5
{
	private Integer ID;
	private String name;
	private Boolean isSelected;
	private Boolean isDefault;
	private AccountState accountState;
	private AccountType type;
	private BackupImage_v5 icon;

	public BackupAccount_v5()
	{
	}

	public BackupAccount_v5(Integer ID, String name, AccountState accountState, AccountType type, BackupImage_v5 icon)
	{
		this.ID = ID;
		this.name = name;
		this.isSelected = false;
		this.isDefault = false;
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

	public Boolean getSelected()
	{
		return isSelected;
	}

	public void setSelected(Boolean selected)
	{
		isSelected = selected;
	}

	public Boolean getDefault()
	{
		return isDefault;
	}

	public void setDefault(Boolean aDefault)
	{
		isDefault = aDefault;
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
		return Objects.equals(ID, that.ID) && Objects.equals(name, that.name) && Objects.equals(isSelected, that.isSelected) && Objects.equals(isDefault, that.isDefault) && accountState == that.accountState && type == that.type && Objects.equals(icon, that.icon);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, isSelected, isDefault, accountState, type, icon);
	}

	@Override
	public String toString()
	{
		return "BackupAccount_v5{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", isSelected=" + isSelected +
				", isDefault=" + isDefault +
				", accountState=" + accountState +
				", type=" + type +
				", icon=" + icon +
				'}';
	}
}
