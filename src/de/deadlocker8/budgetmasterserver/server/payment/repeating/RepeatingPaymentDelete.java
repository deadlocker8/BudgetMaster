package de.deadlocker8.budgetmasterserver.server.payment.repeating;

import static spark.Spark.halt;

import de.deadlocker8.budgetmasterserver.main.DatabaseHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class RepeatingPaymentDelete implements Route
{
	private DatabaseHandler handler;
	
	public RepeatingPaymentDelete(DatabaseHandler handler)
	{
		this.handler = handler;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
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
				handler.deleteRepeatingPayment(id);			

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