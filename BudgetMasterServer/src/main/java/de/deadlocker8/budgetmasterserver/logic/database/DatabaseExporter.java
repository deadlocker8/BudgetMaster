package de.deadlocker8.budgetmasterserver.logic.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmaster.logic.database.Database;
import de.deadlocker8.budgetmaster.logic.payment.NormalPayment;
import de.deadlocker8.budgetmaster.logic.payment.RepeatingPayment;
import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmaster.logic.tag.TagMatch;
import de.deadlocker8.budgetmasterserver.logic.Settings;
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
	    ArrayList<Tag> tags = getAllTags();
	    ArrayList<TagMatch> tagMatches = getAllTagMatches();
	    
	    return new Database(categories, normalPayments, repeatingPayments, tags, tagMatches);
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
	
	private ArrayList<Category> getAllCategories()
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
	
	private ArrayList<NormalPayment> getAllNormalPayments()
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
	
	private ArrayList<RepeatingPayment> getAllRepeatingPayments()
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
            closeConnection(stmt);
        }

        return results;
    }
	
	private ArrayList<Tag> getAllTags()
    {
	    PreparedStatement stmt = null;
        ArrayList<Tag> results = new ArrayList<>();
        try
        {
        	stmt = connection.prepareStatement("SELECT * FROM tag;");
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                int resultID = rs.getInt("ID");                               
                String name = rs.getString("Name");               
            
                results.add(new Tag(resultID, name));
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
	
	private ArrayList<TagMatch> getAllTagMatches()
    {
	    PreparedStatement stmt = null;
        ArrayList<TagMatch> results = new ArrayList<>();
        try
        {
        	stmt = connection.prepareStatement("SELECT * FROM tag_match;");
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                int tagID = rs.getInt("Tag_ID");
                int paymentID = rs.getInt("Payment_ID");
                int repeatingPaymentID = rs.getInt("RepeatingPayment_ID");                             
            
                results.add(new TagMatch(tagID, paymentID, repeatingPaymentID));
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
}