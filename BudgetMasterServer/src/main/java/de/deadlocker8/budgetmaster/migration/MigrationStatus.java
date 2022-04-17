package de.deadlocker8.budgetmaster.migration;

public enum MigrationStatus
{
	NOT_RUNNING("fas fa-ban", "migration.status.not.running", "text-blue"),
	RUNNING("fas fa-gears", "migration.status.running", "text-blue"),
	SUCCESS("fas fa-check", "migration.status.success", "text-green"),
	ERROR("fas fa-exclamation-triangle", "migration.status.error", "red-text");  // red-text is better readable than text-red

	private final String icon;
	private final String localizationKey;
	private final String textColor;

	MigrationStatus(String icon, String localizationKey, String textColor)
	{
		this.icon = icon;
		this.localizationKey = localizationKey;
		this.textColor = textColor;
	}

	public String getIcon()
	{
		return icon;
	}

	public String getLocalizationKey()
	{
		return localizationKey;
	}


	public String getTextColor()
	{
		return textColor;
	}

	@Override
	public String toString()
	{
		return "MigrationStatus{" +
				"icon='" + icon + '\'' +
				", localizationKey='" + localizationKey + '\'' +
				", textColor='" + textColor + '\'' +
				"} " + super.toString();
	}
}
