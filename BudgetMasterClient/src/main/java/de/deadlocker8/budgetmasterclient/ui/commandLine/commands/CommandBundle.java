package de.deadlocker8.budgetmasterclient.ui.commandLine.commands;

import java.util.ResourceBundle;

import de.deadlocker8.budgetmasterclient.ui.commandLine.CommandLineController;

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

	public void setController(CommandLineController controller)
	{
		this.controller = controller;
	}

	public void setLanguageBundle(ResourceBundle languageBundle)
	{
		this.languageBundle = languageBundle;
	}	
}