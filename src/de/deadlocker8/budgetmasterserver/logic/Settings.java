package de.deadlocker8.budgetmasterserver.logic;

public class Settings
{
	private String databaseUrl;
	private String databaseName;
	private String databaseUsername;
	private String databasePassword;
	private int serverPort;
	private String serverSecret;
	private String keystorePath;
	private String keystorePassword;

	public Settings()
	{

	}

	public String getDatabaseUrl()
	{
		return databaseUrl;
	}

	public void setDatabaseUrl(String databaseUrl)
	{
		this.databaseUrl = databaseUrl;
	}

	public String getDatabaseName()
	{
		return databaseName;
	}

	public void setDatabaseName(String databaseName)
	{
		this.databaseName = databaseName;
	}

	public String getDatabaseUsername()
	{
		return databaseUsername;
	}

	public void setDatabaseUsername(String databaseUsername)
	{
		this.databaseUsername = databaseUsername;
	}

	public String getDatabasePassword()
	{
		return databasePassword;
	}

	public void setDatabasePassword(String databasePassword)
	{
		this.databasePassword = databasePassword;
	}

	public int getServerPort()
	{
		return serverPort;
	}

	public void setServerPort(int serverPort)
	{
		this.serverPort = serverPort;
	}

	public String getServerSecret()
	{
		return serverSecret;
	}

	public void setServerSecret(String serverSecret)
	{
		this.serverSecret = serverSecret;
	}

	public String getKeystorePath()
	{
		return keystorePath;
	}

	public void setKeystorePath(String keystorePath)
	{
		this.keystorePath = keystorePath;
	}

	public String getKeystorePassword()
	{
		return keystorePassword;
	}

	public void setKeystorePassword(String keystorePassword)
	{
		this.keystorePassword = keystorePassword;
	}

	@Override
	public String toString()
	{
		return "Settings [databaseUrl=" + databaseUrl + ", databaseName=" + databaseName + ", databaseUsername=" + databaseUsername + ", databasePassword=" + databasePassword + ", serverPort=" + serverPort + ", serverSecret=" + serverSecret + ", keystorePath=" + keystorePath + ", keystorePassword="
				+ keystorePassword + "]";
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Settings other = (Settings)obj;
		if(databaseName == null)
		{
			if(other.databaseName != null)
				return false;
		}
		else if(!databaseName.equals(other.databaseName))
			return false;
		if(databasePassword == null)
		{
			if(other.databasePassword != null)
				return false;
		}
		else if(!databasePassword.equals(other.databasePassword))
			return false;
		if(databaseUrl == null)
		{
			if(other.databaseUrl != null)
				return false;
		}
		else if(!databaseUrl.equals(other.databaseUrl))
			return false;
		if(databaseUsername == null)
		{
			if(other.databaseUsername != null)
				return false;
		}
		else if(!databaseUsername.equals(other.databaseUsername))
			return false;
		if(keystorePassword == null)
		{
			if(other.keystorePassword != null)
				return false;
		}
		else if(!keystorePassword.equals(other.keystorePassword))
			return false;
		if(keystorePath == null)
		{
			if(other.keystorePath != null)
				return false;
		}
		else if(!keystorePath.equals(other.keystorePath))
			return false;
		if(serverPort != other.serverPort)
			return false;
		if(serverSecret == null)
		{
			if(other.serverSecret != null)
				return false;
		}
		else if(!serverSecret.equals(other.serverSecret))
			return false;
		return true;
	}
}