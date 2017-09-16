package de.deadlocker8.budgetmasterserver.server.tag.match;

import static spark.Spark.halt;

import com.google.gson.Gson;

import de.deadlocker8.budgetmasterserver.logic.database.DatabaseTagHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class TagMatchExistingForRepeatingPayment implements Route
{
	private DatabaseTagHandler tagHandler;
	private Gson gson;
	
	public TagMatchExistingForRepeatingPayment(DatabaseTagHandler tagHandler, Gson gson)
	{	
		this.tagHandler = tagHandler;
		this.gson = gson;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
	{
		if(!req.queryParams().contains("tagID") || !req.queryParams().contains("repeatingPaymentID"))
		{
			halt(400, "Bad Request");
		}	

		try
		{	
			int tagID = Integer.parseInt(req.queryMap("tagID").value());
			int repeatingPaymentID = Integer.parseInt(req.queryMap("repeatingPaymentID").value());

			if(tagID < 0 || repeatingPaymentID < 0)
			{
				halt(400, "Bad Request");
			}
			
			return gson.toJson(tagHandler.isMatchExistingForRepeatingPaymentID(tagID, repeatingPaymentID));			
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