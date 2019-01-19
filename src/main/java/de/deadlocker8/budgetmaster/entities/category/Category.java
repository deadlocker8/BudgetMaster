package de.deadlocker8.budgetmaster.entities.category;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.entities.Transaction;
import de.thecodelabs.utils.util.ColorUtils;
import javafx.scene.paint.Color;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
public class Category
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Expose
	private Integer ID;

	@NotNull
	@Size(min = 1)
	@Expose
	private String name;
	@Expose
	private String color;
	@Expose
	private CategoryType type;

	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
	private transient List<Transaction> referringTransactions;

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

	public List<Transaction> getReferringTransactions()
	{
		return referringTransactions;
	}

	public void setReferringTransactions(List<Transaction> referringTransactions)
	{
		this.referringTransactions = referringTransactions;
	}

	public String getAppropriateTextColor()
	{
		return ColorUtils.toRGBHexWithoutOpacity(ColorUtils.getAppropriateTextColor(Color.web(color)));
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

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Category category = (Category) o;
		return Objects.equals(ID, category.ID) &&
				Objects.equals(name, category.name) &&
				Objects.equals(color, category.color) &&
				type == category.type;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, color, type);
	}
}