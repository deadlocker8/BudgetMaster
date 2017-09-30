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
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseTagHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class PaymentSearch implements Route
{
	private DatabaseHandler handler;
	private DatabaseTagHandler tagHandler;

	public PaymentSearch(DatabaseHandler handler, DatabaseTagHandler tagHandler)
	{
		this.handler = handler;
		this.tagHandler = tagHandler;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
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

	private boolean meetsCriteria(Request req, Payment payment)
	{
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
			if(payment.getName().toLowerCase().contains(req.queryMap("query").value().toLowerCase()))
			{
				return checkAmount(req, payment);
			}
			else
			{
				return false;
			}
		}

		if(req.queryParams().contains("description"))
		{
			if(payment.getDescription().toLowerCase().contains(req.queryMap("query").value().toLowerCase()))
			{
				return checkAmount(req, payment);
			}
			else
			{
				return false;
			}
		}

		if(req.queryParams().contains("categoryName"))
		{
			int id = payment.getCategoryID();
			if(id == -1)
				return false;

			Category category = handler.getCategory(payment.getCategoryID());		
			if(category.getName().toLowerCase().contains(req.queryMap("query").value().toLowerCase()))
			{
				return checkAmount(req, payment);
			}
			else
			{
				return false;
			}
		}
		
		if(req.queryParams().contains("tags"))
		{
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
						else
						{
							return false;
						}
					}
				}				
			}
		}

		/*
		 * check here again if amount should be considered
		 * otherwise the following situation is evaluated incorrectly:
		 * 
		 * -at least one of the previous criteria is enabled (e.g. search by name)
		 * -payment doesn't match the criteria
		 * --> checkAmount() is being processed
		 * --> if amount is not in request (search shouldn't search by amount) the checkAmount()-Method returns true
		 * --> payment that doesn't match the criteria AND shouldn't be searched by amount should be evaluated as FALSE instead of TRUE
		 */
		if(req.queryParams().contains("minAmount") && req.queryParams().contains("maxAmount"))
		{
			return checkAmount(req, payment);
		}
		return false;		
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
}