package de.deadlocker8.budgetmasterserver.logic.database.creator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.deadlocker8.budgetmasterserver.logic.Settings;
import logger.Logger;

public class SqliteDatabaseCreator extends DatabaseCreator
{
	public SqliteDatabaseCreator(Connection connection, Settings settings)
	{
		super(connection, settings);
	}

	@Override
	public ArrayList<String> getExistingTables()
	{
		ArrayList<String> tables = new ArrayList<>();
		try
		{
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet res = meta.getTables(settings.getDatabaseName(), null, "", new String[] { "TABLE" });
			while(res.next())
			{
				tables.add(res.getString("TABLE_NAME"));
			}			
		}
		catch(Exception e)
		{
			Logger.error(e);
		}
		return tables;
	}	
	
	@Override
	public void createTableCategory()
	{
		Statement stmt = null;
		String query = "CREATE TABLE `category` (`ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `Name` TEXT DEFAULT NULL, `Color` TEXT NOT NULL);";		
		String query2 = "INSERT INTO `category` (`ID`, `Name`, `Color`) VALUES(1, 'NONE', '#FFFFFF'),(2, 'Übertrag', '#FFFF00');";		
		
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
			stmt.execute(query2);
			Logger.info("Successfully created table category");
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			if(stmt != null)
			{
				try
				{
					stmt.close();
				}
				catch(SQLException e)
				{
				}
			}
		}
	}
	
	@Override
	public void createTablePayment()
	{
		Statement stmt = null;
		String query = "CREATE TABLE `payment` (" + 
				"`ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + 
				"`Name` TEXT DEFAULT NULL," + 
				"`CategoryID` INTEGER DEFAULT NULL," + 
				"`Amount` INTEGER DEFAULT NULL," + 
				"`Date` TEXT DEFAULT NULL," + 
				"`Description` TEXT DEFAULT NULL);";		
		
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
			Logger.info("Successfully created table payment");
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			if(stmt != null)
			{
				try
				{
					stmt.close();
				}
				catch(SQLException e)
				{
				}
			}
		}
	}
	
	@Override
	public void createTableRepeatingEntry()
	{
		Statement stmt = null;
		String query = "CREATE TABLE `repeating_entry` (" + 
				"`ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + 
				"`RepeatingPaymentID` INTEGER NOT NULL," + 
				"`Date` TEXT NOT NULL," + 
				"FOREIGN KEY (RepeatingPaymentID) REFERENCES repeating_payment(ID) ON DELETE CASCADE ON UPDATE CASCADE);";			
		
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
			Logger.info("Successfully created table repeating_entry");
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			if(stmt != null)
			{
				try
				{
					stmt.close();
				}
				catch(SQLException e)
				{
				}
			}
		}
	}
	
	@Override
	public void createTableRepeatingPayment()
	{
		Statement stmt = null;
		String query = "CREATE TABLE `repeating_payment` (\r\n" + 
				"`ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n" + 
				"`Name` TEXT DEFAULT NULL,\r\n" + 
				"`CategoryID` INTEGER DEFAULT NULL,\r\n" + 
				"`Amount` INTEGER DEFAULT NULL,\r\n" + 
				"`Date` TEXT DEFAULT NULL,\r\n" + 
				"`Description` TEXT DEFAULT NULL,\r\n" + 
				"`RepeatInterval` INTEGER DEFAULT NULL,\r\n" + 
				"`RepeatEndDate` TEXT DEFAULT NULL,\r\n" + 
				"`RepeatMonthDay` INTEGER DEFAULT NULL);";			
		
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
			Logger.info("Successfully created table repeating_payment");
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			if(stmt != null)
			{
				try
				{
					stmt.close();
				}
				catch(SQLException e)
				{
				}
			}
		}
	}
	
	@Override
	public void createTableTag()
	{
		Statement stmt = null;
		String query = "CREATE TABLE `tag` (`ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `Name` TEXT NOT NULL);";
		
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
			Logger.info("Successfully created table tag");
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			if(stmt != null)
			{
				try
				{
					stmt.close();
				}
				catch(SQLException e)
				{
				}
			}
		}
	}
	
	@Override
	public void createTableTagMatch()
	{
		Statement stmt = null;
		String query = "CREATE TABLE `tag_match` (`Tag_ID` INTEGER NOT NULL," + 
				"`Payment_ID` INTEGER NOT NULL," + 
				"`RepeatingPayment_ID` INTEGER NOT NULL);"; 
		
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
			Logger.info("Successfully created table tag_match");
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			if(stmt != null)
			{
				try
				{
					stmt.close();
				}
				catch(SQLException e)
				{
				}
			}
		}
	}
}