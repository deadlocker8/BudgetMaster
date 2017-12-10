package de.deadlocker8.budgetmasterserver.server.shutdown;

import java.util.Timer;
import java.util.TimerTask;

import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import logger.Logger;
import spark.Request;
import spark.Response;

public class Shutdown implements AdvancedRoute
{	
	private boolean shutdownInProgress;
	
	public Shutdown(boolean shutdownInProgress)
	{
		this.shutdownInProgress = shutdownInProgress;
	}

	@Override
	public void before()
	{
	}

	@Override
	public Object handleRequest(Request req, Response res)
	{
		Logger.info("Shutting down server due to client request");
		if(!shutdownInProgress)
		{
			shutdownInProgress = true;
			TimerTask task = new TimerTask() 
			{
				@Override
				public void run()
				{
					Logger.info("Shutdown DONE");
					System.exit(0);		
				}
			};
			
			Timer timer = new Timer();
			timer.schedule(task, 2000);
			return "";
		}
		else
		{
			Logger.info("Shutdown is already scheduled");
			return "";
		}
	}

	@Override
	public void after()
	{
	}
}