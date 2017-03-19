package de.deadlocker8.budgetmasterserver.main;

public class Settings
{
	private String databaseUrl;
	private String databaseName;
	private String databaseUsername;
	private String databasePassword;
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