package de.deadlocker8.budgetmasterserver.main;

import java.sql.Connection;
import java.sql.DriverManager;

import logger.Logger;

public class DatabaseImporter
{
	private Connection connection;
	
	public DatabaseImporter(Settings settings) throws IllegalStateException
    {
        try
        {
            this.connection = DriverManager.getConnection(settings.getDatabaseUrl() + settings.getDatabaseName() + "?useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin", settings.getDatabaseUsername(), settings.getDatabasePassword());
        }
        catch(Exception e)
        {
            Logger.error(e);
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
	
	public void importDatabase(Database database)
	{
	   
	}
	
	private void importCategories()
	{	   
        //TODO
	}
	
	private void importNormalPayments()
    {      
        //TODO
    }
	
	private void importRepeatingPayments()
    {      
        //TODO
    }
}