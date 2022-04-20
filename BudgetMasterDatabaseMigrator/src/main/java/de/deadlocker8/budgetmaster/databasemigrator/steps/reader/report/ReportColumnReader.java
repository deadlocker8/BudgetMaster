package de.deadlocker8.budgetmaster.databasemigrator.steps.reader.report;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.report.DestinationReportColumn;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.BaseReader;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportColumnReader extends BaseReader<DestinationReportColumn>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String ACTIVATED = "ACTIVATED";
		public static final String KEY = "KEY";
		public static final String POSITION = "POSITION";
	}

	public ReportColumnReader(DataSource primaryDataSource)
	{
		super(TableNames.REPORT_COLUMN, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationReportColumn> getRowMapper()
	{
		return new ReportColumnRowMapper();
	}

	public static class ReportColumnRowMapper implements RowMapper<DestinationReportColumn>
	{
		@Override
		public DestinationReportColumn mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationReportColumn column = new DestinationReportColumn();
			column.setID(rs.getInt(DatabaseColumns.ID));
			column.setActivated(rs.getBoolean(DatabaseColumns.ACTIVATED));
			column.setLocalizationKey(rs.getString(DatabaseColumns.KEY));
			column.setColumnPosition(rs.getInt(DatabaseColumns.POSITION));
			return column;
		}
	}
}
