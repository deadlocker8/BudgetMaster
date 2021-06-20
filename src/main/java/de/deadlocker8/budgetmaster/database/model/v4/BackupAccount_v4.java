package de.deadlocker8.budgetmaster.database.model.v4;

import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.database.model.BackupInfo;
import de.deadlocker8.budgetmaster.database.model.Upgradeable;
import de.deadlocker8.budgetmaster.database.model.v5.BackupAccount_v5;

import java.util.List;
import java.util.Objects;

public class BackupAccount_v4 implements Upgradeable<BackupAccount_v5>
{
	private Integer ID;
	private String name;
	private AccountType type;

	public BackupAccount_v4()
	{
		// for GSON
	}

	public BackupAccount_v4(Integer ID, String name, AccountType type)
	{
		this.ID = ID;
		this.name = name;
		this.type = type;
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


	public AccountType getType()
	{
		return type;
	}

	public void setType(AccountType type)
	{
		this.type = type;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupAccount_v4 that = (BackupAccount_v4) o;
		return Objects.equals(ID, that.ID) && Objects.equals(name, that.name) && type == that.type;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, type);
	}

	@Override
	public String toString()
	{
		return "BackupAccount_v4{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", type=" + type +
				'}';
	}

	@Override
	public BackupAccount_v5 upgrade(List<BackupInfo> backupInfoItems)
	{
		return new BackupAccount_v5(ID, name, AccountState.FULL_ACCESS, type, null);
	}
}
