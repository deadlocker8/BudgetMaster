package de.deadlocker8.budgetmaster.database.model.v9;

import de.deadlocker8.budgetmaster.database.model.BackupInfo;

import java.util.Objects;

public class BackupTransactionNameKeyword_v9 implements BackupInfo
{
	private String value;

	public BackupTransactionNameKeyword_v9()
	{
		// for GSON
	}

	public BackupTransactionNameKeyword_v9(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupTransactionNameKeyword_v9 that = (BackupTransactionNameKeyword_v9) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(value);
	}

	@Override
	public String toString()
	{
		return "BackupTransactionNameKeyword_v9{" +
				"value='" + value + '\'' +
				'}';
	}
}
