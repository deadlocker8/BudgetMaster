package de.deadlocker8.budgetmaster.databasemigrator.steps.reader;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.templateGroup.DestinationTemplateGroup;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TemplateGroupReader extends BaseReader<DestinationTemplateGroup>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String NAME = "NAME";
		public static final String TYPE = "TYPE";
	}

	public TemplateGroupReader(DataSource primaryDataSource)
	{
		super(TableNames.TEMPLATE_GROUP, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationTemplateGroup> getRowMapper()
	{
		return new TemplateGroupRowMapper();
	}

	public static class TemplateGroupRowMapper implements RowMapper<DestinationTemplateGroup>
	{
		@Override
		public DestinationTemplateGroup mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationTemplateGroup templateGroup = new DestinationTemplateGroup();
			templateGroup.setID(rs.getInt(DatabaseColumns.ID));
			templateGroup.setName(rs.getString(DatabaseColumns.NAME));
			templateGroup.setType(rs.getInt(DatabaseColumns.TYPE));
			return templateGroup;
		}
	}
}
