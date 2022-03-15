package de.deadlocker8.budgetmaster.databasemigrator.destination.icon;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "icon")
public class DestinationIcon
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ID;

	@Column(name = "image_id")
	private Integer imageID;

	@Column(name = "builtin_identifier")
	private String builtinIdentifier;

	@Column(name = "font_color")
	private String fontColor;

	public DestinationIcon()
	{
		// empty
	}

	public DestinationIcon(Integer ID, Integer imageID, String builtinIdentifier, String fontColor)
	{
		this.ID = ID;
		this.imageID = imageID;
		this.builtinIdentifier = builtinIdentifier;
		this.fontColor = fontColor;
	}

	public Integer getID()
	{
		return ID;
	}

	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	public Integer getImageID()
	{
		return imageID;
	}

	public void setImageID(Integer imageID)
	{
		this.imageID = imageID;
	}

	public String getBuiltinIdentifier()
	{
		return builtinIdentifier;
	}

	public void setBuiltinIdentifier(String builtinIdentifier)
	{
		this.builtinIdentifier = builtinIdentifier;
	}

	public String getFontColor()
	{
		return fontColor;
	}

	public void setFontColor(String fontColor)
	{
		this.fontColor = fontColor;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		DestinationIcon that = (DestinationIcon) o;
		return Objects.equals(ID, that.ID) && Objects.equals(imageID, that.imageID) && Objects.equals(builtinIdentifier, that.builtinIdentifier) && Objects.equals(fontColor, that.fontColor);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, imageID, builtinIdentifier, fontColor);
	}

	@Override
	public String toString()
	{
		return "DestinationIcon{" +
				"ID=" + ID +
				", imageID=" + imageID +
				", builtinIdentifier='" + builtinIdentifier + '\'' +
				", fontColor='" + fontColor + '\'' +
				'}';
	}
}
