package de.deadlocker8.budgetmaster.charts;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class Chart
{
	@Id
	@Expose
	private Integer ID;

	@NotNull
	@Size(min = 1)
	@Expose
	private String name;
	@Expose
	@Column(columnDefinition = "TEXT")
	private String script;
	@Expose
	private ChartType type;
	@Expose
	private int version;

	private ChartDisplayType displayType;

	public Chart(String name, String script, ChartType type, int version, ChartDisplayType displayType)
	{
		this.name = name;
		this.script = script;
		this.type = type;
		this.version = version;
		this.displayType = displayType;
	}

	public Chart()
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

	public ChartDisplayType getDisplayType()
	{
		return displayType;
	}

	public void setDisplayType(ChartDisplayType displayType)
	{
		this.displayType = displayType;
	}

	@Override
	public String toString()
	{
		return "Chart{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", script='" + script + '\'' +
				", type=" + type +
				", version=" + version +
				", displayType=" + displayType +
				'}';
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Chart chart = (Chart) o;
		return version == chart.version && Objects.equals(ID, chart.ID) && Objects.equals(name, chart.name) && Objects.equals(script, chart.script) && type == chart.type && displayType == chart.displayType;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, script, type, version, displayType);
	}
}