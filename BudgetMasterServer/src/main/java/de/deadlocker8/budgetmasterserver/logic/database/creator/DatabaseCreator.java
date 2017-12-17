package de.deadlocker8.budgetmasterserver.logic.database.creator;

import java.sql.Connection;
import java.util.ArrayList;

import de.deadlocker8.budgetmasterserver.logic.Settings;
import logger.Logger;

public abstract class DatabaseCreator
{
	Connection connection;
	Settings settings;

	public DatabaseCreator(Connection connection, Settings settings)
	{
		this.connection = connection;
		this.settings = settings;		
	}
	
	public void createTables()
	{
		Logger.info("Checking tables...");
		createTables(getExistingTables());	
		Logger.info("Checking tables [DONE]");
	}

	public abstract ArrayList<String> getExistingTables();	
	
	private void createTables(ArrayList<String> existingTables)
	{
		if(!existingTables.contains("category"))
		{
			createTableCategory();
		}
		
		if(!existingTables.contains("payment"))
		{
			createTablePayment();
		}
		
		if(!existingTables.contains("repeating_payment"))
		{
			createTableRepeatingPayment();
		}
		
		if(!existingTables.contains("repeating_entry"))
		{
			createTableRepeatingEntry();
		}
		
		if(!existingTables.contains("tag"))
		{
			createTableTag();
		}
		
		if(!existingTables.contains("tag_match"))
		{
			createTableTagMatch();
		}
	}
	
	public abstract void createTableCategory();
	
	public abstract void createTablePayment();
	
	public abstract void createTableRepeatingEntry();
	
	public abstract void createTableRepeatingPayment();
	
	public abstract void createTableTag();
	
	public abstract void createTableTagMatch();
}