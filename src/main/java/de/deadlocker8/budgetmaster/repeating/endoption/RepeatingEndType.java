package de.deadlocker8.budgetmaster.repeating.endoption;

import de.thecodelabs.utils.util.Localization;

public enum RepeatingEndType
{
	NEVER("repeating.end.key.never"),
	AFTER_X_TIMES("repeating.end.key.afterXTimes"),
	DATE("repeating.end.key.date");

	private String localizationKey;

	RepeatingEndType(String localizationKey)
	{
		this.localizationKey = localizationKey;
	}

	public String getLocalizationKey()
	{
		return localizationKey;
	}

	public static RepeatingEndType getByLocalization(String localizedName)
	{
		for(RepeatingEndType type : values())
		{
			if(Localization.getString(type.getLocalizationKey()).equals(localizedName))
			{
				return type;
			}
		}

		return null;
	}

	@Override
	public String toString()
	{
		return "RepeatingEndType{" +
				"localizationKey='" + localizationKey + '\'' +
				'}';
	}
}
