package de.deadlocker8.budgetmaster.backup;

import de.thecodelabs.utils.util.Localization;

public enum AutoBackupStrategy
{
	NONE("settings.backup.auto.strategy.none"),
	LOCAL("settings.backup.auto.strategy.local"),
	GIT_LOCAL("settings.backup.auto.strategy.git.local"),
	GIT_REMOTE("settings.backup.auto.strategy.git.remote");

	private String localizationKey;

	AutoBackupStrategy(String localizationKey)
	{
		this.localizationKey = localizationKey;
	}

	public String getLocalizationKey()
	{
		return localizationKey;
	}

	public String getName()
	{
		return Localization.getString(localizationKey);
	}
	public static AutoBackupStrategy fromName(String name)
	{
		for(AutoBackupStrategy type : values())
		{
			if(type.getName().equals(name))
			{
				return type;
			}
		}

		return null;
	}

}
