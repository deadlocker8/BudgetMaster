package de.deadlocker8.budgetmasterserver.logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.NormalPayment;
import de.deadlocker8.budgetmaster.logic.RepeatingPayment;
import javafx.scene.paint.Color;
import logger.Logger;

public class DatabaseExporter
{
	private Connection connection;
	
	public DatabaseExporter(Settings settings) throws IllegalStateException
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
	
	public Database exportDatabase()
	{
	    ArrayList<Category> categories = getAllCategories();
	    ArrayList<NormalPayment> normalPayments = getAllNormalPayments();
	    ArrayList<RepeatingPayment> repeatingPayments = getAllRepeatingPayments();
	    
	    return new Database(categories, normalPayments, repeatingPayments);
	}
	
	private ArrayList<Category> getAllCategories()
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
	
	private ArrayList<NormalPayment> getAllNormalPayments()
    {
	    Statement stmt = null;
        String query = "SELECT * FROM payment;";

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
	
	private ArrayList<RepeatingPayment> getAllRepeatingPayments()
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
                String description = rs.getString("Description");
                int amount = rs.getInt("amount");
                String date = rs.getString("Date");             
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
}