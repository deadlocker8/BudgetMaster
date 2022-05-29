package de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating.modifier;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.modifier.DestinationRepeatingModifierYears;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.BaseReader;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepeatingModifierYearsReader extends BaseReader<DestinationRepeatingModifierYears>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
	}

	public RepeatingModifierYearsReader(DataSource primaryDataSource)
	{
		super(TableNames.REPEATING_MODIFIER_YEARS, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationRepeatingModifierYears> getRowMapper()
	{
		return new DestinationRepeatingModifierRowMapper();
	}

	public static class DestinationRepeatingModifierRowMapper implements RowMapper<DestinationRepeatingModifierYears>
	{
		@Override
		public DestinationRepeatingModifierYears mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationRepeatingModifierYears repeatingModifier = new DestinationRepeatingModifierYears();
			repeatingModifier.setID(rs.getInt(DatabaseColumns.ID));
			return repeatingModifier;
		}
	}
}
