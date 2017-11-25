package de.deadlocker8.budgetmasterserver.logic.database.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmaster.logic.payment.LatestRepeatingPayment;
import de.deadlocker8.budgetmaster.logic.payment.NormalPayment;
import de.deadlocker8.budgetmaster.logic.payment.Payment;
import de.deadlocker8.budgetmaster.logic.payment.RepeatingPayment;
import de.deadlocker8.budgetmaster.logic.payment.RepeatingPaymentEntry;
import de.deadlocker8.budgetmasterserver.logic.Settings;
import de.deadlocker8.budgetmasterserver.logic.Utils;
import logger.Logger;

public abstract class DatabaseHandler
{
	Connection connection;
	Settings settings;
	final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

	public DatabaseHandler(Settings settings) throws IllegalStateException
	{
		this.settings = settings;
	}
	
	public void connect()
	{
		try
		{
			if(connection == null || connection.isClosed())
			{				
				connection = Utils.getDatabaseConnection(settings);
			}
		}
		catch(Exception e)
		{
			Logger.error(e);
			throw new IllegalStateException("Cannot connect the database!", e);
		}
	}
	
	public void closeConnection()
	{
		try
		{
			connection.close();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
	}
	
	void closeStatement(Statement statement)
	{
		if(statement != null)
		{
			try
			{
				statement.close();
			}
			catch(SQLException e)
			{
			}
		}
	}

	/*
	 * GET
	 */
	public abstract int getLastInsertID();
		
	public DateTime getFirstNormalPaymentDate()
	{
		PreparedStatement stmt = null;
		DateTime dateTime = null;
		try
		{
			stmt = connection.prepareStatement("SELECT MIN(Date) as \"min\" FROM payment");			
			ResultSet rs = stmt.executeQuery();

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
			rs.close();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}

		return dateTime;
	}

	public DateTime getFirstRepeatingPaymentDate()
	{
		PreparedStatement stmt = null;
		DateTime dateTime = null;
		try
		{
			stmt = connection.prepareStatement("SELECT MIN(Date) as \"min\" FROM repeating_payment");
			ResultSet rs = stmt.executeQuery();

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
			rs.close();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
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
		PreparedStatement stmt = null;
		ArrayList<Category> results = new ArrayList<>();
		try
		{
			stmt = connection.prepareStatement("SELECT * FROM category ORDER BY category.ID");			
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				int id = rs.getInt("ID");
				String name = rs.getString("Name");
				String color = rs.getString("Color");

				results.add(new Category(id, name, color));
			}
			rs.close();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}

		return results;
	}

	public Category getCategory(int ID)
	{	
		PreparedStatement stmt = null;
		Category result = null;
		try
		{		
			stmt = connection.prepareStatement("SELECT * FROM category WHERE category.ID = ?");		
			stmt.setInt(1, ID);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				int id = rs.getInt("ID");
				String name = rs.getString("Name");
				String color = rs.getString("Color");

				result = new Category(id, name, color);
			}
			rs.close();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}

		return result;
	}
	
	public Category getCategory(String name, String color)
	{
		PreparedStatement stmt = null;
		Category result = null;
		try
		{
			stmt = connection.prepareStatement("SELECT * FROM category WHERE category.name = ? AND category.color = ?;");
			stmt.setString(1, name);
			stmt.setString(2, color);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				int id = rs.getInt("ID");
				String categoryName = rs.getString("Name");
				String categoryColor = rs.getString("Color");

				result = new Category(id, categoryName, categoryColor);
			}
			rs.close();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}

