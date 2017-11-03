package de.deadlocker8.budgetmasterserver.server.category;

import static spark.Spark.halt;

import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseHandler;
import spark.Request;
import spark.Response;

public class CategoryDelete implements AdvancedRoute
{
	private DatabaseHandler handler;
	
	public CategoryDelete(DatabaseHandler handler)
	{	
		this.handler = handler;
	}

	@Override
	public void before()
	{
		handler.connect();
	}

	@Override
	public Object handleRequest(Request req, Response res)
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
				handler.deleteCategory(id);			

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

	@Override
	public void after()
	{
		handler.closeConnection();
	}
}