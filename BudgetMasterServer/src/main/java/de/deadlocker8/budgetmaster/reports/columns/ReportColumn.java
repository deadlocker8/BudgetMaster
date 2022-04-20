package de.deadlocker8.budgetmaster.reports.columns;

import de.deadlocker8.budgetmaster.reports.settings.ReportSettings;

import javax.persistence.*;

@Entity
public class ReportColumn
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ID;

	private String localizationKey;
	private boolean activated;
	private int columnPosition;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private ReportSettings referringSettings;

	public ReportColumn(String localizationKey, int columnPosition)
	{
		this.localizationKey = localizationKey;
		this.activated = true;
		this.columnPosition = columnPosition;
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

	public String getLocalizationKey()
	{
		return localizationKey;
	}

	public void setLocalizationKey(String key)
	{
		this.localizationKey = key;
	}

	public boolean isActivated()
	{
		return activated;
	}

	public void setActivated(boolean activated)
	{
		this.activated = activated;
	}

	public int getColumnPosition()
	{
		return columnPosition;
	}

	public void setColumnPosition(int position)
	{
		this.columnPosition = position;
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
				", localizationKey='" + localizationKey + '\'' +
				", activated=" + activated +
				", columnPosition=" + columnPosition +
				'}';
	}
}
