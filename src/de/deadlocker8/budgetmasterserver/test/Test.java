package de.deadlocker8.budgetmasterserver.test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import tools.Read;

public class Test
{
	public static void main(String[] args) throws Exception
	{
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
		
		URL url = new URL("https://localhost:9000/category?secret=geheim");
		HttpsURLConnection httpCon = (HttpsURLConnection)url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("GET");		
		
		String result = Read.getStringFromInputStream(httpCon.getInputStream());
		System.out.println(result);	
	}
}