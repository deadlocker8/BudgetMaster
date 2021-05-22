package de.deadlocker8.budgetmaster.database.model.v5;

import de.deadlocker8.budgetmaster.categories.CategoryType;

import java.util.Objects;

public class BackupCategory_v5
{
	private Integer ID;
	private String name;
	private String color;
	private CategoryType type;
	private String icon;

	public BackupCategory_v5()
	{
	}

	public BackupCategory_v5(Integer ID, String name, String color, CategoryType type, String icon)
	{
		this.ID = ID;
		this.name = name;
		this.color = color;
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

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupCategory_v5 that = (BackupCategory_v5) o;
		return Objects.equals(ID, that.ID) && Objects.equals(name, that.name) && Objects.equals(color, that.color) && type == that.type && Objects.equals(icon, that.icon);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, color, type, icon);
	}

	@Override
	public String toString()
	{
		return "BackupCategory_v5{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", color='" + color + '\'' +
				", type=" + type +
				", icon='" + icon + '\'' +
				'}';
	}
}
