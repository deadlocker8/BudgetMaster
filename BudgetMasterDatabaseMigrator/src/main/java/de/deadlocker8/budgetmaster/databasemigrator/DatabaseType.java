package de.deadlocker8.budgetmaster.databasemigrator;

public enum DatabaseType
{
	POSTGRESQL("org.postgresql.Driver"),
	MARIADB( "org.mariadb.jdbc.Driver");

	private final String driverClassName;

	DatabaseType(String driverClassName)
	{
		this.driverClassName = driverClassName;
	}


	public String getDriverClassName()
	{
		return driverClassName;
	}

	public static DatabaseType fromDriverClassName(String driverClassName)
	{
		for(DatabaseType type : values())
		{
			if(type.getDriverClassName().equalsIgnoreCase(driverClassName))
			{
				return type;
			}
		}

		return null;
	}
}