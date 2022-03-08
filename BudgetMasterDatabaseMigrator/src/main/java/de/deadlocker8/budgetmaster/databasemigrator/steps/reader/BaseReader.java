package de.deadlocker8.budgetmaster.databasemigrator.steps.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public abstract class BaseReader<T> extends JdbcCursorItemReader<T> implements ItemReader<T>
{
	private static final int FETCH_SIZE = 100;

	protected BaseReader(String tableName, @Autowired DataSource primaryDataSource)
	{
		setDataSource(primaryDataSource);
		setSql(MessageFormat.format("SELECT * FROM {0}", tableName));
		setFetchSize(FETCH_SIZE);
		setRowMapper(getRowMapper());
	}

	protected abstract RowMapper<T> getRowMapper();

	protected static Integer getIntOrNull(ResultSet resultSet, String columnName) throws SQLException
	{
		final Integer intFromDatabase = resultSet.getInt(columnName);
		if(resultSet.wasNull())
		{
			return null;
		}

		return intFromDatabase;
	}
}
