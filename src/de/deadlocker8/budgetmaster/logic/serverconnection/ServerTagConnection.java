package de.deadlocker8.budgetmaster.logic.serverconnection;

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

import de.deadlocker8.budgetmaster.logic.Settings;
import de.deadlocker8.budgetmaster.logic.payment.NormalPayment;
import de.deadlocker8.budgetmaster.logic.payment.RepeatingPayment;
import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import tools.Read;

public class ServerTagConnection
{
	private Settings settings;
	private Gson gson;

	public ServerTagConnection(Settings settings) throws Exception
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

		// check whitelist
		HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> settings.getTrustedHosts().contains(hostname));
	}
	
	public ArrayList<Tag> getTags() throws Exception
	{
		URL url = new URL(settings.getUrl() + "/tag?secret=" + Helpers.getURLEncodedString(settings.getSecret()));
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");

		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			// required by GSON
			Type listType = new TypeToken<ArrayList<Tag>>()
			{
			}.getType();
			return gson.fromJson(result, listType);
		}
		else
		{
			return new ArrayList<>();
		}
	}

	public Tag getTag(int ID) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/tag/single?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&id=" + ID);
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");

		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			return gson.fromJson(result, Tag.class);
		}
		else
		{
			return null;
		}
	}

	public void addTag(Tag tag) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/tag?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&name=" + Helpers.getURLEncodedString(tag.getName()));
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setRequestMethod("POST");
		httpsCon.setDoInput(true);
		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			InputStream stream = httpsCon.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			reader.close();
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}		
	}

	public void deleteTag(int ID) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/tag?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&id=" + ID);
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setRequestMethod("DELETE");
		httpsCon.setDoInput(true);
		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			InputStream stream = httpsCon.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			reader.close();
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}
	}
	
	/*
	 * tag match
	 */
	public boolean isMatchExistingForPayment(int tagID, NormalPayment payment) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/tag/match/normal?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&tagID=" + tagID + "&paymentID=" + payment.getID());
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");

		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			return gson.fromJson(result, Boolean.class);
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}
	}
	
	public boolean isMatchExistingForRepeatingPayment(int tagID, RepeatingPayment payment) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/tag/match/repeating?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&tagID=" + tagID + "&repeatingPaymentID=" + payment.getID());
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");

		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			return gson.fromJson(result, Boolean.class);
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}
	}
	
	public void addTagMatchForPayment(int tagID, NormalPayment payment) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/tag/match/normal?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&tagID=" + tagID + "&paymentID=" + payment.getID());
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setRequestMethod("POST");
		httpsCon.setDoInput(true);
		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			InputStream stream = httpsCon.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			reader.close();
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}		
	}
	
	public void addTagMatchForRepeatingPayment(int tagID, RepeatingPayment payment) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/tag/match/repeating?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&tagID=" + tagID + "&repeatingPaymentID=" + payment.getID());
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setRequestMethod("POST");
		httpsCon.setDoInput(true);
		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			InputStream stream = httpsCon.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			reader.close();
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}		
	}
	
	public void deleteTagMatchForPayment(int tagID, NormalPayment payment) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/tag/match/normal?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&tagID=" + tagID + "&paymentID=" + payment.getID());
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setRequestMethod("DELETE");
		httpsCon.setDoInput(true);
		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			InputStream stream = httpsCon.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			reader.close();
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}
	}
	
	public void deleteTagMatchForRepeatingPayment(int tagID, RepeatingPayment payment) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/tag/match/repeating?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&tagID=" + tagID + "&repeatingPaymentID=" + payment.getID());
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setRequestMethod("DELETE");
		httpsCon.setDoInput(true);
		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			InputStream stream = httpsCon.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			reader.close();
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}
	}
	
	public ArrayList<Tag> getAllTagsForPayment(NormalPayment payment) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/tag/match/all/normal?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&paymentID=" + payment.getID());
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");

		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			// required by GSON
			Type listType = new TypeToken<ArrayList<Tag>>()
			{
			}.getType();
			return gson.fromJson(result, listType);
		}
		else
		{
			return new ArrayList<>();
		}
	}
	
	public ArrayList<Tag> getAllTagsForRepeatingPayment(RepeatingPayment repeatingPayment) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/tag/match/all/normal?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&repeatingPaymentID=" + repeatingPayment.getID());
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");

		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			// required by GSON
			Type listType = new TypeToken<ArrayList<Tag>>()
			{
			}.getType();
			return gson.fromJson(result, listType);
		}
		else
		{
			return new ArrayList<>();
		}
	}
}