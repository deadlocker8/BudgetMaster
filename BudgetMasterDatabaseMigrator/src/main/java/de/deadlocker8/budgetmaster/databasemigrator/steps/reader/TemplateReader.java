package de.deadlocker8.budgetmaster.databasemigrator.steps.reader;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.template.DestinationTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TemplateReader extends BaseReader<DestinationTemplate>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String TEMPLATE_NAME = "TEMPLATE_NAME";
		public static final String AMOUNT = "AMOUNT";
		public static final String IS_EXPENDITURE = "IS_EXPENDITURE";
		public static final String ACCOUNT_ID = "ACCOUNT_ID";
		public static final String CATEGORY_ID = "CATEGORY_ID";
		public static final String NAME = "NAME";
		public static final String DESCRIPTION = "DESCRIPTION";
		public static final String ICON_REFERENCE_ID = "ICON_REFERENCE_ID";
		public static final String TRANSFER_ACCOUNT_ID = "TRANSFER_ACCOUNT_ID";
		public static final String TEMPLATE_GROUP_ID = "TEMPLATE_GROUP_ID";
	}

	public TemplateReader(DataSource primaryDataSource)
	{
		super(TableNames.TEMPLATE, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationTemplate> getRowMapper()
	{
		return new TemplateRowMapper();
	}

	public static class TemplateRowMapper implements RowMapper<DestinationTemplate>
	{
		@Override
		public DestinationTemplate mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationTemplate template = new DestinationTemplate();
			template.setID(rs.getInt(DatabaseColumns.ID));
			template.setTemplateName(rs.getString(DatabaseColumns.TEMPLATE_NAME));
			template.setAmount(getIntOrNull(rs, DatabaseColumns.AMOUNT));
			template.setIsExpenditure(rs.getBoolean(DatabaseColumns.IS_EXPENDITURE));
			template.setAccountID(getIntOrNull(rs, DatabaseColumns.ACCOUNT_ID));
			template.setCategoryID(getIntOrNull(rs, DatabaseColumns.CATEGORY_ID));
			template.setName(rs.getString(DatabaseColumns.NAME));
			template.setDescription(rs.getString(DatabaseColumns.DESCRIPTION));
			template.setTransferAccountID(getIntOrNull(rs, DatabaseColumns.TRANSFER_ACCOUNT_ID));
			template.setIconReferenceID(getIntOrNull(rs, DatabaseColumns.ICON_REFERENCE_ID));
			template.setTemplateGroupID(getIntOrNull(rs, DatabaseColumns.TEMPLATE_GROUP_ID));
			return template;
		}
	}
}
