package de.deadlocker8.budgetmaster.hotkeys;

import de.thecodelabs.utils.util.Localization;

public enum GlobalDatePickerHotKey implements HotKey
{
	PREVIOUS_MONTH(null, "hotkeys.global.datepicker.previous.month.key", "hotkeys.global.datepicker.previous.month"),
	NEXT_MONTH(null, "hotkeys.global.datepicker.next.month.key", "hotkeys.global.datepicker.next.month"),
	CURRENT_MONTH(null, "hotkeys.global.datepicker.today.key", "hotkeys.global.datepicker.today");

	private final String modifier;
	private final String key;
	private final String text;

	GlobalDatePickerHotKey(String modifier, String key, String text)
	{
		this.modifier = modifier;
		this.key = key;
		this.text = text;
	}

	@Override
	public String getModifier()
	{
		return modifier;
	}

	@Override
	public String getModifierLocalized()
	{
		return getLocalized(modifier);
	}

	@Override
	public String getKey()
	{
		return key;
	}

	@Override
	public String getKeyLocalized()
	{
		return getLocalized(key);
	}

	@Override
	public String getText()
	{
		return text;
	}

	@Override
	public String getTextLocalized()
	{
		return getLocalized(text);
	}

	private String getLocalized(String localizationKey)
	{
		if(localizationKey == null)
		{
			return "";
		}

		return Localization.getString(localizationKey);
	}

	@Override
	public String toString()
	{
		return "GlobalDatePickerHotKey{" +
				"modifier='" + modifier + '\'' +
				", key='" + key + '\'' +
				", text='" + text + '\'' +
				"} " + super.toString();
	}
}
