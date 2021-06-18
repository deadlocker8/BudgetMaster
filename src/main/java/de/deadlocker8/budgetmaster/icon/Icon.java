package de.deadlocker8.budgetmaster.icon;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.utils.ProvidesID;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Icon implements ProvidesID
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Integer ID;

	@ManyToOne
	@Expose
	private Image image;

	@Expose
	private String builtinIdentifier;

	public Icon()
	{
	}

	public Icon(Image image)
	{
		this.image = image;
	}

	public Icon(String builtinIdentifier)
	{
		this.builtinIdentifier = builtinIdentifier;
	}

	public Integer getID()
	{
		return ID;
	}

	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	public Image getImage()
	{
		return image;
	}

	public void setImage(Image image)
	{
		this.image = image;
	}

	public String getBuiltinIdentifier()
	{
		return builtinIdentifier;
	}

	public void setBuiltinIdentifier(String builtinIdentifier)
	{
		this.builtinIdentifier = builtinIdentifier;
	}

	public boolean isBuiltinIcon()
	{
		return image == null;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Icon icon = (Icon) o;
		return Objects.equals(ID, icon.ID) && Objects.equals(image, icon.image) && Objects.equals(builtinIdentifier, icon.builtinIdentifier);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, image, builtinIdentifier);
	}

	@Override
	public String toString()
	{
		return "Icon{" +
				"ID=" + ID +
				", image=" + image +
				", builtinIdentifier='" + builtinIdentifier + '\'' +
				'}';
	}
}
