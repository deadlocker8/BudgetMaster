package de.deadlocker8.budgetmaster.reports;


import com.itextpdf.text.Element;
import de.deadlocker8.budgetmaster.utils.Strings;
import de.thecodelabs.utils.util.Localization;

public enum ColumnType
{
	POSITION(Strings.REPORT_POSITION, 1, Element.ALIGN_CENTER),
	DATE(Strings.REPORT_DATE, 2, Element.ALIGN_CENTER),
	REPEATING(Strings.REPORT_REPEATING, 1, Element.ALIGN_CENTER),
	TRANSFER(Strings.REPORT_TRANSFER, 1, Element.ALIGN_CENTER),
	CATEGORY(Strings.REPORT_CATEGORY, 3, Element.ALIGN_CENTER),
	NAME(Strings.REPORT_NAME, 3, Element.ALIGN_CENTER),
	DESCRIPTION(Strings.REPORT_DESCRIPTION, 3, Element.ALIGN_CENTER),
	TAGS(Strings.REPORT_TAGS, 3, Element.ALIGN_CENTER),
	ACCOUNT(Strings.REPORT_ACCOUNT, 2, Element.ALIGN_CENTER),
	RATING(Strings.REPORT_RATING, 1, Element.ALIGN_CENTER),
	AMOUNT(Strings.REPORT_AMOUNT, 2, Element.ALIGN_RIGHT);

	private final String key;
	private final float proportion;
	private final int alignment;

	ColumnType(String name, float proportion, int alignment)
	{
		this.key = name;
		this.proportion = proportion;
		this.alignment = alignment;
	}

	public static ColumnType getByName(String name)
	{
		for(ColumnType type : ColumnType.values())
		{
			if(type.key.equalsIgnoreCase(name))
			{
				return type;
			}
		}

		return null;
	}

	public String getName()
	{
		return Localization.getString(key);
	}

	public String getKey()
	{
		return key;
	}

	public float getProportion()
	{
		return proportion;
	}

	public int getAlignment()
	{
		return alignment;
	}
}