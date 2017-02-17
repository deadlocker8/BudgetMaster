package de.deadlocker8.budgetmaster.logic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tools.ConvertTo;
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
	
	/*
	 * Category	 
	 */
	public ArrayList<Category> getCategories() throws Exception
	{
		URL url = new URL(settings.getUrl() + "/category?secret=" + settings.getSecret());
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");

		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			// required by GSON
			Type listType = new TypeToken<ArrayList<Category>>()
			{
			}.getType();
			return gson.fromJson(result, listType);
		}
		else
		{
			return null;
		}
	}
	
	public void addCategory(Category category) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/category?secret=" + settings.getSecret() + "&name=" + category.getName() + "&color=" + ConvertTo.toRGBHexWithoutOpacity(category.getColor()).replace("#", ""));
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setRequestMethod("POST");
		httpsCon.setDoInput(true);
		InputStream stream = httpsCon.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		reader.close();
	}
	
	public void updateCategory(Category category) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/category?secret=" + settings.getSecret() + "&id=" + category.getID() + "&name=" + category.getName() + "&color=" + ConvertTo.toRGBHexWithoutOpacity(category.getColor()).replace("#", ""));
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setRequestMethod("PUT");
		httpsCon.setDoInput(true);
		InputStream stream = httpsCon.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		reader.close();
	}

	public void deleteCategory(int ID) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/category?secret=" + settings.getSecret() + "&id=" + ID);
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setRequestMethod("DELETE");
		httpsCon.setDoInput(true);
		InputStream stream = httpsCon.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		reader.close();
	}
	
	/*
	 * CategoryBudget
	 */
	public ArrayList<CategoryBudget> getCategoryBudgets(int year, int month) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/categorybudget?secret=" + settings.getSecret() + "&year=" + year + "&month=" + month);
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");

		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			// required by GSON
			Type listType = new TypeToken<ArrayList<CategoryBudget>>()
			{
			}.getType();
			return gson.fromJson(result, listType);
		}
		else
		{
			return null;
		}
	}
	
	/*
	 * Payment
	 */
	
	public void addPayment(Payment payment) throws Exception
	{
		//TODO add payment
//		URL url = new URL(settings.getUrl() + "/category?secret=" + settings.getSecret() + "&name=" + category.getName() + "&color=" + ConvertTo.toRGBHexWithoutOpacity(category.getColor()).replace("#", ""));
//		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
//		httpsCon.setRequestMethod("POST");
//		httpsCon.setDoInput(true);
//		InputStream stream = httpsCon.getInputStream();
//		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//		reader.close();
	}
	
	public void updatePayment(Payment payment, Payment oldPayment) throws Exception
	{
		//TODO update payment
		//TODO handle changing of repeating type
//		URL url = new URL(settings.getUrl() + "/category?secret=" + settings.getSecret() + "&id=" + category.getID() + "&name=" + category.getName() + "&color=" + ConvertTo.toRGBHexWithoutOpacity(category.getColor()).replace("#", ""));
//		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
//		httpsCon.setRequestMethod("PUT");
//		httpsCon.setDoInput(true);
//		InputStream stream = httpsCon.getInputStream();
//		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//		reader.close();
	}
}