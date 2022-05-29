package de.deadlocker8.budgetmaster.charts;

import de.deadlocker8.budgetmaster.utils.LocalizedEnum;

public enum ChartDisplayType implements LocalizedEnum
{
	PIE("fas fa-chart-pie", "pie"),
	BAR("fas fa-chart-bar", "bar"),
	LINE("fas fa-chart-line", "line"),
	CUSTOM("insights", "custom");

	private final String icon;
	private final String localizationKey;

	ChartDisplayType(String icon, String localizationKey)
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
		return "chart.display.type." + localizationKey;
	}
}
