package de.deadlocker8.budgetmaster.entities.report;

import javax.persistence.*;
import java.util.List;

@Entity
public class ReportColumn
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer ID;

	private String key;
	private boolean activated;
	private int position;

	@OneToMany(mappedBy = "columns", fetch = FetchType.LAZY)
	private transient List<ReportSettings> referringSettings;

	public ReportColumn(String key, int position)
	{
		this.key = key;
		this.activated = true;
		this.position = position;
	}

	public ReportColumn()
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

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public boolean isActivated()
	{
		return activated;
	}

	public void setActivated(boolean activated)
	{
		this.activated = activated;
	}

	public int getPosition()
	{
		return position;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}

	@Override
	public String toString()
	{
		return "ReportColumn{" +
				"ID=" + ID +
				", key='" + key + '\'' +
				", activated=" + activated +
				", position=" + position +
				'}';
	}
}
