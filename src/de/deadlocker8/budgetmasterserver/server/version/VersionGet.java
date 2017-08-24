package de.deadlocker8.budgetmasterserver.server.version;

import com.google.gson.Gson;

import de.deadlocker8.budgetmaster.logic.updater.VersionInformation;
import spark.Request;
import spark.Response;
import spark.Route;

public class VersionGet implements Route
{	
	private Gson gson;
	private VersionInformation versionInfo;

	public VersionGet(Gson gson, VersionInformation versionInfo)
	{
		this.gson=gson;
		this.versionInfo = versionInfo;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
	{
		return gson.toJson(versionInfo);
	}
}