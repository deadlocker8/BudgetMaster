package de.deadlocker8.budgetmasterserver.logic.database.taghandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.deadlocker8.budgetmasterserver.logic.Settings;
import logger.Logger;

public class SqliteDatabaseTagHandler extends DatabaseTagHandler
{
	public SqliteDatabaseTagHandler(Settings settings) throws IllegalStateException
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
	
}