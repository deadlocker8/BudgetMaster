package de.deadlocker8.budgetmasterserver.server.tag.match;

import static spark.Spark.halt;

import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.database.taghandler.DatabaseTagHandler;
import spark.Request;
import spark.Response;

public class TagMatchAddForRepeatingPayment implements AdvancedRoute
{
	private DatabaseTagHandler tagHandler;
	
	public TagMatchAddForRepeatingPayment(DatabaseTagHandler tagHandler)
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
			
			tagHandler.addTagMatchForRepeatingPayment(tagID, repeatingPaymentID);

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