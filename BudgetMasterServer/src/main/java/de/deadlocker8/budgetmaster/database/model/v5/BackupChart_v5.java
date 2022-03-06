package de.deadlocker8.budgetmaster.database.model.v5;

import de.deadlocker8.budgetmaster.charts.ChartType;

import java.util.Objects;

public class BackupChart_v5
{
	private Integer ID;
	private String name;
	private String script;
	private ChartType type;
	private int version;

	public BackupChart_v5()
	{
		// for GSON
	}

	public BackupChart_v5(Integer ID, String name, String script, ChartType type, int version)
	{
		this.ID = ID;
		this.name = name;
		this.script = script;
		this.type = type;
		this.version = version;
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

	public String getScript()
	{
		return script;
	}

	public void setScript(String script)
	{
		this.script = script;
	}

	public ChartType getType()
	{
		return type;
	}

	public void setType(ChartType type)
	{
		this.type = type;
	}

	public int getVersion()
	{
		return version;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupChart_v5 that = (BackupChart_v5) o;
		return version == that.version && Objects.equals(ID, that.ID) && Objects.equals(name, that.name) && Objects.equals(script, that.script) && type == that.type;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, script, type, version);
	}

	@Override
	public String toString()
	{
		return "BackupChart_v5{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", script='" + script + '\'' +
				", type=" + type +
				", version=" + version +
				'}';
	}
}
