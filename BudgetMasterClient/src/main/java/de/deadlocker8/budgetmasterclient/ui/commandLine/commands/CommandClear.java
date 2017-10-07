package de.deadlocker8.budgetmasterclient.ui.commandLine.commands;

/**
 * Clears the history log and console
 */
public class CommandClear extends Command
{
	public CommandClear()
	{		
		super.keyword = "clear";		
		super.numberOfParams = 0;
		super.helptText = "help.clear";
	}

	@Override
	public void execute(String[] command, CommandBundle bundle)
	{		
		if(!isValid(command))
		{			
			bundle.getController().print(bundle.getLanguageBundle().getString("error.invalid.arguments"));
			return;
		}	
		
		bundle.getController().clearHistory();
		bundle.getController().clearHistoryLog();
		bundle.getController().clearConsole();		
		bundle.getController().printPrompt();
	}
}