package de.deadlocker8.budgetmasterserver.server.payment.normal;

import static spark.Spark.halt;

import de.deadlocker8.budgetmasterserver.main.DatabaseHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class PaymentAdd implements Route
{
	private DatabaseHandler handler;
	
	public PaymentAdd(DatabaseHandler handler)
	{	
		this.handler = handler;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
	{
		if(!req.queryParams().contains("amount") || 
			!req.queryParams().contains("date") || 
			!req.queryParams().contains("categoryID") || 
			!req.queryParams().contains("name") || 
			!req.queryParams().contains("description"))
		{				
			halt(400, "Bad Request");
		}	
			
		int amount = 0;
		int categoryID = 0;			
		
		try
		{				
			amount = Integer.parseInt(req.queryMap("amount").value());
			categoryID = Integer.parseInt(req.queryMap("categoryID").value());				
			
			try
			{			
				handler.addNormalPayment(amount, 
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
			e.printStackTrace();
			halt(400, "Bad Request");
		}
		
		return "";
	}
}