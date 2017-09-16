package de.deadlocker8.budgetmasterserver.logic.database;

import java.sql.Connection;
import java.sql.DriverManager;
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
import logger.Logger;

public class DatabaseHandler
{
	private Connection connection;
	private final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

	public DatabaseHandler(Settings settings) throws IllegalStateException
	{
		try
		{
			this.connection = DriverManager.getConnection(settings.getDatabaseUrl() + settings.getDatabaseName() + "?useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin&autoReconnect=true&wait_timeout=86400", settings.getDatabaseUsername(), settings.getDatabasePassword());
			new DatabaseCreator(connection, settings);
			Logger.info("Successfully initialized database (" + settings.getDatabaseUrl() + settings.getDatabaseName() + ")");
		}
		catch(Exception e)
		{
			Logger.error(e);
			throw new IllegalStateException("Cannot connect the database!", e);
		}
	}
	
	private void closeConnection(Statement statement)
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
	public int getLastInsertID()
	{
		PreparedStatement stmt = null;
		int lastInsertID = 0;
		try
		{
			stmt = connection.prepareStatement("SELECT LAST_INSERT_ID();");		
			ResultSet rs = stmt.executeQuery();

			while(rs.next())
			{
				lastInsertID = rs.getInt("LAST_INSERT_ID()");				
			}
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
		}

		return lastInsertID;
	}
	
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
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
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
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
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
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
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
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
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
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
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
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
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

				return new NormalPayment(resultID, amount, date, categoryID, name, description);
			}
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
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
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
		}

		return results;
	}

	public ArrayList<NormalPayment> getPayments(int year, int month)
	{
		PreparedStatement stmt = null;

		ArrayList<NormalPayment> results = new ArrayList<>();
		try
		{
			stmt = connection.prepareStatement("SELECT * FROM payment WHERE YEAR(Date) = ? AND  MONTH(Date) = ?;");
			stmt.setInt(1, year);
			stmt.setInt(2, month);
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
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
		}

		return results;
	}
	
	public ArrayList<NormalPayment> getPaymentsBetween(String startDate, String endDate)
	{	
		PreparedStatement stmt = null;

		ArrayList<NormalPayment> results = new ArrayList<>();
		try
		{
			stmt = connection.prepareStatement("SELECT * FROM payment WHERE DATE(Date) BETWEEN ? AND ?;");
			stmt.setString(1, startDate);
			stmt.setString(2, endDate);			
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
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
		}

		return results;
	}

	public ArrayList<RepeatingPaymentEntry> getRepeatingPayments(int year, int month)
	{
		PreparedStatement stmt = null;

		ArrayList<RepeatingPaymentEntry> results = new ArrayList<>();
		try
		{
			stmt = connection.prepareStatement("SELECT repeating_entry.ID, repeating_entry.RepeatingPaymentID, repeating_entry.Date, repeating_payment.Name, repeating_payment.CategoryID, repeating_payment.Amount, repeating_payment.RepeatInterval, repeating_payment.RepeatEndDate, repeating_payment.RepeatMonthDay, repeating_payment.Description FROM repeating_entry, repeating_payment WHERE repeating_entry.RepeatingPaymentID = repeating_payment.ID AND YEAR(repeating_entry.Date) = ? AND MONTH(repeating_entry.Date) = ?;");
			stmt.setInt(1, year);
			stmt.setInt(2, month);			
			ResultSet rs = stmt.executeQuery();

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

				results.add(new RepeatingPaymentEntry(resultID, repeatingPaymentID, date, amount, categoryID, name, description, repeatInterval, repeatEndDate, repeatMonthDay));
			}
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
		}

		return results;
	}
	
	public ArrayList<RepeatingPaymentEntry> getRepeatingPaymentsBetween(String startDate, String endDate)
	{
		PreparedStatement stmt = null;

		ArrayList<RepeatingPaymentEntry> results = new ArrayList<>();
		try
		{
			stmt = connection.prepareStatement("SELECT repeating_entry.ID, repeating_entry.RepeatingPaymentID, repeating_entry.Date, repeating_payment.Name, repeating_payment.CategoryID, repeating_payment.Amount, repeating_payment.RepeatInterval, repeating_payment.RepeatEndDate, repeating_payment.RepeatMonthDay, repeating_payment.Description FROM repeating_entry, repeating_payment WHERE repeating_entry.RepeatingPaymentID = repeating_payment.ID AND DATE(repeating_entry.Date) BETWEEN ? AND ?;");
			stmt.setString(1, startDate);
			stmt.setString(2,  endDate);
			ResultSet rs = stmt.executeQuery();

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
			closeConnection(stmt);
		}

		return results;
	}

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
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
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
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
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
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
		}

		return result;
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
			closeConnection(stmt);
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
			closeConnection(stmt);
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
			closeConnection(stmt);
		}
	}

	public void deleteDatabase()
	{
		Statement stmt = null;
		String tableCategory = "DROP TABLE IF EXISTS category;";
		String tablePayment = "DROP TABLE IF EXISTS payment;";
		String tableRepeatingPayment = "DROP TABLE IF EXISTS repeating_payment;";
		String tableRepeatingEntry = "DROP TABLE IF EXISTS repeating_entry;";
		try
		{
			stmt = connection.createStatement();
			stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");
			stmt.execute(tableCategory);
			Logger.info("Deleted table: category");
			stmt.execute(tablePayment);
			Logger.info("Deleted table: payment");
			stmt.execute(tableRepeatingPayment);
			Logger.info("Deleted table: repeating_payment");
			stmt.execute(tableRepeatingEntry);
			Logger.info("Deleted table: repeating_entry");
			stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
		}
	}

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
			closeConnection(stmt);
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
			closeConnection(stmt);
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
                return rs.getInt(1);
            }
		}
		catch(SQLException e)
		{
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
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
                return rs.getInt(1);
            }
		}
		catch(SQLException e)
		{			
			Logger.error(e);
		}
		finally
		{
			closeConnection(stmt);
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
			closeConnection(stmt);
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
			closeConnection(stmt);
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
			closeConnection(stmt);
		}
	}
}