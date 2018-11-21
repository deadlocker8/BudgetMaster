package de.deadlocker8.budgetmaster.repeating.modifier;


import de.thecodelabs.utils.util.Localization;

public enum RepeatingModifierType
{
	DAYS("repeating.modifier.days"),
	MONTHS("repeating.modifier.months"),
	YEARS("repeating.modifier.years");

	private String localizationKey;

	RepeatingModifierType(String localizationKey)
	{
		this.localizationKey = localizationKey;
	}

	public String getLocalizationKey()
	{
		return localizationKey;
	}

	public static RepeatingModifierType getByLocalization(String localizedName)
	{
		for(RepeatingModifierType type : values())
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
		return "RepeatingModifierType{" +
				"localizationKey='" + localizationKey + '\'' +
				'}';
	}
}
