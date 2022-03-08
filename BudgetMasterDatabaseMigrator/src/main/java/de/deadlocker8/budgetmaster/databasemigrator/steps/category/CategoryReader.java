package de.deadlocker8.budgetmaster.databasemigrator.steps.category;

import de.deadlocker8.budgetmaster.databasemigrator.destination.category.DestinationCategory;
import de.deadlocker8.budgetmaster.databasemigrator.steps.BaseReader;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryReader extends BaseReader<DestinationCategory>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String NAME = "NAME";
		public static final String COLOR = "COLOR";
		public static final String TYPE = "TYPE";
	}

	public CategoryReader(DataSource primaryDataSource)
	{
		super("category", primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationCategory> getRowMapper()
	{
		return new CategoryRowMapper();
	}

	public static class CategoryRowMapper implements RowMapper<DestinationCategory>
	{
		@Override
		public DestinationCategory mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationCategory category = new DestinationCategory();
			category.setID(rs.getInt(DatabaseColumns.ID));
			category.setName(rs.getString(DatabaseColumns.NAME));
			category.setColor(rs.getString(DatabaseColumns.COLOR));
			category.setType(rs.getInt(DatabaseColumns.TYPE));
			return category;
		}
	}
}
