package de.deadlocker8.budgetmasterserver.server.payment.repeating;

import static spark.Spark.halt;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.payment.RepeatingPayment;
import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.database.handler.DatabaseHandler;
import spark.Request;
import spark.Response;

public class RepeatingPaymentGet implements AdvancedRoute
{
	private DatabaseHandler handler;
	private Gson gson;
	
	public RepeatingPaymentGet(DatabaseHandler handler, Gson gson)
	{
		this.handler = handler;
		this.gson = gson;
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
				RepeatingPayment payment = handler.getRepeatingPayment(id);			

				return gson.toJson(payment);
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