package de.deadlocker8.budgetmaster.categories;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.Iconizable;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.utils.ProvidesID;
import de.thecodelabs.utils.util.Color;
import de.thecodelabs.utils.util.ColorUtilsNonJavaFX;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
public class Category implements ProvidesID, Iconizable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@OneToOne(cascade = CascadeType.REMOVE)
	@Expose
	private Icon iconReference;

	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
	private List<Transaction> referringTransactions;

	public Category(String name, String color, CategoryType type)
	{
		this(name, color, type, null);
	}

	public Category(String name, String color, CategoryType type, Icon iconReference)
	{
		this.name = name;
		this.color = color;
		this.type = type;
		this.iconReference = iconReference;
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

	@Override
	public Icon getIconReference()
	{
		return iconReference;
	}

	@Override
	public void setIconReference(Icon iconReference)
	{
		this.iconReference = iconReference;
	}

	@Override
	public String getFontColor(boolean isDarkTheme)
	{
		final Icon icon = getIconReference();
		if(icon == null)
		{
			return getAppropriateTextColor();
		}

		final String fontColor = icon.getFontColor();
		if(fontColor == null)
		{
			return getAppropriateTextColor();
		}

		return fontColor;
	}

	public String getAppropriateTextColor()
	{
		return ColorUtilsNonJavaFX.getAppropriateTextColor(new Color(color)).toRGBHexWithoutOpacity();
	}

	public List<Transaction> getReferringTransactions()
	{
		return referringTransactions;
	}

	public void setReferringTransactions(List<Transaction> referringTransactions)
	{
		this.referringTransactions = referringTransactions;
	}

	@Override
	public String toString()
	{
		return "Category{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", color='" + color + '\'' +
				", type=" + type +
				", iconReference='" + iconReference + '\'' +
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
				type == category.type &&
				Objects.equals(iconReference, category.iconReference);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, color, type, iconReference);
	}
}