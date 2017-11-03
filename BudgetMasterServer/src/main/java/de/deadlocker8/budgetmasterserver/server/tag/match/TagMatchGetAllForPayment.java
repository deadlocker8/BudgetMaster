package de.deadlocker8.budgetmasterserver.server.tag.match;

import static spark.Spark.halt;

import java.util.ArrayList;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import de.deadlocker8.budgetmasterserver.logic.database.DatabaseTagHandler;
import spark.Request;
import spark.Response;

public class TagMatchGetAllForPayment implements AdvancedRoute
{
	private DatabaseTagHandler tagHandler;
	private Gson gson;
	
	public TagMatchGetAllForPayment(DatabaseTagHandler tagHandler, Gson gson)
	{	
		this.tagHandler = tagHandler;
		this.gson = gson;
	}

	@Override
	public void before()
	{
		tagHandler.connect();
	}

	@Override
	public Object handleRequest(Request req, Response res)
	{
		if(!req.queryParams().contains("paymentID"))
		{
			halt(400, "Bad Request");
		}	

		try
		{				
			int paymentID = Integer.parseInt(req.queryMap("paymentID").value());

			if(paymentID < 0)
			{
				halt(400, "Bad Request");
			}
			
			ArrayList<Integer> tagIDs = tagHandler.getAllTagsForPayment(paymentID);
			ArrayList<Tag> tags = new ArrayList<>();
			for(Integer currentTagID : tagIDs)
			{
				Tag currentTag = tagHandler.getTagByID(currentTagID);
				if(currentTag != null)
				{
					tags.add(currentTag);
				}
			}
			
			return gson.toJson(tags);
		}
		catch(IllegalStateException ex)
		{				
			halt(500, "Internal Server Error");
		}
		catch(Exception e)
		{				
			halt(400, "Bad Request");
		}
		
		return null;
	}

	@Override
	public void after()
	{
		tagHandler.closeConnection();
	}
}