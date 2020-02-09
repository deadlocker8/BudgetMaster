package de.deadlocker8.budgetmaster.reports.columns;

import de.deadlocker8.budgetmaster.reports.settings.ReportSettings;

import javax.persistence.*;

@Entity
public class ReportColumn
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ID;

	private String key;
	private boolean activated;
	private int position;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private ReportSettings referringSettings;

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

	public ReportSettings getReferringSettings()
	{
		return referringSettings;
	}

	public void setReferringSettings(ReportSettings referringSettings)
	{
		this.referringSettings = referringSettings;
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
