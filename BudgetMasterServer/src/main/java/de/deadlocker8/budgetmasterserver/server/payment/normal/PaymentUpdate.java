package de.deadlocker8.budgetmasterserver.server.payment.normal;

import static spark.Spark.halt;

import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseHandler;
import spark.Request;
import spark.Response;

public class PaymentUpdate implements AdvancedRoute
{
	private DatabaseHandler handler;
	
	public PaymentUpdate(DatabaseHandler handler)
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
		if(!req.queryParams().contains("id") ||
				!req.queryParams().contains("amount") || 
				!req.queryParams().contains("date") || 
				!req.queryParams().contains("categoryID") || 
				!req.queryParams().contains("name") || 
				!req.queryParams().contains("description"))
			{
				halt(400, "Bad Request");
			}	
			
			int id = -1;
			int amount = 0;
			int categoryID = -1;
			
			try
			{				
				id = Integer.parseInt(req.queryMap("id").value());
				amount = Integer.parseInt(req.queryMap("amount").value());
				categoryID = Integer.parseInt(req.queryMap("categoryID").value());
				
				if(id < 0)
				{
					halt(400, "Bad Request");
				}
				
				try
				{				
					handler.updateNormalPayment(id, 
												amount, 
												req.queryMap("date").value(), 
												categoryID, 
												req.queryMap("name").value(), 
												req.queryMap("description").value());			

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