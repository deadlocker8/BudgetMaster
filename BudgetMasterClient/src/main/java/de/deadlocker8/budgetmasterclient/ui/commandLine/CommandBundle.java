package de.deadlocker8.budgetmasterclient.ui.commandLine;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class CommandBundle
{
	private CommandLineController controller;
	private ResourceBundle languageBundle;	

	public CommandBundle()
	{
		
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
}