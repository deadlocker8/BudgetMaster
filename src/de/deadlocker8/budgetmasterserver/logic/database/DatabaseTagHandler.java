package de.deadlocker8.budgetmasterserver.logic.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmasterserver.logic.Settings;
import logger.Logger;

public class DatabaseTagHandler
{
	private Connection connection;
	
	public DatabaseTagHandler(Settings settings) throws IllegalStateException
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
	
	public ArrayList<Tag> getAllTags()
	{	   
        PreparedStatement stmt = null;
        ArrayList<Tag> results = new ArrayList<>();
        try
        {
        	stmt = connection.prepareStatement("SELECT * FROM tag ORDER BY tag.Name");            
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                int id = rs.getInt("ID");
                String name = rs.getString("Name");

                results.add(new Tag(id, name));
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
	
	public Tag getTagByID(int ID)
    {
	    PreparedStatement stmt = null;
	    Tag tag = null;
        try
        {
        	stmt = connection.prepareStatement("SELECT * FROM tag WHERE tag.ID= ?;");	
			stmt.setInt(1, ID);
			ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
            	int id = rs.getInt("ID");
            	String name = rs.getString("Name");
            
                tag = new Tag(id, name);
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

        return tag;
    }
	
	public void addTag(String name)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("INSERT INTO tag (Name) VALUES(?);");
			stmt.setString(1, name);
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
	
	public void deleteTag(int ID)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("DELETE FROM tag WHERE tag.ID = ?;");
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
	
	public boolean isMatchExistingForPaymentID(int tagID, int paymentID)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("SELECT * FROM tag_match WHERE tag_match.Tag_ID = ? AND tag_match.Payment_ID = ?;");
			stmt.setInt(1, tagID);
			stmt.setInt(2, paymentID);
			ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
            	return true;
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
		
		return false;
	}
	
	public boolean isMatchExistingForRepeatingPaymentID(int tagID, int repeatingPaymentID)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("SELECT * FROM tag_match WHERE tag_match.Tag_ID = ? AND tag_match.RepeatingPayment_ID = ?;");
			stmt.setInt(1, tagID);
			stmt.setInt(2, repeatingPaymentID);
			ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
            	return true;
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
		
		return false;
	}
	
	public void addTagMatchForPayment(int tagID, int paymentID)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("INSERT INTO tag_match (Tag_ID, Payment_ID, RepeatingPayment_ID) VALUES(?, ?, ?);");
			stmt.setInt(1, tagID);
			stmt.setInt(2, paymentID);
			stmt.setInt(3, -1);
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
	
	public void addTagMatchForRepeatingPayment(int tagID, int repeatingPaymentID)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("INSERT INTO tag_match (Tag_ID, Payment_ID, RepeatingPayment_ID) VALUES(?, ?, ?);");
			stmt.setInt(1, tagID);
			stmt.setInt(2, -1);
			stmt.setInt(3, repeatingPaymentID);
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
	
	public void deleteTagMatchForPayment(int tagID, int paymentID)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("DELETE FROM tag_match WHERE tag_match.Tag_ID = ? AND tag_match.Payment_ID = ?;");
			stmt.setInt(1, tagID);
			stmt.setInt(2, paymentID);
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
	
	public void deleteTagMatchForRepeatingPayment(int tagID, int repeatingPaymentID)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("DELETE FROM tag_match WHERE tag_match.Tag_ID = ? AND tag_match.repeatingPayment_ID = ?;");
			stmt.setInt(1, tagID);
			stmt.setInt(2, repeatingPaymentID);
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
}