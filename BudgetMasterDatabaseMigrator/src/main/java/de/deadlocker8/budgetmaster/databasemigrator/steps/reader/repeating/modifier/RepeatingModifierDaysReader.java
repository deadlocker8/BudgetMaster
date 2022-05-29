package de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating.modifier;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.modifier.DestinationRepeatingModifierDays;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.BaseReader;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepeatingModifierDaysReader extends BaseReader<DestinationRepeatingModifierDays>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
	}

	public RepeatingModifierDaysReader(DataSource primaryDataSource)
	{
		super(TableNames.REPEATING_MODIFIER_DAYS, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationRepeatingModifierDays> getRowMapper()
	{
		return new DestinationRepeatingModifierRowMapper();
	}

	public static class DestinationRepeatingModifierRowMapper implements RowMapper<DestinationRepeatingModifierDays>
	{
		@Override
		public DestinationRepeatingModifierDays mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationRepeatingModifierDays repeatingModifier = new DestinationRepeatingModifierDays();
			repeatingModifier.setID(rs.getInt(DatabaseColumns.ID));
			return repeatingModifier;
		}
	}
}
