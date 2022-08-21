package de.deadlocker8.budgetmaster.database;

public enum DatabaseType
{
	POSTGRESQL("PostgreSQL", "org.postgresql.Driver"),
	MARIADB("MariaDB", "org.mariadb.jdbc.Driver");

	private final String name;
	private final String driverClassName;

	DatabaseType(String name, String driverClassName)
	{
		this.name = name;
		this.driverClassName = driverClassName;
	}

	public String getName()
	{
		return name;
	}

	public String getDriverClassName()
	{
		return driverClassName;
	}

	public static DatabaseType fromName(String name)
	{
		for(DatabaseType type : values())
		{
			if(type.getName().equalsIgnoreCase(name))
			{
				return type;
			}
		}

		return null;
	}
}