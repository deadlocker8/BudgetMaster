package de.deadlocker8.budgetmaster.database.model.v8;

import de.deadlocker8.budgetmaster.database.model.BackupInfo;

import java.util.Objects;

public class BackupIcon_v8 implements BackupInfo
{
	private Integer ID;
	private Integer imageID;
	private String builtinIdentifier;
	private String fontColor;

	public BackupIcon_v8()
	{
		// for GSON
	}

	public BackupIcon_v8(Integer ID, Integer imageID, String builtinIdentifier, String fontColor)
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
		BackupIcon_v8 that = (BackupIcon_v8) o;
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
		return "BackupIcon_v8{" +
				"ID=" + ID +
				", imageID=" + imageID +
				", builtinIdentifier='" + builtinIdentifier + '\'' +
				", fontColor='" + fontColor + '\'' +
				'}';
	}
}
