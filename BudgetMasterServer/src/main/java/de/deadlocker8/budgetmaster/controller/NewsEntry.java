package de.deadlocker8.budgetmaster.controller;

import de.thecodelabs.utils.util.Localization;

import java.text.MessageFormat;

public record NewsEntry(String headline, String description)
{
	public static NewsEntry createWithLocalizationKey(String shortKey)
	{
		return createWithLocalizationKeys(MessageFormat.format("news.{0}.headline", shortKey),
				MessageFormat.format("news.{0}.description", shortKey));
	}

	public static NewsEntry createWithLocalizationKeys(String headlineKey, String descriptionKey)
	{
		return new NewsEntry(Localization.getString(headlineKey), Localization.getString(descriptionKey));
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
