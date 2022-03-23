package de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating.end;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.end.DestinationRepeatingEndAfterXTimes;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.BaseReader;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepeatingEndAfterXTimesReader extends BaseReader<DestinationRepeatingEndAfterXTimes>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String TIMES = "TIMES";
	}

	public RepeatingEndAfterXTimesReader(DataSource primaryDataSource)
	{
		super(TableNames.REPEATING_END_AFTER_X_TIMES, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationRepeatingEndAfterXTimes> getRowMapper()
	{
		return new DestinationRepeatingEndAfterXTimesRowMapper();
	}

	public static class DestinationRepeatingEndAfterXTimesRowMapper implements RowMapper<DestinationRepeatingEndAfterXTimes>
	{
		@Override
		public DestinationRepeatingEndAfterXTimes mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationRepeatingEndAfterXTimes repeatingEndAfterXTimes = new DestinationRepeatingEndAfterXTimes();
			repeatingEndAfterXTimes.setID(rs.getInt(DatabaseColumns.ID));
			repeatingEndAfterXTimes.setTimes(rs.getInt(DatabaseColumns.TIMES));
			return repeatingEndAfterXTimes;
		}
	}
}
