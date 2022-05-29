package de.deadlocker8.budgetmaster.databasemigrator.steps.reader.tag;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.tag.DestinationTag;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.BaseReader;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TagReader extends BaseReader<DestinationTag>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String NAME = "NAME";
	}

	public TagReader(DataSource primaryDataSource)
	{
		super(TableNames.TAG, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationTag> getRowMapper()
	{
		return new TagRowMapper();
	}

	public static class TagRowMapper implements RowMapper<DestinationTag>
	{
		@Override
		public DestinationTag mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationTag tag = new DestinationTag();
			tag.setID(rs.getInt(DatabaseColumns.ID));
			tag.setName(rs.getString(DatabaseColumns.NAME));
			return tag;
		}
	}
}
