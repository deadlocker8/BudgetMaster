package de.deadlocker8.budgetmaster.reports;


import de.deadlocker8.budgetmaster.utils.Strings;
import de.thecodelabs.utils.util.Localization;

public enum ColumnType
{
	POSITION(Strings.REPORT_POSITION, 1),
	DATE(Strings.REPORT_DATE, 2),
	REPEATING(Strings.REPORT_REPEATING, 1),
	CATEGORY(Strings.REPORT_CATEGORY, 3),
	NAME(Strings.REPORT_NAME, 3),
	DESCRIPTION(Strings.REPORT_DESCRIPTION, 3), 
	TAGS(Strings.REPORT_TAGS, 3),
	ACCOUNT(Strings.REPORT_ACCOUNT, 2),
	RATING(Strings.REPORT_RATING, 1),
	AMOUNT(Strings.REPORT_AMOUNT, 2);
	
	private String name;
	private float proportion;

	private ColumnType(String name, float proportion)
	{
		this.name = name;
		this.proportion = proportion;
	}

	public static ColumnType getByName(String name)
	{
		for(ColumnType type : ColumnType.values())
		{
			if(type.name.equalsIgnoreCase(name))
			{
				return type;
			}
		}

		return null;
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