package de.deadlocker8.budgetmaster.entities;

import javafx.scene.paint.Color;
import tools.ConvertTo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Category
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer ID;

	@NotNull
	@Size(min = 1)
	private String name;
	private String color;
	private CategoryType type;

	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
	private List<Payment> referringPayments;

	public Category(String name, String color, CategoryType type)
	{
		this.name = name;
		this.color = color;
		this.type = type;
	}

	public Category()
	{
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

	public List<Payment> getReferringPayments()
	{
		return referringPayments;
	}

	public void setReferringPayments(List<Payment> referringPayments)
	{
		this.referringPayments = referringPayments;
	}

	public String getAppropriateTextColor()
	{
		return ConvertTo.toRGBHexWithoutOpacity(ConvertTo.getAppropriateTextColor(Color.web(color)));
	}

	@Override
	public String toString()
	{
		return "Category{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", color='" + color + '\'' +
				", type=" + type +
				'}';
	}
}