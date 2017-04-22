package de.deadlocker8.budgetmasterserver.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.LatestRepeatingPayment;
import de.deadlocker8.budgetmaster.logic.NormalPayment;
import de.deadlocker8.budgetmaster.logic.Payment;
import de.deadlocker8.budgetmaster.logic.RepeatingPayment;
import de.deadlocker8.budgetmaster.logic.RepeatingPaymentEntry;
import javafx.scene.paint.Color;
import logger.Logger;
import tools.ConvertTo;

public class DatabaseHandler
{
	private Connection connection;
	private final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

	public DatabaseHandler(Settings settings) throws IllegalStateException
	{
		try
		{
			this.connection = DriverManager.getConnection(settings.getDatabaseUrl() + settings.getDatabaseName() + "?useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin", settings.getDatabaseUsername(), settings.getDatabasePassword());
			new DatabaseCreator(connection, settings);
			Logger.info("Successfully initialized database (" + settings.getDatabaseUrl() + settings.getDatabaseName() + ")");
		}
		catch(Exception e)
		{
			Logger.error(e);
			throw new IllegalStateException("Cannot connect the database!", e);
		}
	}

	/*
	 * GET
	 */
	public DateTime getFirstNormalPaymentDate()
	{
		Statement stmt = null;
		String query = "SELECT MIN(Date) as \"min\" FROM payment";
		DateTime dateTime = null;
		try
		{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while(rs.next())
			{
				String min = rs.getString("min");
				if(min == null)
				{
					dateTime = null;
				}
				else
				{
					dateTime = formatter.parseDateTime(rs.getString("min"));
				}
			}
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

		return dateTime;
	}
	
	public DateTime getFirstRepeatingPaymentDate()
	{
		Statement stmt = null;
		String query = "SELECT MIN(Date) as \"min\" FROM repeating_payment";
		DateTime dateTime = null;
		try
		{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while(rs.next())
			{
				String min = rs.getString("min");
				if(min == null)
				{
					dateTime = null;
				}
				else
				{
					dateTime = formatter.parseDateTime(rs.getString("min"));
				}
			}
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

		return dateTime;
	}

	public int getRestForAllPreviousMonths(int year, int month)
	{		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("MM.yyyy");
		String dateString = String.valueOf(month) + "." + year;
		DateTime currentDate = formatter.parseDateTime(dateString);		
		
		DateTime firstNormalPaymentDate = getFirstNormalPaymentDate();
		if(firstNormalPaymentDate == null)
		{
			firstNormalPaymentDate = currentDate;
		}
		DateTime firstRepeatingPaymentDate = getFirstRepeatingPaymentDate();
		if(firstRepeatingPaymentDate == null)
		{
			firstRepeatingPaymentDate = currentDate;
		}
		
		DateTime firstDate = firstNormalPaymentDate;
		if(firstRepeatingPaymentDate.isBefore(firstNormalPaymentDate))
		{
			firstDate = firstRepeatingPaymentDate;
		}	
	
		if(firstDate.isAfter(currentDate))
		{
			return 0;
		}

		int startYear = firstDate.getYear();
		int startMonth = firstDate.getMonthOfYear();
		int totalRest = 0;
		
		while(startYear < year || startMonth < month)
		{
			totalRest += getRest(startYear, startMonth);			

			startMonth++;
			if(startMonth > 12)
			{
				startMonth = 1;
				startYear++;
			}
		}
		return totalRest;
	}

	public int getRest(int year, int month)
	{
		ArrayList<Payment> payments = new ArrayList<>();
		payments.addAll(getPayments(year, month));
		payments.addAll(getRepeatingPayments(year, month));
		
		int rest = 0;
		for(Payment currentPayment : payments)
		{
			rest += currentPayment.getAmount();
		}
		
		return rest;
	}

	public ArrayList<Category> getCategories()
	{
		Statement stmt = null;
		String query = "SELECT * FROM category ORDER BY category.ID";
		ArrayList<Category> results = new ArrayList<>();
		try
		{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())
			{
				int id = rs.getInt("ID");
				String name = rs.getString("Name");
				String color = rs.getString("Color");

				results.add(new Category(id, name, Color.web(color)));
			}
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

		return results;
	}
	
	public Category getCategory(int ID)
	{
		Statement stmt = null;
		String query = "SELECT * FROM category WHERE category.ID = " + ID;	
		Category result = null;
		try
		{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);			
			while(rs.next())
			{
				int id = rs.getInt("ID");
				String name = rs.getString("Name");
				String color = rs.getString("Color");

				result = new Category(id, name, Color.web(color));
			}
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

		return result;
	}
	
	public NormalPayment getPayment(int ID)
	{
		Statement stmt = null;
		String query = "SELECT * FROM payment WHERE payment.ID= " + ID;
		try
		{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while(rs.next())
			{
				int resultID = rs.getInt("ID");
				String name = rs.getString("Name");
				int amount = rs.getInt("amount");
				String date = rs.getString("Date");
				int categoryID = rs.getInt("CategoryID");
				String description = rs.getString("Description");

				return new NormalPayment(resultID, amount, date, categoryID, name, description);
			}
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

		return null;
	}

	public ArrayList<NormalPayment> getPayments(int year, int month)
	{
		Statement stmt = null;
		String query = "SELECT * FROM payment WHERE YEAR(Date) = " + year + " AND  MONTH(Date) = " + month;

		ArrayList<NormalPayment> results = new ArrayList<>();
		try
		{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while(rs.next())
			{
				int resultID = rs.getInt("ID");				
				String name = rs.getString("Name");
				int amount = rs.getInt("amount");
				String date = rs.getString("Date");				
				int categoryID = rs.getInt("CategoryID");
				String description = rs.getString("Description");
			
				results.add(new NormalPayment(resultID, amount, date, categoryID, name, description));
			}
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

		return results;
	}

	public ArrayList<RepeatingPaymentEntry> getRepeatingPayments(int year, int month)
	{
		Statement stmt = null;
		String query = "SELECT repeating_entry.ID, repeating_entry.RepeatingPaymentID, repeating_entry.Date, repeating_payment.Name, repeating_payment.CategoryID, repeating_payment.Amount, repeating_payment.RepeatInterval, repeating_payment.RepeatEndDate, repeating_payment.RepeatMonthDay, repeating_payment.Description FROM repeating_entry, repeating_payment WHERE repeating_entry.RepeatingPaymentID = repeating_payment.ID AND YEAR(repeating_entry.Date) = " + year + " AND MONTH(repeating_entry.Date) = " + month;

		ArrayList<RepeatingPaymentEntry> results = new ArrayList<>();
		try
		{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while(rs.next())
			{
				int resultID = rs.getInt("ID");
				int repeatingPaymentID = rs.getInt("repeatingPaymentID");				
				String name = rs.getString("Name");
				String description = rs.getString("Description");
				int amount = rs.getInt("amount");
				String date = rs.getString("Date");				
				int categoryID = rs.getInt("CategoryID");
				int repeatInterval = rs.getInt("RepeatInterval");
				String repeatEndDate = rs.getString("RepeatEndDate");
				int repeatMonthDay = rs.getInt("RepeatMonthDay");		
			
				results.add(new RepeatingPaymentEntry(resultID, repeatingPaymentID, date, amount, categoryID, name, description,repeatInterval, repeatEndDate, repeatMonthDay));
			}
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

		return results;
	}
	
	public ArrayList<RepeatingPayment> getAllRepeatingPayments()
	{		
		Statement stmt = null;
		String query = "SELECT * FROM repeating_payment;";

		ArrayList<RepeatingPayment> results = new ArrayList<>();
		try
		{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while(rs.next())
			{
				int resultID = rs.getInt("ID");				
				String name = rs.getString("Name");
				int amount = rs.getInt("amount");
				String date = rs.getString("Date");		
				String description = rs.getString("Description");
				int categoryID = rs.getInt("CategoryID");
				int repeatInterval = rs.getInt("RepeatInterval");
				String repeatEndDate = rs.getString("RepeatEndDate");
				int repeatMonthDay = rs.getInt("RepeatMonthDay");			

				results.add(new RepeatingPayment(resultID, amount, date, categoryID, name, description, repeatInterval, repeatEndDate, repeatMonthDay));
			}
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

		return results;
	}
	
	public ArrayList<LatestRepeatingPayment> getLatestRepeatingPaymentEntries()
	{
		Statement stmt = null;
		String query = "SELECT ID, RepeatingPaymentID, MAX(Date) as 'LastDate' FROM repeating_entry GROUP BY RepeatingPaymentID";

		ArrayList<LatestRepeatingPayment> results = new ArrayList<>();
		try
		{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while(rs.next())
			{
				int resultID = rs.getInt("ID");
				int repeatingPaymentID = rs.getInt("repeatingPaymentID");				
				String date = rs.getString("LastDate");
			
				results.add(new LatestRepeatingPayment(resultID, repeatingPaymentID, date));
			}
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

		return results;
	}
	
	public RepeatingPayment getRepeatingPayment(int ID)
	{
		Statement stmt = null;
		String query = "SELECT * FROM repeating_payment WHERE ID = " + ID;	
		RepeatingPayment result = null;
		try
		{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);			
			while(rs.next())
			{
				int id = rs.getInt("ID");				
				int amount = rs.getInt("amount");
				String date = rs.getString("Date");
				int categoryID = rs.getInt("CategoryID");
				String name = rs.getString("Name");
				String description = rs.getString("Description");
				int repeatInterval = rs.getInt("repeatInterval");
				String repeatEndDate = rs.getString("repeatEndDate");
				int repeatMonthDay = rs.getInt("repeatMonthDay");

				result = new RepeatingPayment(id, amount, date, categoryID, name, description, repeatInterval, repeatEndDate, repeatMonthDay);
			}
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

		return result;
	}

	/*
	 * DELETE
	 */
	public void deleteCategory(int ID)
	{
		Statement stmt = null;
		String query = "DELETE FROM category WHERE category.ID = " + ID;
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
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
	
	public void deletePayment(int ID)
	{
		Statement stmt = null;
		String query = "DELETE FROM payment WHERE payment.ID = " + ID;
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
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
	
	public void deleteRepeatingPayment(int ID)
	{
		Statement stmt = null;
		String query = "DELETE FROM repeating_payment WHERE repeating_payment.ID = " + ID;
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
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

	/*
	 * ADD
	 */
	public void addCategory(String name, Color color)
	{
		Statement stmt = null;
		String query = "INSERT INTO category (Name, Color) VALUES('" + name + "' , '" + ConvertTo.toRGBHexWithoutOpacity(color) + "');";
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
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

	public void addNormalPayment(int amount, String date, int categoryID, String name, String description)
	{
		Statement stmt = null;
		String query = "INSERT INTO payment (Amount, Date, CategoryID, Name, Description) VALUES('" + amount + "' , '" + date + "' , '" + categoryID + "' , '" + name + "' , '" + description + "');";
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
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
	
	public void addRepeatingPayment(int amount, String date, int categoryID, String name, String description, int repeatInterval, String repeatEndDate, int repeatMonthDay)
	{
		Statement stmt = null;
		String query;		
		//A is placeholder for empty repeatEndDate
		if(repeatEndDate.equals("A") || repeatEndDate == null)
		{			
			query = "INSERT INTO repeating_payment (Amount, Date, CategoryID, Name, RepeatInterval, RepeatEndDate, RepeatMonthDay, Description) VALUES('" + amount + "' , '" + date + "' , '" + categoryID + "' , '" + name + "' , '" + repeatInterval + "' , NULL , '" + repeatMonthDay + "' , '" + description + "');";
		}
		else
		{
			query = "INSERT INTO repeating_payment (Amount, Date, CategoryID, Name, RepeatInterval, RepeatEndDate, RepeatMonthDay, Description) VALUES('" + amount + "' , '" + date + "' , '" + categoryID + "' , '" + name + "' , '" + repeatInterval + "' , '" + repeatEndDate + "' , '" + repeatMonthDay + "' , '" + description + "');";
		}
		
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
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
	
	public void addRepeatingPaymentEntry(int repeatingPaymentID, String date)
	{
		Statement stmt = null;
		String query;		
		query = "INSERT INTO repeating_entry (RepeatingPaymentID, Date) VALUES('" +  repeatingPaymentID + "' , '" + date + "');";
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
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

	/*
	 * UPDATE
	 */
	public void updateCategory(int ID, String name, Color color)
	{
		Statement stmt = null;
		String query = "UPDATE category SET name='" + name + "' , color='" + ConvertTo.toRGBHexWithoutOpacity(color) + "' WHERE ID = " + ID + ";";
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
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
	
	public void updateNormalPayment(int ID, int amount, String date, int categoryID, String name, String description)
	{
		Statement stmt = null;
		String query = "UPDATE payment SET amount = '" + amount + "', date='" + date + "', categoryID='" + categoryID + "', name='" + name + "', description='" + description + "' WHERE ID = " + ID + ";";
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
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