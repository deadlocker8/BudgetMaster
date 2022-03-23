package de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.DestinationRepeatingOption;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.BaseReader;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepeatingOptionReader extends BaseReader<DestinationRepeatingOption>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String START_DATE = "START_DATE";
		public static final String END_OPTION_ID = "END_OPTION_ID";
		public static final String MODIFIER_ID = "MODIFIER_ID";
	}

	public RepeatingOptionReader(DataSource primaryDataSource)
	{
		super(TableNames.REPEATING_OPTION, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationRepeatingOption> getRowMapper()
	{
		return new RepeatingOptionRowMapper();
	}

	public static class RepeatingOptionRowMapper implements RowMapper<DestinationRepeatingOption>
	{
		@Override
		public DestinationRepeatingOption mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationRepeatingOption repeatingOption = new DestinationRepeatingOption();
			repeatingOption.setID(rs.getInt(DatabaseColumns.ID));
			repeatingOption.setStartDate(rs.getString(DatabaseColumns.START_DATE));
			repeatingOption.setEndOptionID(rs.getInt(DatabaseColumns.END_OPTION_ID));
			repeatingOption.setModifierID(rs.getInt(DatabaseColumns.MODIFIER_ID));
			return repeatingOption;
		}
	}
}
