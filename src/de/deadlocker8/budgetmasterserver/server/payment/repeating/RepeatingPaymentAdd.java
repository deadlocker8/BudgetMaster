package de.deadlocker8.budgetmasterserver.server.payment.repeating;

import static spark.Spark.halt;

import de.deadlocker8.budgetmasterserver.main.DatabaseHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class RepeatingPaymentAdd implements Route
{
	private DatabaseHandler handler;
	
	public RepeatingPaymentAdd(DatabaseHandler handler)
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
			!req.queryParams().contains("repeatInterval") || 
			!req.queryParams().contains("repeatEndDate") || 
			!req.queryParams().contains("repeatMonthDay") || 
			!req.queryParams().contains("description"))
		{				
			halt(400, "Bad Request");
		}	
			
		int amount = 0;
		int categoryID = 0;
		int repeatInterval = 0;
		int repeatMonthDay = 0;
		
		try
		{				
			amount = Integer.parseInt(req.queryMap("amount").value());
			categoryID = Integer.parseInt(req.queryMap("categoryID").value());
			repeatInterval = Integer.parseInt(req.queryMap("repeatInterval").value());
			repeatMonthDay = Integer.parseInt(req.queryMap("repeatMonthDay").value());
			
			try
			{				
				handler.addRepeatingPayment(amount, 
											req.queryMap("date").value(), 
											categoryID, 
											req.queryMap("name").value(), 
											req.queryMap("description").value(), 
											repeatInterval, 
											req.queryMap("repeatEndDate").value(), 
											repeatMonthDay);			

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