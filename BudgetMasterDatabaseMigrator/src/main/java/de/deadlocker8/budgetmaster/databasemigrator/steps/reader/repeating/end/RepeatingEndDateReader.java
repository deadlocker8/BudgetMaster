package de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating.end;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.end.DestinationRepeatingEndDate;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.BaseReader;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepeatingEndDateReader extends BaseReader<DestinationRepeatingEndDate>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String END_DATE = "END_DATE";
	}

	public RepeatingEndDateReader(DataSource primaryDataSource)
	{
		super(TableNames.REPEATING_END_DATE, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationRepeatingEndDate> getRowMapper()
	{
		return new DestinationRepeatingEndDateRowMapper();
	}

	public static class DestinationRepeatingEndDateRowMapper implements RowMapper<DestinationRepeatingEndDate>
	{
		@Override
		public DestinationRepeatingEndDate mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationRepeatingEndDate repeatingEndDate = new DestinationRepeatingEndDate();
			repeatingEndDate.setID(rs.getInt(DatabaseColumns.ID));
			repeatingEndDate.setEndDate(rs.getString(DatabaseColumns.END_DATE));
			return repeatingEndDate;
		}
	}
}
