package de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating.end;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.end.DestinationRepeatingEnd;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.BaseReader;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepeatingEndReader extends BaseReader<DestinationRepeatingEnd>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String LOCALIZATION_KEY = "LOCALIZATION_KEY";
		public static final String TYPE = "TYPE";
	}

	public RepeatingEndReader(DataSource primaryDataSource)
	{
		super(TableNames.REPEATING_END, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationRepeatingEnd> getRowMapper()
	{
		return new RepeatingEndRowMapper();
	}

	public static class RepeatingEndRowMapper implements RowMapper<DestinationRepeatingEnd>
	{
		@Override
		public DestinationRepeatingEnd mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationRepeatingEnd repeatingEnd = new DestinationRepeatingEnd();
			repeatingEnd.setID(rs.getInt(DatabaseColumns.ID));
			repeatingEnd.setLocalizationKey(rs.getString(DatabaseColumns.LOCALIZATION_KEY));
			repeatingEnd.setType(rs.getString(DatabaseColumns.TYPE));
			return repeatingEnd;
		}
	}
}
