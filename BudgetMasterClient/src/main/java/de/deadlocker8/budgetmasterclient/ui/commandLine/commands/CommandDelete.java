package de.deadlocker8.budgetmasterclient.ui.commandLine.commands;

import de.deadlocker8.budgetmasterclient.ui.commandLine.CommandBundle;
import logger.Logger;

public class CommandDelete extends Command
{
	public CommandDelete()
	{		
		super.keyword = "delete";		
		super.numberOfParams = 1;
		super.helptText = "help.delete";
	}

	@Override
	public void execute(String[] command, CommandBundle bundle)
	{		
		if(!isValid(command))
		{			
			bundle.getController().print(bundle.getString("error.invalid.arguments"));
			return;
		}
		
		if(command[1].equals("log-client"))
		{
			Logger.clearLogFile();
			bundle.getController().print(bundle.getString("delete.success", "log-client", Logger.getFolder()));
			return;
		}
		
		if(command[1].equals("log-server"))
		{
			//TODO
//			Logger.clearLogFile();
//			bundle.getController().print(bundle.getString("delete.success", "log-server", Logger.getFolder()));
			return;
		}
			
		bundle.getController().print(bundle.getString("error.invalid.parameter", command[1], keyword));
	}
}