package de.deadlocker8.budgetmaster.databasemigrator.steps.reader;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.transaction.DestinationTransaction;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionReader extends BaseReader<DestinationTransaction>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String AMOUNT = "AMOUNT";
		public static final String IS_EXPENDITURE = "IS_EXPENDITURE";
		public static final String DATE = "DATE";
		public static final String ACCOUNT_ID = "ACCOUNT_ID";
		public static final String CATEGORY_ID = "CATEGORY_ID";
		public static final String NAME = "NAME";
		public static final String DESCRIPTION = "DESCRIPTION";
		public static final String REPEATING_OPTION_ID = "REPEATING_OPTION_ID";
		public static final String TRANSFER_ACCOUNT_ID = "TRANSFER_ACCOUNT_ID";
	}

	public TransactionReader(DataSource primaryDataSource)
	{
		super(TableNames.TRANSACTION, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationTransaction> getRowMapper()
	{
		return new TransactionRowMapper();
	}

	public static class TransactionRowMapper implements RowMapper<DestinationTransaction>
	{
		@Override
		public DestinationTransaction mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationTransaction transaction = new DestinationTransaction();
			transaction.setID(rs.getInt(DatabaseColumns.ID));
			transaction.setAmount(rs.getInt(DatabaseColumns.AMOUNT));
			transaction.setIsExpenditure(rs.getBoolean(DatabaseColumns.IS_EXPENDITURE));
			transaction.setDate(rs.getString(DatabaseColumns.DATE));
			transaction.setAccountID(rs.getInt(DatabaseColumns.ACCOUNT_ID));
			transaction.setCategoryID(rs.getInt(DatabaseColumns.CATEGORY_ID));
			transaction.setName(rs.getString(DatabaseColumns.NAME));
			transaction.setDescription(rs.getString(DatabaseColumns.DESCRIPTION));
			transaction.setRepeatingOptionID(getIntOrNull(rs, DatabaseColumns.REPEATING_OPTION_ID));
			transaction.setTransferAccountID(getIntOrNull(rs, DatabaseColumns.TRANSFER_ACCOUNT_ID));
			return transaction;
		}
	}
}
