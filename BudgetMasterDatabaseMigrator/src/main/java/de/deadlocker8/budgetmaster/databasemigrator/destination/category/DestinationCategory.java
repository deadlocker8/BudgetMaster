package de.deadlocker8.budgetmaster.databasemigrator.destination.category;


import javax.persistence.*;

@Entity
@Table(name = "category")
public class DestinationCategory
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ID;

	private String name;

	private String color;

	private Integer type;

	public DestinationCategory()
	{
	}

	public DestinationCategory(Integer ID, String name, String color, Integer type)
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

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
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
