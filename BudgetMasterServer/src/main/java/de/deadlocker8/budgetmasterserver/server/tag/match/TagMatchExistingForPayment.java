package de.deadlocker8.budgetmasterserver.server.tag.match;

import static spark.Spark.halt;

import com.google.gson.Gson;

import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.database.taghandler.DatabaseTagHandler;
import spark.Request;
import spark.Response;

public class TagMatchExistingForPayment implements AdvancedRoute
{
	private DatabaseTagHandler tagHandler;
	private Gson gson;
	
	public TagMatchExistingForPayment(DatabaseTagHandler tagHandler, Gson gson)
	{	
		this.tagHandler = tagHandler;
		this.gson = gson;
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
			
			return gson.toJson(tagHandler.isMatchExistingForPaymentID(tagID, paymentID));
		}
		catch(IllegalStateException ex)
		{				
			halt(500, "Internal Server Error");
		}
		catch(Exception e)
		{				
			halt(400, "Bad Request");
		}
		
		return null;
	}

	@Override
	public void after()
	{
		tagHandler.closeConnection();		
	}
}