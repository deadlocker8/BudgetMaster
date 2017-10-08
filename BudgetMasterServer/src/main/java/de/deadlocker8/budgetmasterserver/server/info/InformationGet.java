package de.deadlocker8.budgetmasterserver.server.info;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.serverconnection.ServerInformation;
import de.deadlocker8.budgetmaster.logic.updater.VersionInformation;
import de.deadlocker8.budgetmasterserver.logic.Settings;
import spark.Request;
import spark.Response;
import spark.Route;

public class InformationGet implements Route
{	
	private Gson gson;
	private VersionInformation versionInfo;
	private Settings settings;

	public InformationGet(Gson gson, VersionInformation versionInfo, Settings settings)
	{
		this.gson = gson;
		this.versionInfo = versionInfo;
		this.settings = settings;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
	{
		ServerInformation serverInfo = new ServerInformation();
		serverInfo.setDatabaseUrl(settings.getDatabaseUrl());
		serverInfo.setDatabaseName(settings.getDatabaseName());
		serverInfo.setDatabaseUsername(settings.getDatabaseUsername());
		serverInfo.setServerPort(settings.getServerPort());
		serverInfo.setKeystorePath(settings.getKeystorePath());
		serverInfo.setVersionInfo(versionInfo);		
		
		return gson.toJson(serverInfo);
	}
}