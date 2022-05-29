package de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating.end;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.end.DestinationRepeatingEndNever;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.BaseReader;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepeatingEndNeverReader extends BaseReader<DestinationRepeatingEndNever>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
	}

	public RepeatingEndNeverReader(DataSource primaryDataSource)
	{
		super(TableNames.REPEATING_END_NEVER, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationRepeatingEndNever> getRowMapper()
	{
		return new DestinationRepeatingEndNeverRowMapper();
	}

	public static class DestinationRepeatingEndNeverRowMapper implements RowMapper<DestinationRepeatingEndNever>
	{
		@Override
		public DestinationRepeatingEndNever mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationRepeatingEndNever repeatingEndNever = new DestinationRepeatingEndNever();
			repeatingEndNever.setID(rs.getInt(DatabaseColumns.ID));
			return repeatingEndNever;
		}
	}
}
