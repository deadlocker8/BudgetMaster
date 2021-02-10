package de.deadlocker8.budgetmaster.hotkeys;


public interface HotKey
{
	String getModifier();

	String getKey();

	String getLocalizationKey();

	String getLocalizedText();
}
