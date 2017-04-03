package de.deadlocker8.budgetmasterserver.main;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import logger.Logger;

public class DatabaseCreator
{
	private Connection connection;
	private Settings settings;

	public DatabaseCreator(Connection connection, Settings settings)
	{
		this.connection = connection;
		this.settings = settings;
		Logger.info("Checking tables...");
		createTables(getExistingTables());	
		Logger.info("Checking tables [DONE]");
	}

	private ArrayList<String> getExistingTables()
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
	}
	
	private void createTableCategory()
	{
		Statement stmt = null;
		String query = "CREATE TABLE `category` (`ID` int(11) NOT NULL COMMENT 'ID'," +
				 " `Name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'category name'," +
				  "`Color` text COLLATE utf8_unicode_ci NOT NULL COMMENT 'color hexcode'" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;";		
		String query2 = "INSERT INTO `category` (`ID`, `Name`, `Color`) VALUES(1, 'NONE', '#FFFFFF'),(2, 'Übertrag', '#FFFF00');";		
		String query3 = "ALTER TABLE `category` ADD PRIMARY KEY (`ID`);";		
		String query4 = "ALTER TABLE `category` MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID', AUTO_INCREMENT=3;";	
		
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
			stmt.execute(query2);
			stmt.execute(query3);
			stmt.execute(query4);
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
	
	private void createTablePayment()
	{
		Statement stmt = null;
		String query = "CREATE TABLE `payment` (" +
					 "`ID` int(11) NOT NULL COMMENT 'ID'," +
					 "`Name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'payment name (description)'," +
					 "`CategoryID` int(11) DEFAULT NULL COMMENT 'category ID'," +
					 "`Amount` int(11) DEFAULT NULL COMMENT 'amount in cents'," +
					 "`Date` date DEFAULT NULL COMMENT 'payment date',"	 +
					 "`Description` varchar(150) DEFAULT NULL COMMENT 'optional description'" +
					 ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;";			
		String query2 = "ALTER TABLE `payment` ADD PRIMARY KEY (`ID`);";		
		String query3 = "ALTER TABLE `payment` MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID';";	
		
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
			stmt.execute(query2);
			stmt.execute(query3);
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
	
	private void createTableRepeatingEntry()
	{
		Statement stmt = null;
		String query = "CREATE TABLE `repeating_entry` (" +
					  "`ID` int(11) NOT NULL," +
					  "`RepeatingPaymentID` int(11) NOT NULL," +
					  "`Date` date NOT NULL" +
					") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;";			
		String query2 = "ALTER TABLE `repeating_entry` ADD PRIMARY KEY (`ID`), ADD KEY `RepeatingPaymentID` (`RepeatingPaymentID`);";		
		String query3 = "ALTER TABLE `repeating_entry` MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID';";	
		String query4 = "ALTER TABLE `repeating_entry` ADD CONSTRAINT `constraint_1` FOREIGN KEY (`RepeatingPaymentID`) REFERENCES `repeating_payment` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;";
		
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
			stmt.execute(query2);
			stmt.execute(query3);
			stmt.execute(query4);
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
	
	private void createTableRepeatingPayment()
	{
		Statement stmt = null;
		String query = "CREATE TABLE `repeating_payment` (" +
					  "`ID` int(11) NOT NULL COMMENT 'ID'," +
					  "`Name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'payment name (description)'," +
					  "`CategoryID` int(11) DEFAULT NULL COMMENT 'category ID'," +
					  "`Amount` int(11) DEFAULT NULL COMMENT 'amount in cents'," +
					  "`Date` date DEFAULT NULL COMMENT 'payment date'," +
					  "`Description` varchar(150) DEFAULT NULL COMMENT 'optional description'," +
					  "`RepeatInterval` int(11) DEFAULT NULL COMMENT 'repeat interval in days'," +
					  "`RepeatEndDate` date DEFAULT NULL COMMENT 'repeat end date'," +
					  "`RepeatMonthDay` int(11) DEFAULT NULL COMMENT 'day in month on which payment repeats'" +
					") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;";			
		String query2 = "ALTER TABLE `repeating_payment` ADD PRIMARY KEY (`ID`);";		
		String query3 = "ALTER TABLE `repeating_payment` MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID';";	
		
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
			stmt.execute(query2);
			stmt.execute(query3);
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
}