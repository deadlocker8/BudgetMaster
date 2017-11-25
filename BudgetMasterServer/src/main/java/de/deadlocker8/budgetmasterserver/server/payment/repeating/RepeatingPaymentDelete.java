package de.deadlocker8.budgetmasterserver.server.payment.repeating;

import static spark.Spark.halt;

import java.util.ArrayList;

import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.database.handler.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.logic.database.taghandler.DatabaseTagHandler;
import spark.Request;
import spark.Response;

public class RepeatingPaymentDelete implements AdvancedRoute
{
	private DatabaseHandler handler;
	private DatabaseTagHandler tagHandler;
	
	public RepeatingPaymentDelete(DatabaseHandler handler, DatabaseTagHandler tagHandler)
	{
		this.handler = handler;
		this.tagHandler = tagHandler;
	}

	@Override
	public void before()
	{
		handler.connect();
		tagHandler.connect();
	}

	@Override
	public Object handleRequest(Request req, Response res)
	{
		if(!req.queryParams().contains("id"))
		{
			halt(400, "Bad Request");
		}			
		
		int id = -1;		
		
		try
		{				
			id = Integer.parseInt(req.queryMap("id").value());
			
			if(id < 0)
			{
				halt(400, "Bad Request");
			}
			
			try
			{					
				handler.deleteRepeatingPayment(id);	
				ArrayList<Integer> tagIDs = tagHandler.getAllTagsForRepeatingPayment(id);
				for(Integer currentTagID : tagIDs)
				{
					tagHandler.deleteTagMatchForRepeatingPayment(currentTagID, id);
				}

				return "";
			}
			catch(IllegalStateException ex)
			{
				halt(500, "Internal Server Error");
			}
		}
		catch(Exception e)
		{
			halt(400, "Bad Request");
		}
		
		return "";
	}

	@Override
	public void after()
	{
		handler.closeConnection();
		tagHandler.closeConnection();		
	}
}