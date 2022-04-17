package de.deadlocker8.budgetmaster.migration;

public enum MigrationStatus
{
	NOT_RUNNING("migration.status.not.running"),
	RUNNING("migration.status.running"),
	SUCCESS("migration.status.success"),
	ERROR("migration.status.error");

	private final String localizationKey;

	MigrationStatus(String localizationKey)
	{
		this.localizationKey = localizationKey;
	}

	public String getLocalizationKey()
	{
		return localizationKey;
	}

	@Override
	public String toString()
	{
		return "MigrationStatus{" +
				"localizationKey='" + localizationKey + '\'' +
				"} " + super.toString();
	}
}
