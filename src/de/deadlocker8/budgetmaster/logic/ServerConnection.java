package de.deadlocker8.budgetmaster.logic;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tools.Read;

public class ServerConnection
{
	private Settings settings;
	private Gson gson;

	public ServerConnection(Settings settings) throws Exception
	{
		this.settings = settings;
		this.gson = new Gson();
		
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
		{
			public java.security.cert.X509Certificate[] getAcceptedIssuers()
			{
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType)
			{
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType)
			{
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> hostname.equals("localhost"));
	}
	
	public ArrayList<Category> getCategories() throws Exception
	{
		URL url = new URL(settings.getUrl() + "/category?secret=" + settings.getSecret());
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");		
		
		String result = Read.getStringFromInputStream(httpsCon.getInputStream());
		//required by GSON
		Type listType = new TypeToken<ArrayList<Category>>(){}.getType();		
		return gson.fromJson(result, listType);		
	}	
}