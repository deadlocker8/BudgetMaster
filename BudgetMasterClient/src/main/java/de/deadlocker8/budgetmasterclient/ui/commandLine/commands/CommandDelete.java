package de.deadlocker8.budgetmasterclient.ui.commandLine.commands;

import de.deadlocker8.budgetmaster.logic.serverconnection.ServerConnection;
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
			bundle.getController().print(bundle.getString("delete.success", "client logfile"));
			return;
		}
		
		if(command[1].equals("log-server"))
		{
			try
			{
				ServerConnection connection = new ServerConnection(bundle.getParentController().getSettings());
				connection.deleteLog();
				bundle.getController().print(bundle.getString("delete.success", "server logfile"));
			}
			catch(Exception e)
			{
				bundle.getController().print(bundle.getString("delete.error.connection"));
			}
			
			return;
		}
			
		bundle.getController().print(bundle.getString("error.invalid.parameter", command[1], keyword));
	}
}