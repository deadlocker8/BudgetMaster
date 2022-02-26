package de.deadlocker8.budgetmaster.database.model.v7;

import de.deadlocker8.budgetmaster.database.model.BackupInfo;
import de.deadlocker8.budgetmaster.database.model.Upgradeable;
import de.deadlocker8.budgetmaster.database.model.v8.BackupIcon_v8;

import java.util.List;
import java.util.Objects;

public class BackupIcon_v7 implements BackupInfo, Upgradeable<BackupIcon_v8>
{
	private Integer ID;
	private Integer imageID;
	private String builtinIdentifier;

	public BackupIcon_v7()
	{
		// for GSON
	}

	public BackupIcon_v7(Integer ID, Integer imageID, String builtinIdentifier)
	{
		this.ID = ID;
		this.imageID = imageID;
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

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupIcon_v7 that = (BackupIcon_v7) o;
		return Objects.equals(ID, that.ID) && Objects.equals(imageID, that.imageID) && Objects.equals(builtinIdentifier, that.builtinIdentifier);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, imageID, builtinIdentifier);
	}

	@Override
	public String toString()
	{
		return "BackupIcon_v7{" +
				"ID=" + ID +
				", imageID=" + imageID +
				", builtinIdentifier='" + builtinIdentifier + '\'' +
				'}';
	}

	@Override
	public BackupIcon_v8 upgrade(List<BackupInfo> backupInfoItems)
	{
		return new BackupIcon_v8(this.ID, this.imageID, this.builtinIdentifier, null);
	}
}
