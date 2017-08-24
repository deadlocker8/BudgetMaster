package de.deadlocker8.budgetmasterserver.server.version;

import spark.Request;
import spark.Response;
import spark.Route;

public class VersionGet implements Route
{	
	private String versionCode;

	public VersionGet( String versionCode)
	{
		this.versionCode = versionCode;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception
	{
		return versionCode;
	}
}