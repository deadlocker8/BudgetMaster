package de.deadlocker8.budgetmasterserver.server.payment.repeating;

import static spark.Spark.halt;

import java.util.ArrayList;

import de.deadlocker8.budgetmasterserver.logic.database.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseTagHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class RepeatingPaymentDelete implements Route
{
	private DatabaseHandler handler;
	private DatabaseTagHandler tagHandler;
	
	public RepeatingPaymentDelete(DatabaseHandler handler, DatabaseTagHandler tagHandler)
	{
		this.handler = handler;
		this.tagHandler = tagHandler;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
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
}