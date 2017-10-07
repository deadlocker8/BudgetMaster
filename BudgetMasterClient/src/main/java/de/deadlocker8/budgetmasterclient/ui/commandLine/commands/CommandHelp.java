package de.deadlocker8.budgetmasterclient.ui.commandLine.commands;

import java.util.MissingResourceException;

/**
 * prints help for given command
 */
public class CommandHelp extends Command
{
	public CommandHelp()
	{
		super();	
		super.keyword = "help";		
		super.numberOfParams = 1;
		super.helptText = "help.help";
	}

	@Override
	public void execute(String[] command, CommandBundle bundle)
	{		
		if(!isValid(command))
		{			
			bundle.getController().print(bundle.getLanguageBundle().getString("error.invalid.arguments"));
			return;
		}			
		
		for(Command cmd : PossibleCommands.possibleCommands)
		{
			if(cmd.getKeyword().equals(command[1]))
			{	
				try
				{
					bundle.getController().print(bundle.getLanguageBundle().getString("help." + command[1]));
				}
				catch(MissingResourceException e)
				{
					bundle.getController().print(bundle.getLanguageBundle().getString("error.general"));
				}
				return;
			}
		}		
				
		bundle.getController().print(bundle.getLanguageBundle().getString("error.no.help"));		
	}
}
