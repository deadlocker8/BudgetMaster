package de.deadlocker8.budgetmaster.databasemigrator;

public enum CommandLineOptions
{
	SOURCE_URL("spring.datasource.jdbc-url", "source h2 database JDBC url, e.g. jdbc:h2:/C:/Users/Admin/AppData/Roaming/Deadlocker/BudgetMaster/debug/budgetmaster"),
	DESTINATION_URL("spring.seconddatasource.jdbc-url", "destination database JDBC url (postgres ir mariadb), e.g. jdbc:postgresql://localhost:5432/budgetmaster"),
	DESTINATION_DRIVER_CLASS_NAME("spring.seconddatasource.driver-class-name", "destination database driver class name, e.g. org.postgresql.Driver or org.mariadb.jdbc.Driver"),
	DESTINATION_USER_NAME("spring.seconddatasource.username", "destination postresql user name, e.g. budgetmaster"),
	DESTINATION_PASSWORD("spring.seconddatasource.password", "destination postresql password, e.g. BudgetMaster");

	private final String name;
	private final String description;

	CommandLineOptions(String name, String description)
	{
		this.name = name;
		this.description = description;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}
}
