package de.deadlocker8.budgetmasterclient.ui.commandLine;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import de.deadlocker8.budgetmaster.logic.Settings;

public class CommandBundle
{
	private CommandLineController controller;
	private ResourceBundle languageBundle;
	private Settings settings;

	public CommandBundle(Settings settings)
	{
		this.settings = settings;
	}

	public CommandLineController getController()
	{
		return controller;
	}	
	
	public ResourceBundle getLanguageBundle()
	{
		return languageBundle;
	}
	
	public String getString(String key)
	{
		return languageBundle.getString(key);
	}
	
	public String getString(String key, Object... args)
	{
		return MessageFormat.format(languageBundle.getString(key), args);
	}

	public void setController(CommandLineController controller)
	{
		this.controller = controller;
	}

	public void setLanguageBundle(ResourceBundle languageBundle)
	{
		this.languageBundle = languageBundle;
	}

	public Settings getSettings()
	{
		return settings;
	}
}