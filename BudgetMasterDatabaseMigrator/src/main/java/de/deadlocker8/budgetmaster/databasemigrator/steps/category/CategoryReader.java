package de.deadlocker8.budgetmaster.databasemigrator.steps.category;

import de.deadlocker8.budgetmaster.databasemigrator.source.category.SourceCategory;
import de.deadlocker8.budgetmaster.databasemigrator.steps.BaseReader;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CategoryReader extends BaseReader<SourceCategory>
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
	protected RowMapper<SourceCategory> getRowMapper()
	{
		return new CategoryRowMapper();
	}

	public static class CategoryRowMapper implements RowMapper<SourceCategory>
	{
		@Override
		public SourceCategory mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final SourceCategory category = new SourceCategory();
			category.setID(rs.getInt(DatabaseColumns.ID));
			category.setName(rs.getString(DatabaseColumns.NAME));
			category.setColor(rs.getString(DatabaseColumns.COLOR));
			category.setType(rs.getInt(DatabaseColumns.TYPE));
			return category;
		}
	}
}
