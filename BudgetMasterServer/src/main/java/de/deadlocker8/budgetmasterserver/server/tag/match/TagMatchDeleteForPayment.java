package de.deadlocker8.budgetmasterserver.server.tag.match;

import static spark.Spark.halt;

import de.deadlocker8.budgetmasterserver.logic.database.DatabaseTagHandler;
import de.deadlocker8.budgetmasterserver.server.updater.TagUpdater;
import spark.Request;
import spark.Response;
import spark.Route;

public class TagMatchDeleteForPayment implements Route
{
	private DatabaseTagHandler tagHandler;
	
	public TagMatchDeleteForPayment(DatabaseTagHandler tagHandler)
	{	
		this.tagHandler = tagHandler;
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
			
			tagHandler.deleteTagMatchForPayment(tagID, paymentID);
			
			TagUpdater tagUpdater = new TagUpdater(tagHandler);
			tagUpdater.deleteTagsIfNotReferenced();

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