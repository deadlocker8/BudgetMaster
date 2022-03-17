package de.deadlocker8.budgetmaster.databasemigrator.steps.reader;

import de.deadlocker8.budgetmaster.databasemigrator.destination.chart.DestinationChart;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChartReader extends BaseReader<DestinationChart>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String NAME = "NAME";
		public static final String SCRIPT = "SCRIPT";
		public static final String TYPE = "TYPE";
		public static final String VERSION = "VERSION";
		public static final String DISPLAY_TYPE = "DISPLAY_TYPE";
		public static final String GROUP_TYPE = "GROUP_TYPE";
		public static final String PREVIEW_IMAGE_FILE_NAME = "PREVIEW_IMAGE_FILE_NAME";
	}

	public ChartReader(DataSource primaryDataSource)
	{
		super("chart", primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationChart> getRowMapper()
	{
		return new ChartRowMapper();
	}

	public static class ChartRowMapper implements RowMapper<DestinationChart>
	{
		@Override
		public DestinationChart mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationChart chart = new DestinationChart();
			chart.setID(rs.getInt(DatabaseColumns.ID));
			chart.setName(rs.getString(DatabaseColumns.NAME));
			chart.setScript(rs.getString(DatabaseColumns.SCRIPT));
			chart.setType(rs.getInt(DatabaseColumns.TYPE));
			chart.setVersion(rs.getInt(DatabaseColumns.VERSION));
			chart.setDisplayType(rs.getInt(DatabaseColumns.DISPLAY_TYPE));
			chart.setGroupType(rs.getInt(DatabaseColumns.GROUP_TYPE));
			chart.setPreviewImageFileName(rs.getString(DatabaseColumns.PREVIEW_IMAGE_FILE_NAME));
			return chart;
		}
	}
}
