package de.deadlocker8.budgetmaster.statistics;


import de.deadlocker8.budgetmaster.services.EntityType;
import de.thecodelabs.utils.util.Localization;

import java.text.MessageFormat;

public class StatisticItem
{
	private final String icon;
	private final String text;
	private final String backgroundColor;
	private final String textColor;

	public StatisticItem(EntityType entityType, int numberOfItems, String textColor)
	{
		this(entityType.getIcon(),
				MessageFormat.format("{0} {1}", numberOfItems, Localization.getString(entityType.getLocalizationKey())),
				entityType.getColor(),
				textColor);
	}

	public StatisticItem(String icon, String text, String backgroundColor, String textColor)
	{
		this.icon = icon;
		this.text = text;
		this.backgroundColor = backgroundColor;
		this.textColor = textColor;
	}

	public String getIcon()
	{
		return icon;
	}

	public String getText()
	{
		return text;
	}

	public String getBackgroundColor()
	{
		return backgroundColor;
	}

	public String getTextColor()
	{
		return textColor;
	}

	@Override
	public String toString()
	{
		return "StatisticItem{" +
				"icon='" + icon + '\'' +
				", text='" + text + '\'' +
				", backgroundColor=" + backgroundColor +
				", textColor=" + textColor +
				'}';
	}
}
