package de.deadlocker8.budgetmasterserver.server.payment.search;

import static spark.Spark.halt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmaster.logic.payment.NormalPayment;
import de.deadlocker8.budgetmaster.logic.payment.Payment;
import de.deadlocker8.budgetmaster.logic.payment.PaymentJSONSerializer;
import de.deadlocker8.budgetmaster.logic.payment.RepeatingPayment;
import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseTagHandler;
import spark.Request;
import spark.Response;

public class PaymentSearch implements AdvancedRoute
{
	private DatabaseHandler handler;
	private DatabaseTagHandler tagHandler;

	public PaymentSearch(DatabaseHandler handler, DatabaseTagHandler tagHandler)
	{
		this.handler = handler;
		this.tagHandler = tagHandler;
	}

	private boolean meetsCriteria(Request req, Payment payment)
	{
		boolean otherChecksThanAmount = false;
		
		if(req.queryMap("query").value().toLowerCase().equals(""))
			return checkAmount(req, payment);
		
		if(!req.queryParams().contains("name") 
			&& !req.queryParams().contains("description") 
			&& !req.queryParams().contains("categoryName")
			&& !req.queryParams().contains("tags")
			&& !req.queryParams().contains("minAmount")
			&& !req.queryParams().contains("maxAmount"))
			return false;

		if(req.queryParams().contains("name"))
		{
			otherChecksThanAmount = true;
			if(payment.getName().toLowerCase().contains(req.queryMap("query").value().toLowerCase()))
			{
				return checkAmount(req, payment);
			}
		}

		if(req.queryParams().contains("description"))
		{
			otherChecksThanAmount = true;
			if(payment.getDescription().toLowerCase().contains(req.queryMap("query").value().toLowerCase()))
			{
				return checkAmount(req, payment);
			}
		}

		if(req.queryParams().contains("categoryName"))
		{
			otherChecksThanAmount = true;
			int id = payment.getCategoryID();
			//TODO
			if(id == -1)
				return false;

			Category category = handler.getCategory(payment.getCategoryID());		
			if(category.getName().toLowerCase().contains(req.queryMap("query").value().toLowerCase()))
			{
				return checkAmount(req, payment);
			}
		}
		
		if(req.queryParams().contains("tags"))
		{
			otherChecksThanAmount = true;
			ArrayList<Integer> tagIDs = new ArrayList<>();
			if(payment instanceof NormalPayment)
			{
				tagIDs = tagHandler.getAllTagsForPayment(payment.getID());				
			}
			else
			{
				tagIDs = tagHandler.getAllTagsForRepeatingPayment(payment.getID());
			}
			
			if(tagIDs.size() > 0) 
			{				
				for(Integer currentTagID : tagIDs)
				{
					Tag currentTag = tagHandler.getTagByID(currentTagID);
					if(currentTag != null)
					{
						if(currentTag.getName().toLowerCase().contains(req.queryMap("query").value().toLowerCase())) 
						{
							return checkAmount(req, payment);
						}
					}
				}				
			}
		}
		
		if(otherChecksThanAmount)
		{
			return false;
		}
		else
		{
			return checkAmount(req, payment);
		}
	}
	
	private boolean checkAmount(Request req, Payment payment)
	{
		if(req.queryParams().contains("minAmount") && req.queryParams().contains("maxAmount"))
		{
			try
			{
				int minAmount = Integer.parseInt(req.queryMap("minAmount").value());
				int maxAmount = Integer.parseInt(req.queryMap("maxAmount").value());			
				int amount = Math.abs(payment.getAmount());
				
				if(amount >= minAmount && amount <= maxAmount)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			catch(NumberFormatException e)
			{
				halt(400, "Bad Request");
			}
		}
			
		return true;		
	}

	@Override
	public void before()
	{
		handler.connect();
		tagHandler.connect();
	}

	@Override
	public Object handleRequest(Request req, Response res)
	{
		try
		{
			ArrayList<Payment> payments = new ArrayList<>();
			ArrayList<NormalPayment> normalPayments = handler.getAllNormalPayments();
			ArrayList<RepeatingPayment> repeatingPayments = handler.getAllRepeatingPayments();
			for(Payment currentPayment : normalPayments)
			{
				if(meetsCriteria(req, currentPayment))
				{
					payments.add(currentPayment);
				}
			}

			for(Payment currentPayment : repeatingPayments)
			{
				if(meetsCriteria(req, currentPayment))
				{
					payments.add(currentPayment);
				}
			}

			Collections.sort(payments, new Comparator<Payment>()
			{
				@Override
				public int compare(Payment o1, Payment o2)
				{
					return o1.getDate().compareTo(o2.getDate());
				}
			});
			
			return PaymentJSONSerializer.serializePaymentList(payments);
		}
		catch(IllegalStateException ex)
		{
			halt(500, "Internal Server Error");
		}

		return null;
	}

	@Override
	public void after()
	{
		handler.closeConnection();
		tagHandler.closeConnection();		
	}
}