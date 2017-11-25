package de.deadlocker8.budgetmasterserver.logic.database.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.payment.NormalPayment;
import de.deadlocker8.budgetmaster.logic.payment.RepeatingPaymentEntry;
import de.deadlocker8.budgetmasterserver.logic.Settings;
import logger.Logger;

public class SqliteDatabaseHandler extends DatabaseHandler
{
	public SqliteDatabaseHandler(Settings settings) throws IllegalStateException
	{
		super(settings);
		connect();
	}
	
	@Override
	public int getLastInsertID()
	{
		PreparedStatement stmt = null;
		int lastInsertID = 0;
		try
		{
			stmt = connection.prepareStatement("SELECT last_insert_rowid() as `ID`");		
			ResultSet rs = stmt.executeQuery();

			while(rs.next())
			{
				lastInsertID = rs.getInt("ID");				
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

		return lastInsertID;
	}
	
	@Override
	public ArrayList<NormalPayment> getPayments(int year, int month)
	{
		PreparedStatement stmt = null;

		ArrayList<NormalPayment> results = new ArrayList<>();
		try
		{
			stmt = connection.prepareStatement("SELECT * FROM payment WHERE strftime('%Y', Date) = ? AND strftime('%m', Date) = ?;");
			stmt.setString(1, String.format("%02d", year));
			stmt.setString(2, String.format("%02d", month));
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
	
	@Override
	public ArrayList<RepeatingPaymentEntry> getRepeatingPayments(int year, int month)
	{
		PreparedStatement stmt = null;

		ArrayList<RepeatingPaymentEntry> results = new ArrayList<>();
		try
		{
			stmt = connection.prepareStatement("SELECT repeating_entry.ID, repeating_entry.RepeatingPaymentID, repeating_entry.Date, repeating_payment.Name, repeating_payment.CategoryID, repeating_payment.Amount, repeating_payment.RepeatInterval, repeating_payment.RepeatEndDate, repeating_payment.RepeatMonthDay, repeating_payment.Description FROM repeating_entry, repeating_payment WHERE repeating_entry.RepeatingPaymentID = repeating_payment.ID AND strftime('%Y', repeating_entry.Date) = ? AND strftime('%m', repeating_entry.Date) = ?;");
			stmt.setString(1, String.format("%02d", year));
			stmt.setString(2, String.format("%02d", month));		
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
	
	@Override
	public ArrayList<NormalPayment> getPaymentsBetween(String startDate, String endDate)
	{	
		PreparedStatement stmt = null;

		ArrayList<NormalPayment> results = new ArrayList<>();
		try
		{
			stmt = connection.prepareStatement("SELECT * FROM payment WHERE Date BETWEEN ? AND ?;");
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
	
	@Override
	public ArrayList<RepeatingPaymentEntry> getRepeatingPaymentsBetween(String startDate, String endDate)
	{
		PreparedStatement stmt = null;

		ArrayList<RepeatingPaymentEntry> results = new ArrayList<>();
		try
		{
			stmt = connection.prepareStatement("SELECT repeating_entry.ID, repeating_entry.RepeatingPaymentID, repeating_entry.Date, repeating_payment.Name, repeating_payment.CategoryID, repeating_payment.Amount, repeating_payment.RepeatInterval, repeating_payment.RepeatEndDate, repeating_payment.RepeatMonthDay, repeating_payment.Description FROM repeating_entry, repeating_payment WHERE repeating_entry.RepeatingPaymentID = repeating_payment.ID AND repeating_entry.Date BETWEEN ? AND ?;");
			stmt.setString(1, startDate);
			stmt.setString(2, endDate);
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
	
	@Override
	public void deleteDatabase()
	{
		Statement stmt = null;
		String tableCategory = "DROP TABLE IF EXISTS category;";
		String tablePayment = "DROP TABLE IF EXISTS payment;";
		String tableRepeatingPayment = "DROP TABLE IF EXISTS repeating_payment;";
		String tableRepeatingEntry = "DROP TABLE IF EXISTS repeating_entry;";
		String tableTag = "DROP TABLE IF EXISTS tag;";
		String tableTagMatch = "DROP TABLE IF EXISTS tag_match;";
		try
		{
			stmt = connection.createStatement();
			stmt.execute(tableCategory);
			Logger.info("Deleted table: category");
			stmt.execute(tablePayment);
			Logger.info("Deleted table: payment");
			stmt.execute(tableRepeatingPayment);
			Logger.info("Deleted table: repeating_payment");
			stmt.execute(tableRepeatingEntry);
			Logger.info("Deleted table: repeating_entry");
			stmt.execute(tableTag);
			Logger.info("Deleted table: tag");
			stmt.execute(tableTagMatch);
			Logger.info("Deleted table: tag_match");
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