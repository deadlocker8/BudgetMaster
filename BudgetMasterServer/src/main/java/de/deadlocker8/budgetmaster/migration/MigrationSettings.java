package de.deadlocker8.budgetmaster.migration;

import java.util.Objects;

public final class MigrationSettings
{
	private final String hostname;
	private final Integer port;
	private final String databaseName;
	private final String username;
	private final String password;
	private DatabaseType databaseType;

	public MigrationSettings(String hostname, Integer port, String databaseName, String username, String password, DatabaseType databaseType)
	{
		this.hostname = hostname;
		this.port = port;
		this.databaseName = databaseName;
		this.username = username;
		this.password = password;
		this.databaseType = databaseType;
	}

	public String hostname()
	{
		return hostname;
	}

	public Integer port()
	{
		return port;
	}

	public String databaseName()
	{
		return databaseName;
	}

	public String username()
	{
		return username;
	}

	public String password()
	{
		return password;
	}

	public DatabaseType databaseType()
	{
		return databaseType;
	}

	public void setDatabaseType(DatabaseType databaseType)
	{
		this.databaseType = databaseType;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj == this) return true;
		if(obj == null || obj.getClass() != this.getClass()) return false;
		var that = (MigrationSettings) obj;
		return Objects.equals(this.hostname, that.hostname) &&
				Objects.equals(this.port, that.port) &&
				Objects.equals(this.databaseName, that.databaseName) &&
				Objects.equals(this.username, that.username) &&
				Objects.equals(this.password, that.password) &&
				Objects.equals(this.databaseType, that.databaseType);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(hostname, port, databaseName, username, password, databaseType);
	}

	@Override
	public String toString()
	{
		return "MigrationSettings[" +
				"hostname=" + hostname + ", " +
				"port=" + port + ", " +
				"databaseName=" + databaseName + ", " +
				"username=" + username + ", " +
				"password=" + password + ", " +
				"databaseType=" + databaseType + ']';
	}

}
