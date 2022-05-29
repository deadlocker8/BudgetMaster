package de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating.modifier;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.modifier.DestinationRepeatingModifier;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.BaseReader;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepeatingModifierReader extends BaseReader<DestinationRepeatingModifier>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String LOCALIZATION_KEY = "LOCALIZATION_KEY";
		public static final String TYPE = "TYPE";
		public static final String QUANTITY = "QUANTITY";
	}

	public RepeatingModifierReader(DataSource primaryDataSource)
	{
		super(TableNames.REPEATING_MODIFIER, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationRepeatingModifier> getRowMapper()
	{
		return new RepeatingModifierRowMapper();
	}

	public static class RepeatingModifierRowMapper implements RowMapper<DestinationRepeatingModifier>
	{
		@Override
		public DestinationRepeatingModifier mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationRepeatingModifier repeatingModifier = new DestinationRepeatingModifier();
			repeatingModifier.setID(rs.getInt(DatabaseColumns.ID));
			repeatingModifier.setLocalizationKey(rs.getString(DatabaseColumns.LOCALIZATION_KEY));
			repeatingModifier.setType(rs.getString(DatabaseColumns.TYPE));
			repeatingModifier.setQuantity(rs.getInt(DatabaseColumns.QUANTITY));
			return repeatingModifier;
		}
	}
}
