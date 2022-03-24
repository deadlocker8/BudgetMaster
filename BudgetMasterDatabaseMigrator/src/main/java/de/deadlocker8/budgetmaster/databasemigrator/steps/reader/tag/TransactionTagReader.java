
package de.deadlocker8.budgetmaster.databasemigrator.steps.reader.tag;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.tag.DestinationTransactionTag;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.BaseReader;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionTagReader extends BaseReader<DestinationTransactionTag>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String TRANSACTION_ID = "TRANSACTION_ID";
		public static final String TAGS_ID = "TAGS_ID";
	}

	public TransactionTagReader(DataSource primaryDataSource)
	{
		super(TableNames.TRANSACTION_TAGS, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationTransactionTag> getRowMapper()
	{
		return new TagRowMapper();
	}

	public static class TagRowMapper implements RowMapper<DestinationTransactionTag>
	{
		@Override
		public DestinationTransactionTag mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationTransactionTag tag = new DestinationTransactionTag();
			tag.setID(rowNum);
			tag.setTransactionID(rs.getInt(DatabaseColumns.TRANSACTION_ID));
			tag.setTagsID(rs.getInt(DatabaseColumns.TAGS_ID));
			return tag;
		}
	}
}
