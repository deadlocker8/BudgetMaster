package de.deadlocker8.budgetmaster.databasemigrator.steps.reader.report;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.report.DestinationReportSettings;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.BaseReader;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportSettingsReader extends BaseReader<DestinationReportSettings>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String DATE = "DATE";
		public static final String INCLUDE_BUDGET = "INCLUDE_BUDGET";
		public static final String INCLUDE_CATEGORY_BUDGETS = "INCLUDE_CATEGORY_BUDGETS";
		public static final String SPLIT_TABLES = "SPLIT_TABLES";
	}

	public ReportSettingsReader(DataSource primaryDataSource)
	{
		super(TableNames.REPORT_SETTINGS, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationReportSettings> getRowMapper()
	{
		return new ReportSettingsRowMapper();
	}

	public static class ReportSettingsRowMapper implements RowMapper<DestinationReportSettings>
	{
		@Override
		public DestinationReportSettings mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationReportSettings settings = new DestinationReportSettings();
			settings.setID(rs.getInt(DatabaseColumns.ID));
			settings.setDate(rs.getString(DatabaseColumns.DATE));
			settings.setIncludeBudget(rs.getBoolean(DatabaseColumns.INCLUDE_BUDGET));
			settings.setIncludeCategoryBudgets(rs.getBoolean(DatabaseColumns.INCLUDE_CATEGORY_BUDGETS));
			settings.setSplitTables(rs.getBoolean(DatabaseColumns.SPLIT_TABLES));
			return settings;
		}
	}
}
