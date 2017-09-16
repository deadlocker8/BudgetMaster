package de.deadlocker8.budgetmasterserver.server.tag.tag;

import static spark.Spark.halt;

import de.deadlocker8.budgetmasterserver.logic.database.DatabaseTagHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class TagAdd implements Route
{
	private DatabaseTagHandler tagHandler;
	
	public TagAdd(DatabaseTagHandler tagHandler)
	{	
		this.tagHandler = tagHandler;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
	{
		if(!req.queryParams().contains("name"))
		{
			halt(400, "Bad Request");
		}	
						
		try
		{			
			tagHandler.addTag(req.queryMap("name").value());			

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