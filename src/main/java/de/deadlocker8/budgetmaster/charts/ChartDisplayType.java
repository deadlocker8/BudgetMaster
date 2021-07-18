package de.deadlocker8.budgetmaster.charts;

import de.deadlocker8.budgetmaster.utils.LocalizedEnum;

public enum ChartDisplayType implements LocalizedEnum
{
	PIE("pie"),
	BAR("bar"),
	LINE("line"),
	CUSTOM("custom");

	private final String localizationKey;

	ChartDisplayType(String localizationKey)
	{
		this.localizationKey = localizationKey;
	}

	@Override
	public String getLocalizationKey()
	{
		return "chart.display.type." + localizationKey;
	}
}