		return result;
	}
	
	public boolean categoryExists(int ID)
	{
		PreparedStatement stmt = null;
		boolean exists = false;
		try
		{
			stmt = connection.prepareStatement("SELECT COUNT(ID) as \"count\" FROM category WHERE category.ID = ?;");
			stmt.setInt(1, ID);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				if(rs.getInt("count") > 0)
				{
					exists = true;
				}
			}
			rs.close();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}

		return exists;
	}

	public NormalPayment getPayment(int ID)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("SELECT * FROM payment WHERE payment.ID= ?;");	
			stmt.setInt(1, ID);
			ResultSet rs = stmt.executeQuery();

			while(rs.next())
			{
				int resultID = rs.getInt("ID");
				String name = rs.getString("Name");
				int amount = rs.getInt("amount");
				String date = rs.getString("Date");
				int categoryID = rs.getInt("CategoryID");
				String description = rs.getString("Description");
				rs.close();
				return new NormalPayment(resultID, amount, date, categoryID, name, description);
			}
			rs.close();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}

		return null;
	}
	
	public ArrayList<NormalPayment> getAllNormalPayments()
	{		
		PreparedStatement stmt = null;

		ArrayList<NormalPayment> results = new ArrayList<>();
		try
		{
			stmt = connection.prepareStatement("SELECT * FROM payment;");
			
			ResultSet rs = stmt.executeQuery();

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
			rs.close();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}

		return results;
	}

	public abstract ArrayList<NormalPayment> getPayments(int year, int month);
	
	public abstract ArrayList<NormalPayment> getPaymentsBetween(String startDate, String endDate);
	
	public abstract ArrayList<RepeatingPaymentEntry> getRepeatingPayments(int year, int month);	
	
	public abstract ArrayList<RepeatingPaymentEntry> getRepeatingPaymentsBetween(String startDate, String endDate);

	public ArrayList<RepeatingPayment> getAllRepeatingPayments()
	{
		PreparedStatement stmt = null;

		ArrayList<RepeatingPayment> results = new ArrayList<>();
		try
		{
			stmt = connection.prepareStatement("SELECT * FROM repeating_payment;");			
			ResultSet rs = stmt.executeQuery();

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
			rs.close();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}

		return results;
	}

	public ArrayList<LatestRepeatingPayment> getLatestRepeatingPaymentEntries()
	{
		PreparedStatement stmt = null;

		ArrayList<LatestRepeatingPayment> results = new ArrayList<>();
		try
		{
			stmt = connection.prepareStatement("SELECT ID, RepeatingPaymentID, MAX(Date) as 'LastDate' FROM repeating_entry GROUP BY RepeatingPaymentID");		
			ResultSet rs = stmt.executeQuery();

			while(rs.next())
			{
				int resultID = rs.getInt("ID");
				int repeatingPaymentID = rs.getInt("repeatingPaymentID");
				String date = rs.getString("LastDate");

				results.add(new LatestRepeatingPayment(resultID, repeatingPaymentID, date));
			}
			rs.close();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}

		return results;
	}

	public RepeatingPayment getRepeatingPayment(int ID)
	{
		PreparedStatement stmt = null;
		RepeatingPayment result = null;
		try
		{
			stmt = connection.prepareStatement("SELECT * FROM repeating_payment WHERE ID = ?;");
			stmt.setInt(1,  ID);
			ResultSet rs = stmt.executeQuery();
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
			rs.close();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}

		return result;
	}
	
	public int getNormalPaymentMaxAmount()
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("SELECT MAX(ABS(Amount)) as'max' FROM payment;");		;
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				int result = rs.getInt("max");
				rs.close();
				return result;
			}
			rs.close();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}

		return -1;
	}
	
	public int getRepeatingPaymentMaxAmount()
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("SELECT MAX(ABS(Amount)) as'max' FROM repeating_payment;");		;
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				int result = rs.getInt("max");
				rs.close();
				return result;
			}
			rs.close();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}

		return -1;
	}

	/*
	 * DELETE
	 */
	public void deleteCategory(int ID)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("DELETE FROM category WHERE category.ID = ?;");
			stmt.setInt(1, ID);
			stmt.execute();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}
	}

	public void deletePayment(int ID)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("DELETE FROM payment WHERE payment.ID = ?;");
			stmt.setInt(1, ID);
			stmt.execute();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}
	}

	public void deleteRepeatingPayment(int ID)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("DELETE FROM repeating_payment WHERE repeating_payment.ID = ?;");
			stmt.setInt(1, ID);
			stmt.execute();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}
	}

	public abstract void deleteDatabase();

	/*
	 * ADD
	 */
	public void addCategory(String name, String color)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("INSERT INTO category (Name, Color) VALUES(?, ?);");
			stmt.setString(1, name);
			stmt.setString(2, color);
			stmt.execute();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}
	}

	public void importCategory(Category category)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("INSERT INTO category (ID, Name, Color) VALUES(?, ?, ?);");
			stmt.setInt(1,  category.getID());
			stmt.setString(2, category.getName());
			stmt.setString(3, category.getColor());
			stmt.execute();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}
	}

	public Integer addNormalPayment(int amount, String date, int categoryID, String name, String description)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("INSERT INTO payment (Amount, Date, CategoryID, Name, Description) VALUES(?, ?, ?, ?, ?);",
												Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, amount);
			stmt.setString(2, date);
			stmt.setInt(3, categoryID);
			stmt.setString(4, name);
			stmt.setString(5, description);
			stmt.execute();
			
			ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next())
            {
            	int result = rs.getInt(1);
            	rs.close();
                return result;
            }
            rs.close();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}
		
		return -1;
	}

	public Integer addRepeatingPayment(int amount, String date, int categoryID, String name, String description, int repeatInterval, String repeatEndDate, int repeatMonthDay)
	{
		PreparedStatement stmt = null;
		String correctRepeatEndDate = repeatEndDate;
		if(correctRepeatEndDate == null || correctRepeatEndDate.equals("A"))
		{
			correctRepeatEndDate = null;
		}

		try
		{
			stmt = connection.prepareStatement("INSERT INTO repeating_payment (Amount, Date, CategoryID, Name, RepeatInterval, RepeatEndDate, RepeatMonthDay, Description) VALUES(?, ?, ?, ?, ?, ?, ?, ?);",
												Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, amount);
			stmt.setString(2, date);
			stmt.setInt(3, categoryID);
			stmt.setString(4, name);
			stmt.setInt(5, repeatInterval);
			stmt.setString(6, correctRepeatEndDate);
			stmt.setInt(7, repeatMonthDay);
			stmt.setString(8, description);
			stmt.execute();
			
			ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next())
            {
            	int result = rs.getInt(1);
            	rs.close();
                return result;
            }
            rs.close();
		}
		catch(SQLException e)
		{			
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}
		
		return -1;
	}
	
	public void addRepeatingPaymentEntry(int repeatingPaymentID, String date)
	{
		PreparedStatement stmt = null;
		
		try
		{
			stmt = connection.prepareStatement("INSERT INTO repeating_entry (RepeatingPaymentID, Date) VALUES(?, ?);");
			stmt.setInt(1, repeatingPaymentID);
			stmt.setString(2, date);
			stmt.execute();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}
	}

	/*
	 * UPDATE
	 */
	public void updateCategory(int ID, String name, String color)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("UPDATE category SET name=? , color=? WHERE ID = ?;");
			stmt.setString(1, name);
			stmt.setString(2, color);
			stmt.setInt(3, ID);
			stmt.executeUpdate();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}
	}

	public void updateNormalPayment(int ID, int amount, String date, int categoryID, String name, String description)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("UPDATE payment SET amount=?, date=?, categoryID=?, name=?, description=? WHERE ID=?;");
			stmt.setInt(1, amount);
			stmt.setString(2, date);
			stmt.setInt(3, categoryID);
			stmt.setString(4, name);
			stmt.setString(5, description);
			stmt.setInt(6, ID);
			stmt.executeUpdate();
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeStatement(stmt);
		}
	}
}