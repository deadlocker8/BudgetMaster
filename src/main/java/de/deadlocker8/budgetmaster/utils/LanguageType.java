package de.deadlocker8.budgetmaster.utils;

import java.util.Locale;

public enum LanguageType
{
	GERMAN("Deutsch", Locale.GERMAN, "german"),
	ENGLISH("English", Locale.ENGLISH, "english");
	
	private String name;
	private Locale locale;
	private String iconName;
	
	LanguageType(String name, Locale locale, String iconName)
	{
		this.name = name;
		this.locale = locale;
		this.iconName = iconName;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Locale getLocale()
	{
		return locale;
	}

	public String getIconName()
	{
		return iconName;
	}

	public static LanguageType fromName(String name)
	{
		for(LanguageType type : values())
		{
			if(type.getName().equals(name))
			{
				return type;
			}
		}

		return null;
	}
}