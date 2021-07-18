package de.deadlocker8.budgetmaster.charts;

import de.deadlocker8.budgetmaster.utils.LocalizedEnum;

public enum ChartGroupType implements LocalizedEnum
{
	NONE("none"),
	MONTH("month"),
	YEAR("year");

	private final String localizationKey;

	ChartGroupType(String localizationKey)
	{
		this.localizationKey = localizationKey;
	}

	@Override
	public String getLocalizationKey()
	{
		return "chart.group.type." + localizationKey;
	}
}
