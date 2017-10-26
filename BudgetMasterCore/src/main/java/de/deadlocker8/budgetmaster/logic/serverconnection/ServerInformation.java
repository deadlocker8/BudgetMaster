package de.deadlocker8.budgetmaster.logic.serverconnection;

import de.deadlocker8.budgetmaster.logic.updater.VersionInformation;

public class ServerInformation
{
	private String databaseUrl;
	private String databaseName;
	private String databaseUsername;
	private int serverPort;
	private String keystorePath;
	private VersionInformation versionInfo;

	public ServerInformation()
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

	public int getServerPort()
	{
		return serverPort;
	}

	public void setServerPort(int serverPort)
	{
		this.serverPort = serverPort;
	}

	public String getKeystorePath()
	{
		return keystorePath;
	}

	public void setKeystorePath(String keystorePath)
	{
		this.keystorePath = keystorePath;
	}

	public VersionInformation getVersionInfo()
	{
		return versionInfo;
	}

	public void setVersionInfo(VersionInformation versionInfo)
	{
		this.versionInfo = versionInfo;
	}

	@Override
	public String toString()
	{
		return "ServerInfo [databaseUrl=" + databaseUrl + ", databaseName=" + databaseName + ", databaseUsername=" + databaseUsername + ", serverPort=" + serverPort + ", keystorePath=" + keystorePath + ", versionInfo=" + versionInfo + "]";
	}
}