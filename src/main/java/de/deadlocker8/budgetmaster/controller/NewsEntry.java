package de.deadlocker8.budgetmaster.controller;

import de.thecodelabs.utils.util.Localization;

public class NewsEntry
{
	private final String headline;
	private final String description;

	public NewsEntry(String headline, String description)
	{
		this.headline = headline;
		this.description = description;
	}

	public static NewsEntry createWithLocalizationKeys(String headlineKey, String descriptionKey)
	{
		return new NewsEntry(Localization.getString(headlineKey), Localization.getString(descriptionKey));
	}

	public String getHeadline()
	{
		return headline;
	}

	public String getDescription()
	{
		return description;
	}

	@Override
	public String toString()
	{
		return "NewsEntry{" +
				"headline='" + headline + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
