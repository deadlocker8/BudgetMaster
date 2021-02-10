package de.deadlocker8.budgetmaster.hotkeys;

import de.thecodelabs.utils.util.Localization;

public enum GeneralHotKey implements HotKey
{
	CREATE_TRANSACTION(null, "hotkeys.transactions.new.normal.key", "hotkeys.transactions.new.normal"),
	CREATE_RECURRING_TRANSACTION(null, "hotkeys.transactions.new.repeating.key", "hotkeys.transactions.new.repeating"),
	CREATE_TRANSFER_TRANSACTION(null, "hotkeys.transactions.new.transfer.key", "hotkeys.transactions.new.transfer"),
	CREATE_TRANSACTION_FROM_TEMPLATE(null, "hotkeys.transactions.new.template.key", "hotkeys.transactions.new.template"),
	SAVE_TRANSACTION("hotkeys.transactions.save.modifier", "hotkeys.transactions.save.key", "hotkeys.transactions.save"),
	FILTER(null, "hotkeys.transactions.filter.key", "hotkeys.transactions.filter"),
	SEARCH(null, "hotkeys.search.key", "hotkeys.search");

	private final String modifier;
	private final String key;
	private final String text;

	GeneralHotKey(String modifier, String key, String text)
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
		return "GeneralHotKey{" +
				"modifier='" + modifier + '\'' +
				", key='" + key + '\'' +
				", text='" + text + '\'' +
				"} " + super.toString();
	}
}
