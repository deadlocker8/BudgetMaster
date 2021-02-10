package de.deadlocker8.budgetmaster.hotkeys;


public interface HotKey
{
	String getModifier();

	String getModifierLocalized();

	String getKey();

	String getKeyLocalized();

	String getText();

	String getTextLocalized();
}
