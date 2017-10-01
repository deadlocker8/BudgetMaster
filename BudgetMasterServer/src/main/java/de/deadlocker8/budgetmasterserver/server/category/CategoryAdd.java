package de.deadlocker8.budgetmasterserver.server.category;

import static spark.Spark.halt;

import de.deadlocker8.budgetmasterserver.logic.database.DatabaseHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class CategoryAdd implements Route
{
	private DatabaseHandler handler;
	
	public CategoryAdd( DatabaseHandler handler)
	{	
		this.handler = handler;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
	{
		if(!req.queryParams().contains("name") || !req.queryParams().contains("color"))
		{
			halt(400, "Bad Request");
		}	
						
		try
		{			
			handler.addCategory(req.queryMap("name").value(), "#" + req.queryMap("color").value());			

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