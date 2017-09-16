package de.deadlocker8.budgetmasterserver.server.tag.match;

import static spark.Spark.halt;

import de.deadlocker8.budgetmasterserver.logic.database.DatabaseTagHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class TagMatchDeleteForRepeatingPayment implements Route
{
	private DatabaseTagHandler tagHandler;
	
	public TagMatchDeleteForRepeatingPayment(DatabaseTagHandler tagHandler)
	{	
		this.tagHandler = tagHandler;
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
			
			tagHandler.deleteTagMatchForRepeatingPayment(tagID, repeatingPaymentID);

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
}