package de.deadlocker8.budgetmasterserver.server.tag.match;

import static spark.Spark.halt;

import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.database.taghandler.DatabaseTagHandler;
import spark.Request;
import spark.Response;

public class TagMatchAddForPayment implements AdvancedRoute
{
	private DatabaseTagHandler tagHandler;
	
	public TagMatchAddForPayment(DatabaseTagHandler tagHandler)
	{	
		this.tagHandler = tagHandler;
	}

	@Override
	public void before()
	{
		tagHandler.connect();
	}

	@Override
	public Object handleRequest(Request req, Response res)
	{
		if(!req.queryParams().contains("tagID") || !req.queryParams().contains("paymentID"))
		{
			halt(400, "Bad Request");
		}	

		try
		{	
			int tagID = Integer.parseInt(req.queryMap("tagID").value());
			int paymentID = Integer.parseInt(req.queryMap("paymentID").value());

			if(tagID < 0 || paymentID < 0)
			{
				halt(400, "Bad Request");
			}
			
			tagHandler.addTagMatchForPayment(tagID, paymentID);

			return "";
		}
		catch(IllegalStateException ex)
		{				
			halt(500, "Internal Server Error");
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
		tagHandler.closeConnection();
	}
}