package de.deadlocker8.budgetmaster.databasemigrator.steps.reader;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.account.DestinationAccount;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountReader extends BaseReader<DestinationAccount>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String NAME = "NAME";
		public static final String IS_SELECTED = "IS_SELECTED";
		public static final String IS_DEFAULT = "IS_DEFAULT";
		public static final String ACCOUNT_STATE = "ACCOUNT_STATE";
		public static final String ICON_REFERENCE_ID = "ICON_REFERENCE_ID";
		public static final String TYPE = "TYPE";
	}

	public AccountReader(DataSource primaryDataSource)
	{
		super(TableNames.ACCOUNT, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationAccount> getRowMapper()
	{
		return new AccountRowMapper();
	}

	public static class AccountRowMapper implements RowMapper<DestinationAccount>
	{
		@Override
		public DestinationAccount mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationAccount account = new DestinationAccount();
			account.setID(rs.getInt(DatabaseColumns.ID));
			account.setDefault(rs.getBoolean(DatabaseColumns.IS_DEFAULT));
			account.setSelected(rs.getBoolean(DatabaseColumns.IS_SELECTED));
			account.setName(rs.getString(DatabaseColumns.NAME));
			account.setAccountState(rs.getInt(DatabaseColumns.ACCOUNT_STATE));
			account.setType(rs.getInt(DatabaseColumns.TYPE));
			account.setIconReferenceID(getIntOrNull(rs, DatabaseColumns.ICON_REFERENCE_ID));
			return account;
		}
	}
}
