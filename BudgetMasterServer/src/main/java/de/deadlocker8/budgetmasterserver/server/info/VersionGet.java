package de.deadlocker8.budgetmasterserver.server.info;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.updater.VersionInformation;
import de.deadlocker8.budgetmasterserver.logic.AdvancedRoute;
import spark.Request;
import spark.Response;

public class VersionGet implements AdvancedRoute
{	
	private Gson gson;
	private VersionInformation versionInfo;

	public VersionGet(Gson gson, VersionInformation versionInfo)
	{
		this.gson = gson;
		this.versionInfo = versionInfo;
	}

	@Override
	public void before()
	{
	}

	@Override
	public Object handleRequest(Request req, Response res)
	{
		return gson.toJson(versionInfo);
	}

	@Override
	public void after()
	{
	}
}