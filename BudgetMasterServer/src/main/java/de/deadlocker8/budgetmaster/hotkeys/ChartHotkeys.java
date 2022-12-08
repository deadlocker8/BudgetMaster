package de.deadlocker8.budgetmaster.hotkeys;

import de.thecodelabs.utils.util.Localization;

import java.text.MessageFormat;

public enum ChartHotkeys implements HotKey
{
	SHIFT_CLICK("hotkeys.charts.shift.click", true);

	private final String localizationKey;
	private final boolean hasModifier;

	ChartHotkeys(String localizationKey, boolean hasModifier)
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
		return "ChartHotkeys{" +
				"localizationKey='" + localizationKey + '\'' +
				", hasModifier=" + hasModifier +
				"} " + super.toString();
	}
}
