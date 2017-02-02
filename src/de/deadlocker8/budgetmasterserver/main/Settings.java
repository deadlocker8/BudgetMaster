package de.deadlocker8.budgetmasterserver.main;

public class Settings
{
	private String databaseUrl = "jdbc:mysql://localhost:3306/";
	private String databaseName = "budgetmaster";
	private String databaseUsername = "root";
	private String databasePassword = "";
	private int serverPort;	
	private String serverSecret;
	
	public Settings()
	{
		
	}
	
	public String getDatabaseUrl()
	{
		return databaseUrl;
	}

	public String getDatabaseName()
	{
		return databaseName;
	}

	public String getDatabaseUsername()
	{
		return databaseUsername;
	}

	public String getDatabasePassword()
	{
		return databasePassword;
	}

	public int getServerPort()
	{
		return serverPort;
	}

	public String getServerSecret()
	{
		return serverSecret;
	}
}