package de.deadlocker8.budgetmaster.hotkeys;

import de.thecodelabs.utils.util.Localization;

import java.text.MessageFormat;

public enum GeneralHotKey implements HotKey
{
	CREATE_TRANSACTION("hotkeys.transactions.new.normal", false),
	CREATE_RECURRING_TRANSACTION("hotkeys.transactions.new.repeating", false),
	CREATE_TRANSFER_TRANSACTION("hotkeys.transactions.new.transfer", false),
	CREATE_TRANSACTION_FROM_TEMPLATE("hotkeys.transactions.new.template", false),
	SAVE_TRANSACTION("hotkeys.transactions.save", true),
	FILTER("hotkeys.transactions.filter", false),
	SEARCH("hotkeys.search", false);

	private final String localizationKey;
	private final boolean hasModifier;

	GeneralHotKey(String localizationKey, boolean hasModifier)
	{
		this.localizationKey = localizationKey;
		this.hasModifier = hasModifier;
	}

	@Override
	public String getModifierLocalized()
	{
		if(hasModifier)
		{
			return getLocalized("modifier");
		}
		return null;
	}

	@Override
	public String getKeyLocalized()
	{
		return getLocalized("key");
	}

	@Override
	public String getTextLocalized()
	{
		return getLocalized(null);
	}

	private String getLocalized(String keySuffix)
	{
		if(keySuffix == null)
		{
			return Localization.getString(localizationKey);
		}

		return Localization.getString(MessageFormat.format("{0}.{1}", localizationKey, keySuffix));
	}

	@Override
	public String toString()
	{
		return "GeneralHotKey{" +
				"localizationKey='" + localizationKey + '\'' +
				", hasModifier=" + hasModifier +
				"} " + super.toString();
	}
}
