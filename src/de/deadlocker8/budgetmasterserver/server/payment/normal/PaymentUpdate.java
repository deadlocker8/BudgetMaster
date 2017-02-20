package de.deadlocker8.budgetmasterserver.server.payment.normal;

import static spark.Spark.halt;

import de.deadlocker8.budgetmasterserver.main.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.main.Settings;
import spark.Request;
import spark.Response;
import spark.Route;

public class PaymentUpdate implements Route
{
	private Settings settings;
	
	public PaymentUpdate(Settings settings)
	{	
		this.settings = settings;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
	{
		if(!req.queryParams().contains("id") ||!req.queryParams().contains("amount") || !req.queryParams().contains("date") || !req.queryParams().contains("categoryID") || !req.queryParams().contains("name"))
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
				DatabaseHandler handler = new DatabaseHandler(settings);
				handler.updateNormalPayment(id, amount, req.queryMap("date").value(), categoryID, req.queryMap("name").value());			

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
}