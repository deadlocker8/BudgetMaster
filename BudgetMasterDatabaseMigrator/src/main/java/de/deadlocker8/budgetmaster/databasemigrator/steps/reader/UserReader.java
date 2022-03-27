package de.deadlocker8.budgetmaster.databasemigrator.steps.reader;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.hint.DestinationHint;
import de.deadlocker8.budgetmaster.databasemigrator.destination.user.DestinationUser;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserReader extends BaseReader<DestinationUser>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String NAME = "NAME";
		public static final String PASSWORD = "PASSWORD";
		public static final String SELECTED_ACCOUNT_ID = "SELECTED_ACCOUNT_ID";
	}

	public UserReader(DataSource primaryDataSource)
	{
		super(TableNames.USER_SOURCE, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationUser> getRowMapper()
	{
		return new HintRowMapper();
	}

	public static class HintRowMapper implements RowMapper<DestinationUser>
	{
		@Override
		public DestinationUser mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationUser user = new DestinationUser();
			user.setID(rs.getInt(DatabaseColumns.ID));
			user.setName(rs.getString(DatabaseColumns.NAME));
			user.setPassword(rs.getString(DatabaseColumns.PASSWORD));
			user.setSelectedAccountID(rs.getInt(DatabaseColumns.SELECTED_ACCOUNT_ID));
			return user;
		}
	}
}
