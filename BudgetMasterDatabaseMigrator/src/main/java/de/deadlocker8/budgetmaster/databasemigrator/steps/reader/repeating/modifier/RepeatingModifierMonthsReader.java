package de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating.modifier;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.modifier.DestinationRepeatingModifierMonths;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.BaseReader;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepeatingModifierMonthsReader extends BaseReader<DestinationRepeatingModifierMonths>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
	}

	public RepeatingModifierMonthsReader(DataSource primaryDataSource)
	{
		super(TableNames.REPEATING_MODIFIER_MONTHS, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationRepeatingModifierMonths> getRowMapper()
	{
		return new DestinationRepeatingModifierRowMapper();
	}

	public static class DestinationRepeatingModifierRowMapper implements RowMapper<DestinationRepeatingModifierMonths>
	{
		@Override
		public DestinationRepeatingModifierMonths mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationRepeatingModifierMonths repeatingModifier = new DestinationRepeatingModifierMonths();
			repeatingModifier.setID(rs.getInt(DatabaseColumns.ID));
			return repeatingModifier;
		}
	}
}
