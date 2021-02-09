package de.deadlocker8.budgetmaster.hotkeys;

import de.thecodelabs.utils.util.Localization;

public enum HotKey
{
	CREATE_TRANSACTION(null, "n", "hotkeys.transactions.new.normal"),
	CREATE_RECURRING_TRANSACTION(null, "r", "hotkeys.transactions.new.repeating"),
	CREATE_TRANSFER_TRANSACTION(null, "t", "hotkeys.transactions.new.transfer"),
	CREATE_TRANSACTION_FROM_TEMPLATE(null, "v", "hotkeys.transactions.new.template"),
	SAVE_TRANSACTION("Ctrl", "Enter", "hotkeys.transactions.save"),
	FILTER(null, "f", "hotkeys.transactions.filter"),
	SEARCH(null, "s", "hotkeys.search");

	private final String modifier;
	private final String key;
	private final String localizationKey;

	HotKey(String modifier, String key, String localizationKey)
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
		return "HotKey{" +
				"modifier='" + modifier + '\'' +
				", combination='" + key + '\'' +
				", localizationKey='" + localizationKey + '\'' +
				"} " + super.toString();
	}
}
