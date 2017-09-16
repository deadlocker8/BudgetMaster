package de.deadlocker8.budgetmasterserver.server.payment.normal;

import static spark.Spark.halt;

import com.google.gson.Gson;

import de.deadlocker8.budgetmasterserver.logic.database.DatabaseHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class PaymentAdd implements Route
{
	private DatabaseHandler handler;
	private Gson gson;
	
	public PaymentAdd(DatabaseHandler handler, Gson gson)
	{	
		this.handler = handler;
		this.gson = gson;
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
				Integer id = handler.addNormalPayment(amount, 
													  req.queryMap("date").value(),
													  categoryID, 
													  req.queryMap("name").value(), 
													  req.queryMap("description").value());			

				return gson.toJson(id);
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
}