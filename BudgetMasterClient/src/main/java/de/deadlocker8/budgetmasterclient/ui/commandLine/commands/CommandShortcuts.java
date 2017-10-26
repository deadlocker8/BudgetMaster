package de.deadlocker8.budgetmasterclient.ui.commandLine.commands;

import de.deadlocker8.budgetmasterclient.ui.commandLine.CommandBundle;

/**
 * Lists all available Shortcuts
 */
public class CommandShortcuts extends Command
{
	public CommandShortcuts()
	{		
		super.keyword = "shortcuts";		
		super.numberOfParams = 0;
		super.helptText = "help.shortcuts";
	}

	@Override
	public void execute(String[] command, CommandBundle bundle)
	{		
		bundle.getController().print("Available Shortcuts:");
		bundle.getController().print(bundle.getString("info.shortcuts"));
	}
}