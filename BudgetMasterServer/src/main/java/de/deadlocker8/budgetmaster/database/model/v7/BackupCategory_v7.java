package de.deadlocker8.budgetmaster.database.model.v7;

import de.deadlocker8.budgetmaster.categories.CategoryType;

import java.util.Objects;

public class BackupCategory_v7
{
	private Integer ID;
	private String name;
	private String color;
	private CategoryType type;
	private Integer iconReferenceID;

	public BackupCategory_v7()
	{
		// for GSON
	}

	public BackupCategory_v7(Integer ID, String name, String color, CategoryType type, Integer iconReferenceID)
	{
		this.ID = ID;
		this.name = name;
		this.color = color;
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
		BackupCategory_v7 that = (BackupCategory_v7) o;
		return Objects.equals(ID, that.ID) && Objects.equals(name, that.name) && Objects.equals(color, that.color) && type == that.type && Objects.equals(iconReferenceID, that.iconReferenceID);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, color, type, iconReferenceID);
	}

	@Override
	public String toString()
	{
		return "BackupCategory_v7{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", color='" + color + '\'' +
				", type=" + type +
				", iconReferenceID='" + iconReferenceID + '\'' +
				'}';
	}
}
