package de.deadlocker8.budgetmaster.hotkeys;

import de.thecodelabs.utils.util.Localization;

public enum GlobalDatePickerHotKey
{
	PREVIOUS_MONTH(null, "n", "hotkeys.global.datepicker.previous.month"),
	NEXT_MONTH(null, "r", "hotkeys.global.datepicker.next.month"),
	CURRENT_MONTH(null, "t", "hotkeys.global.datepicker.today");

	private final String modifier;
	private final String key;
	private final String localizationKey;

	GlobalDatePickerHotKey(String modifier, String key, String localizationKey)
	{
		this.modifier = modifier;
		this.key = key;
		this.localizationKey = localizationKey;
	}

	public String getModifier()
	{
		return modifier;
	}

	public String getKey()
	{
		return key;
	}

	public String getLocalizationKey()
	{
		return localizationKey;
	}

	public String getLocalizedText()
	{
		return Localization.getString(localizationKey);
	}

	@Override
	public String toString()
	{
		return "GlobalDatePickerHotKey{" +
				"modifier='" + modifier + '\'' +
				", combination='" + key + '\'' +
				", localizationKey='" + localizationKey + '\'' +
				"} " + super.toString();
	}
}
