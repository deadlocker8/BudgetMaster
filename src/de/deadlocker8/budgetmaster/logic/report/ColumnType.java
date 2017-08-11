package de.deadlocker8.budgetmaster.logic.report;

import de.deadlocker8.budgetmaster.logic.utils.Strings;
import tools.Localization;

public enum ColumnType
{
	POSITION(Strings.REPORT_POSITION),
	DATE(Strings.REPORT_DATE),
	REPEATING(Strings.REPORT_REPEATING),
	CATEGORY(Strings.REPORT_CATEGORY),
	NAME(Strings.REPORT_NAME),
	DESCRIPTION(Strings.REPORT_DESCRIPTION), 
	RATING(Strings.REPORT_RATING), 
	AMOUNT(Strings.REPORT_AMOUNT);
	
	private String name;

	private ColumnType(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return Localization.getString(name);
	}
}