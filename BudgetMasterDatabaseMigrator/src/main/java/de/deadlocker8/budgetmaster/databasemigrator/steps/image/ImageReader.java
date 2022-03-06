package de.deadlocker8.budgetmaster.databasemigrator.steps.image;

import de.deadlocker8.budgetmaster.databasemigrator.source.image.SourceImage;
import de.deadlocker8.budgetmaster.databasemigrator.steps.BaseReader;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ImageReader extends BaseReader<SourceImage>
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
	protected RowMapper<SourceImage> getRowMapper()
	{
		return new ImageRowMapper();
	}

	public static class ImageRowMapper implements RowMapper<SourceImage>
	{
		@Override
		public SourceImage mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final SourceImage image = new SourceImage();
			image.setID(rs.getInt(DatabaseColumns.ID));
			image.setFileExtension(rs.getInt(DatabaseColumns.FILE_EXTENSION));
			image.setFileName(rs.getString(DatabaseColumns.FILE_NAME));
			image.setImage(rs.getBytes(DatabaseColumns.IMAGE));
			return image;
		}
	}
}
