package de.deadlocker8.budgetmasterserver.server.tag.match;

import static spark.Spark.halt;

import com.google.gson.Gson;

import de.deadlocker8.budgetmasterserver.logic.database.DatabaseTagHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class TagMatchExistingForPayment implements Route
{
	private DatabaseTagHandler tagHandler;
	private Gson gson;
	
	public TagMatchExistingForPayment(DatabaseTagHandler tagHandler, Gson gson)
	{	
		this.tagHandler = tagHandler;
		this.gson = gson;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
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
}