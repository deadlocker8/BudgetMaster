package de.deadlocker8.budgetmaster.databasemigrator.steps.reader.tag;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.tag.DestinationTemplateTag;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.BaseReader;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TemplateTagReader extends BaseReader<DestinationTemplateTag>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String TEMPLATE_ID = "TEMPLATE_ID";
		public static final String TAGS_ID = "TAGS_ID";
	}

	public TemplateTagReader(DataSource primaryDataSource)
	{
		super(TableNames.TEMPLATE_TAGS, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationTemplateTag> getRowMapper()
	{
		return new TagRowMapper();
	}

	public static class TagRowMapper implements RowMapper<DestinationTemplateTag>
	{
		@Override
		public DestinationTemplateTag mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationTemplateTag tag = new DestinationTemplateTag();
			tag.setID(rowNum);
			tag.setTemplateID(rs.getInt(DatabaseColumns.TEMPLATE_ID));
			tag.setTagsID(rs.getInt(DatabaseColumns.TAGS_ID));
			return tag;
		}
	}
}
