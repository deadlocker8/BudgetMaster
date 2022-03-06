package de.deadlocker8.budgetmaster.databasemigrator.source.category;


import javax.persistence.*;

@Entity
@Table(name = "category")
public class SourceCategory
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ID;

	private String name;

	private String color;

	private CategoryType type;

	public SourceCategory()
	{
	}

	public SourceCategory(Integer ID, String name, String color, CategoryType type)
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
	public String toString()
	{
		return "SourceCategory{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", color='" + color + '\'' +
				", type=" + type +
				'}';
	}
}
