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
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Expose
	private Integer ID;

	@NotNull
	@Size(min = 1)
	@Expose
	private String name;
	@Expose
	@Column(columnDefinition="TEXT")
	private String script;
	@Expose
	private ChartType type;

	public Chart(String name, String script, ChartType type)
	{
		this.name = name;
		this.script = script;
		this.type = type;
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

	@Override
	public String toString()
	{
		return "Chart{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", script='" + script + '\'' +
				", type=" + type +
				'}';
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Chart chart = (Chart) o;
		return Objects.equals(ID, chart.ID) &&
				Objects.equals(name, chart.name) &&
				Objects.equals(script, chart.script) &&
				type == chart.type;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, script, type);
	}
}