package de.deadlocker8.budgetmaster.databasemigrator.destination.category;


import javax.persistence.*;
import java.util.Objects;

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

	@Column(name = "icon_reference_id")
	private Integer iconReferenceID;

	public DestinationCategory()
	{
		// empty
	}

	public DestinationCategory(Integer ID, String name, String color, Integer type, Integer iconReferenceID)
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

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
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
		DestinationCategory that = (DestinationCategory) o;
		return Objects.equals(ID, that.ID) && Objects.equals(name, that.name) && Objects.equals(color, that.color) && Objects.equals(type, that.type) && Objects.equals(iconReferenceID, that.iconReferenceID);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, color, type, iconReferenceID);
	}

	@Override
	public String toString()
	{
		return "DestinationCategory{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", color='" + color + '\'' +
				", type=" + type +
				", iconReferenceID=" + iconReferenceID +
				'}';
	}
}
