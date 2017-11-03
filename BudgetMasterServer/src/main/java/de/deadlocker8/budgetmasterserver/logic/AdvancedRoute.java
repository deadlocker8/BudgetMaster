package de.deadlocker8.budgetmasterserver.logic;

import spark.Request;
import spark.Response;
import spark.Route;

public interface AdvancedRoute extends Route
{
	void before();
	Object handleRequest(Request req, Response res);
	void after();
	default Object handle(Request request, Response response) throws Exception
	{
		before();
		Object value = handleRequest(request, response);
		after();
		return value;
	}
}