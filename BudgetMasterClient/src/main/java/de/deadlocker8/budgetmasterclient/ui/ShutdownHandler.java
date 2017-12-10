package de.deadlocker8.budgetmasterclient.ui;

import de.deadlocker8.budgetmaster.logic.ServerType;
import de.deadlocker8.budgetmaster.logic.serverconnection.ServerConnection;
import de.deadlocker8.budgetmasterclient.ui.controller.Controller;
import logger.Logger;
import tools.Worker;

public class ShutdownHandler
{
	private Thread shutdownThread;
	private Controller controller;
	
	public ShutdownHandler()
	{
		shutdownThread = new Thread(() -> {
			shutdown();
		});
	}
	
	public Thread getShutdownThread()
	{
		return shutdownThread;
	}	
	
	public void setController(Controller controller)
	{
		this.controller = controller;
	}

	public void shutdown()
	{
		if(controller.getSettings().getServerType().equals(ServerType.LOCAL))
		{
			Logger.debug("Stopping local BudgetMasterServer...");
			try
			{
				ServerConnection connection = new ServerConnection(controller.getSettings());
				connection.shutdownServer();
			}
			catch(Exception e)
			{
				Logger.error(e);
			}
		}
		Worker.shutdown();
		System.exit(0);
	}
}
