package de.deadlocker8.budgetmasterserver.test;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Test
{
	public static void main(String[] args) throws Exception
	{
		URL url = new URL("http://localhost:8000");
		HttpURLConnection httpCon = (HttpURLConnection)url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("PUT");
		OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
		out.write("Resource content");
		out.close();
		httpCon.getInputStream();
	}
}