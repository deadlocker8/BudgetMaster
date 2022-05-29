package de.deadlocker8.budgetmaster.databasemigrator.steps.reader;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.hint.DestinationHint;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HintReader extends BaseReader<DestinationHint>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String LOCALIZATION_KEY = "LOCALIZATION_KEY";
		public static final String IS_DISMISSED = "IS_DISMISSED";
	}

	public HintReader(DataSource primaryDataSource)
	{
		super(TableNames.HINT, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationHint> getRowMapper()
	{
		return new HintRowMapper();
	}

	public static class HintRowMapper implements RowMapper<DestinationHint>
	{
		@Override
		public DestinationHint mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationHint hint = new DestinationHint();
			hint.setID(rs.getInt(DatabaseColumns.ID));
			hint.setLocalizationKey(rs.getString(DatabaseColumns.LOCALIZATION_KEY));
			hint.setDismissed(rs.getBoolean(DatabaseColumns.IS_DISMISSED));
			return hint;
		}
	}
}
