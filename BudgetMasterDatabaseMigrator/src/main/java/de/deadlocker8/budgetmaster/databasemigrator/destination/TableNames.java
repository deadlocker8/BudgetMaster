package de.deadlocker8.budgetmaster.databasemigrator.destination;

public class TableNames
{
	private TableNames()
	{
		// empty
	}

	public static final String IMAGE = "image";
	public static final String ICON = "icon";
	public static final String CATEGORY = "category";
	public static final String ACCOUNT = "account";
	public static final String CHART = "chart";
	public static final String HINT = "hint";

	public static final String REPEATING_END = "repeating_end";
	public static final String REPEATING_END_AFTER_X_TIMES = "repeating_end_afterxtimes";
	public static final String REPEATING_END_DATE = "repeating_end_date";
	public static final String REPEATING_END_NEVER = "repeating_end_never";

	public static final String REPEATING_MODIFIER = "repeating_modifier";
	public static final String REPEATING_MODIFIER_DAYS = "repeating_modifier_days";
	public static final String REPEATING_MODIFIER_MONTHS = "repeating_modifier_months";
	public static final String REPEATING_MODIFIER_YEARS = "repeating_modifier_years";
}
