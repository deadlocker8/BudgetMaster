package de.deadlocker8.budgetmaster.database.model.v8;

import de.deadlocker8.budgetmaster.templategroup.TemplateGroupType;

import java.util.Objects;

public class BackupTemplateGroup_v8
{
	private Integer ID;
	private String name;
	private TemplateGroupType type;

	public BackupTemplateGroup_v8()
	{
		// for GSON
	}

	public BackupTemplateGroup_v8(Integer ID, String name, TemplateGroupType type)
	{
		this.ID = ID;
		this.name = name;
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

	public TemplateGroupType getType()
	{
		return type;
	}

	public void setType(TemplateGroupType type)
	{
		this.type = type;
	}


	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupTemplateGroup_v8 that = (BackupTemplateGroup_v8) o;
		return Objects.equals(ID, that.ID) && Objects.equals(name, that.name) && type == that.type;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, type);
	}

	@Override
	public String toString()
	{
		return "BackupTemplateGroup_v8{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", type=" + type +
				'}';
	}
}
