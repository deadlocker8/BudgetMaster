package de.deadlocker8.budgetmaster.databasemigrator.steps.reader;

import de.deadlocker8.budgetmaster.databasemigrator.destination.image.DestinationImage;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageReader extends BaseReader<DestinationImage>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String FILE_EXTENSION = "FILE_EXTENSION";
		public static final String FILE_NAME = "FILE_NAME";
		public static final String IMAGE = "IMAGE";
	}

	public ImageReader(DataSource primaryDataSource)
	{
		super("image", primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationImage> getRowMapper()
	{
		return new ImageRowMapper();
	}

	public static class ImageRowMapper implements RowMapper<DestinationImage>
	{
		@Override
		public DestinationImage mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationImage image = new DestinationImage();
			image.setID(rs.getInt(DatabaseColumns.ID));
			image.setFileExtension(rs.getInt(DatabaseColumns.FILE_EXTENSION));
			image.setFileName(rs.getString(DatabaseColumns.FILE_NAME));
			image.setImage(rs.getBytes(DatabaseColumns.IMAGE));
			return image;
		}
	}
}
