package de.deadlocker8.budgetmasterclient.ui.commandLine.commands;

import de.deadlocker8.budgetmaster.logic.serverconnection.ServerConnection;
import de.deadlocker8.budgetmaster.logic.serverconnection.ServerInformation;
import de.deadlocker8.budgetmaster.logic.updater.VersionInformation;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmasterclient.ui.commandLine.CommandBundle;
import tools.Localization;

/**
 * prints help for given command
 */
public class CommandInfo extends Command
{
	public CommandInfo()
	{
		super();	
		super.keyword = "info";		
		super.numberOfParams = 1;
		super.helptText = "help.info";
	}

	@Override
	public void execute(String[] command, CommandBundle bundle)
	{		
		if(!isValid(command))
		{			
			bundle.getController().print(bundle.getString("error.invalid.arguments"));
			return;
		}
		
		if(command[1].equals("client"))
		{	
			String text = Localization.getString(Strings.APP_NAME) 
					+ " v" + Localization.getString(Strings.VERSION_NAME) 
					+ " (" + Localization.getString(Strings.VERSION_CODE) 
					+ ") from " + Localization.getString(Strings.VERSION_DATE);
			bundle.getController().print(text);
			return;
		}
		
		if(command[1].equals("server"))
		{			
			try
			{
				ServerConnection connection = new ServerConnection(bundle.getParentController().getSettings());
				ServerInformation serverInfo = connection.getServerInfo();
				VersionInformation versionInfo = serverInfo.getVersionInfo();
				
				String text = "BudgetMasterServer v" + versionInfo.getVersionName() + " (" + versionInfo.getVersionCode() + ") from " + versionInfo.getDate() + "\n"
				+ "Listening on port " + serverInfo.getServerPort() + "\n"
				+ "Connected with user \"" + serverInfo.getDatabaseUsername() + "\" to database \"" + serverInfo.getDatabaseName() + "@" + serverInfo.getDatabaseUrl() + "\"\n"
				+ "Keystore location is: \"" + serverInfo.getKeystorePath() + "\"";			
				
				bundle.getController().print(text);
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
