package de.deadlocker8.budgetmasterserver.server.payment.repeating;

import static spark.Spark.halt;

import de.deadlocker8.budgetmasterserver.main.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.main.Settings;
import spark.Request;
import spark.Response;
import spark.Route;

public class RepeatingPaymentAdd implements Route
{
	private Settings settings;
	
	public RepeatingPaymentAdd(Settings settings)
	{	
		this.settings = settings;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
	{
		if(!req.queryParams().contains("amount") || !req.queryParams().contains("date") || !req.queryParams().contains("categoryID") || !req.queryParams().contains("name") || !req.queryParams().contains("repeatInterval") || !req.queryParams().contains("repeatEndDate") || !req.queryParams().contains("repeatMonthDay"))
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
				DatabaseHandler handler = new DatabaseHandler(settings);
				handler.addRepeatingPayment(amount, req.queryMap("date").value(), categoryID, req.queryMap("name").value(), repeatInterval, req.queryMap("repeatEndDate").value(), repeatMonthDay);			

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