package de.deadlocker8.budgetmaster.databasemigrator.destination.icon;

import javax.persistence.*;

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
