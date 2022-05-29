package de.deadlocker8.budgetmaster.charts;

import de.deadlocker8.budgetmaster.utils.LocalizedEnum;

public enum ChartGroupType implements LocalizedEnum
{
	NONE("far fa-calendar-times", "none"),
	MONTH("far fa-calendar", "month"),
	YEAR("far fa-calendar-alt", "year");

	private final String icon;
	private final String localizationKey;

	ChartGroupType(String icon, String localizationKey)
	{
		this.icon = icon;
		this.localizationKey = localizationKey;
	}

	public boolean hasFontAwesomeIcon()
	{
		return icon.startsWith("fa");
	}

	public String getIcon()
	{
		return icon;
	}

	@Override
	public String getLocalizationKey()
	{
		return "chart.group.type." + localizationKey;
	}
}
