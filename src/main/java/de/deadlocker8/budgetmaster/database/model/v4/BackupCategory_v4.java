package de.deadlocker8.budgetmaster.database.model.v4;

import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.database.model.BackupInfo;
import de.deadlocker8.budgetmaster.database.model.Upgradeable;
import de.deadlocker8.budgetmaster.database.model.v5.BackupCategory_v5;

import java.util.List;
import java.util.Objects;

public class BackupCategory_v4 implements Upgradeable<BackupCategory_v5>
{
	private Integer ID;
	private String name;
	private String color;
	private CategoryType type;

	public BackupCategory_v4()
	{
		// for GSON
	}

	public BackupCategory_v4(Integer ID, String name, String color, CategoryType type)
	{
		this.ID = ID;
		this.name = name;
		this.color = color;
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

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}

	public CategoryType getType()
	{
		return type;
	}

	public void setType(CategoryType type)
	{
		this.type = type;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupCategory_v4 that = (BackupCategory_v4) o;
		return Objects.equals(ID, that.ID) && Objects.equals(name, that.name) && Objects.equals(color, that.color) && type == that.type;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, color, type);
	}

	@Override
	public String toString()
	{
		return "BackupCategory_v4{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", color='" + color + '\'' +
				", type=" + type +
				'}';
	}

	@Override
	public BackupCategory_v5 upgrade(List<BackupInfo> backupInfoItems)
	{
		return new BackupCategory_v5(ID, name, color, type, null);
	}
}
