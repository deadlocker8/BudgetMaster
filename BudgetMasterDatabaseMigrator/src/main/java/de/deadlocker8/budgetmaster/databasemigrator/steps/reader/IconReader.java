package de.deadlocker8.budgetmaster.databasemigrator.steps.reader;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.icon.DestinationIcon;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IconReader extends BaseReader<DestinationIcon>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String BUILTIN_IDENTIFIER = "BUILTIN_IDENTIFIER";
		public static final String IMAGE_ID = "IMAGE_ID";
		public static final String FONT_COLOR = "FONT_COLOR";
	}

	public IconReader(DataSource primaryDataSource)
	{
		super(TableNames.ICON, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationIcon> getRowMapper()
	{
		return new IconRowMapper();
	}

	public static class IconRowMapper implements RowMapper<DestinationIcon>
	{
		@Override
		public DestinationIcon mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationIcon icon = new DestinationIcon();
			icon.setID(rs.getInt(DatabaseColumns.ID));
			icon.setBuiltinIdentifier(rs.getString(DatabaseColumns.BUILTIN_IDENTIFIER));
			icon.setImageID(getIntOrNull(rs, DatabaseColumns.IMAGE_ID));
			icon.setFontColor(rs.getString(DatabaseColumns.FONT_COLOR));
			return icon;
		}
	}
}
