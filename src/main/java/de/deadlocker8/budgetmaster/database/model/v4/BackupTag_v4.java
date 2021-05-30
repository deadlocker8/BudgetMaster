package de.deadlocker8.budgetmaster.database.model.v4;

import java.util.Objects;

public class BackupTag_v4
{
	private String name;

	public BackupTag_v4()
	{
		// for GSON
	}

	public BackupTag_v4(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupTag_v4 that = (BackupTag_v4) o;
		return Objects.equals(name, that.name);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name);
	}

	@Override
	public String toString()
	{
		return "BackupTag_v4{" +
				"name='" + name + '\'' +
				'}';
	}
}
