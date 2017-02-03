package de.deadlocker8.budgetmasterserver.main;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.CategoryBudget;
import de.deadlocker8.budgetmaster.logic.Payment;
import javafx.scene.paint.Color;
import logger.LogLevel;
import logger.Logger;

public class DatabaseHandler
{
	private final String URL = "jdbc:mysql://localhost:3306/";
	private final String DB_NAME = "budgetmaster";
	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private Connection connection;
	
	//DEBUG String query = "SELECT * FROM Payment WHERE YEAR(Date) = " + year + " AND MONTH(Date) = " + month + ";";

	public DatabaseHandler()
	{
		try
		{
			this.connection = DriverManager.getConnection(URL + DB_NAME, USERNAME, PASSWORD);
		}
		catch(SQLException e)
		{
			throw new IllegalStateException("Cannot connect the database!", e);
		}
	}

	public void listTables()
	{
		try
		{
			DatabaseMetaData md = connection.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);
			while(rs.next())
			{
				System.out.println(rs.getString(3));
			}
		}
		catch(SQLException e)
		{
			throw new IllegalStateException("Cannot connect the database!", e);
		}
	}

	public ArrayList<String> getPaymentTypes()
	{
		Statement stmt = null;
		String query = "SELECT Description FROM PaymentType";
		ArrayList<String> results = new ArrayList<>();
		try
		{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())
			{
				results.add(rs.getString("Description"));
			}
		}
		catch(SQLException e)
		{
			Logger.log(LogLevel.ERROR, Logger.exceptionToString(e));
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

	public ArrayList<Category> getCategories()
	{
		Statement stmt = null;
		String query = "SELECT * FROM Category";
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
			Logger.log(LogLevel.ERROR, Logger.exceptionToString(e));
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

	public ArrayList<CategoryBudget> getCategoryBudget(int year, int month)
	{
		Statement stmt = null;
		String query = "SELECT Category.Name, Category.Color, SUM(Payment.Amount) as amount FROM Payment Category WHERE Payment.CategoryID = Category.ID AND YEAR(Date) = " + year + " AND MONTH(Date) = " + month + " GROUP BY Payment.CategoryID ORDER BY SUM(Payment.Amount);";
		
		ArrayList<CategoryBudget> results = new ArrayList<>();
		try
		{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while(rs.next())
			{	
				String name = rs.getString("Name");
				String color = rs.getString("color");
				int amount = rs.getInt("amount");
				
				results.add(new CategoryBudget(name, Color.web(color), amount / 100.0));
			}				
		}
		catch(SQLException e)
		{
			Logger.log(LogLevel.ERROR, Logger.exceptionToString(e));
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
	
	//TODO incomplete --> not only payments with same year and month but repeating payments
	public ArrayList<Payment> getPayments(int year, int month)
	{
		Statement stmt = null;
		String query = "SELECT * FROM Payment, PaymentType WHERE Payment.PaymentTypeID = PaymentType.ID AND YEAR(Date) = " + year + " AND MONTH(Date) = " + month + " ORDER BY Payment.Date;";
		
		ArrayList<Payment> results = new ArrayList<>();
		try
		{
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while(rs.next())
			{	
				int ID = rs.getInt("ID");				
				String name = rs.getString("Name");			
				int amount = rs.getInt("amount");
				String date = rs.getString("Date");
				int categoryID = rs.getInt("CategoryID");
				int repeatInterval = rs.getInt("RepeatInterval");
				String repeatEndDate = rs.getString("RepeatEndDate");
				int repeatMonthDay = rs.getInt("RepeatMonthDay");				
				results.add(new Payment(ID, amount, date, categoryID, name, repeatInterval, repeatEndDate, repeatMonthDay));
			}				
		}
		catch(SQLException e)
		{
			Logger.log(LogLevel.ERROR, Logger.exceptionToString(e));
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
}