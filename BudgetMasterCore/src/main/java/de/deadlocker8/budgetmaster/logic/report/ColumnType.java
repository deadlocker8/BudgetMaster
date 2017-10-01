package de.deadlocker8.budgetmaster.logic.report;

import de.deadlocker8.budgetmaster.logic.utils.Strings;
import tools.Localization;

public enum ColumnType
{
	POSITION(Strings.REPORT_POSITION, 1),
	DATE(Strings.REPORT_DATE, 2),
	REPEATING(Strings.REPORT_REPEATING, 1),
	CATEGORY(Strings.REPORT_CATEGORY, 3),
	NAME(Strings.REPORT_NAME, 3),
	DESCRIPTION(Strings.REPORT_DESCRIPTION, 3), 
	TAGS(Strings.REPORT_TAGS, 3),
	RATING(Strings.REPORT_RATING, 1), 
	AMOUNT(Strings.REPORT_AMOUNT, 2);
	
	private String name;
	private float proportion;

	private ColumnType(String name, float proportion)
	{
		this.name = name;
		this.proportion = proportion;
	}

	public String getName()
	{
		return Localization.getString(name);
	}

	public float getProportion()
	{
		return proportion;
	}
}