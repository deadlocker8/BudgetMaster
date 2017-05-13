package de.deadlocker8.budgetmasterserver.server.database;

import static spark.Spark.halt;

import java.util.ArrayList;

import org.joda.time.DateTime;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.NormalPayment;
import de.deadlocker8.budgetmasterserver.main.Database;
import de.deadlocker8.budgetmasterserver.main.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.main.DatabaseImporter;
import de.deadlocker8.budgetmasterserver.main.Settings;
import de.deadlocker8.budgetmasterserver.server.updater.RepeatingPaymentUpdater;
import logger.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

public class DatabaseImport implements Route 
{
    private DatabaseHandler handler;
    private Settings settings;
    private Gson gson;

    public DatabaseImport(DatabaseHandler handler, Settings settings, Gson gson)
	{
	    this.handler = handler;
		this.settings = settings;
		this.gson = gson;
	}

    @Override
	public Object handle(Request req, Response res) throws Exception
	{
	    if(!req.queryParams().contains("delete"))
        {
            halt(400, "Bad Request");
        }
	    //TODO json input?
	    String databaseJSON = "";
	    
	    
	    try
	    {
	        boolean delete = Boolean.parseBoolean(req.queryMap("delete").value());  
	       
	        
//TODO
//    		try
//    		{		        
//	        	Database database = gson.fromJson(databaseJSON, Database.class);
	        
//    		    if(delete)
//              {
//                    handler.deleteDatabase();
//                    handler = new DatabaseHandler(settings); 
//              }
//    		    
//    		    DatabaseImporter importer = new DatabaseImporter(handler);	  
//    		    importer.importDatabase(database);		    
//    			return "";
//    		}
//    		catch(Exception e)
//    		{
//    		    Logger.error(e);
//    			halt(500, "Internal Server Error");
//    		}		
	    }
        catch(Exception e)
        {
            halt(400, "Bad Request");
        }
		
		return null;
	}
}