package de.deadlocker8.budgetmaster.logic.serverconnection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import de.deadlocker8.budgetmaster.logic.Settings;
import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmaster.logic.category.CategoryBudget;
import de.deadlocker8.budgetmaster.logic.charts.CategoryInOutSum;
import de.deadlocker8.budgetmaster.logic.charts.MonthInOutSum;
import de.deadlocker8.budgetmaster.logic.database.Database;
import de.deadlocker8.budgetmaster.logic.payment.NormalPayment;
import de.deadlocker8.budgetmaster.logic.payment.Payment;
import de.deadlocker8.budgetmaster.logic.payment.PaymentJSONDeserializer;
import de.deadlocker8.budgetmaster.logic.payment.RepeatingPayment;
import de.deadlocker8.budgetmaster.logic.payment.RepeatingPaymentEntry;
import de.deadlocker8.budgetmaster.logic.updater.VersionInformation;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
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

		// check whitelist
		HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> settings.getTrustedHosts().contains(hostname));
	}

	/*
	 * Category
	 */
	public ArrayList<Category> getCategories() throws Exception
	{
		URL url = new URL(settings.getUrl() + "/category?secret=" + Helpers.getURLEncodedString(settings.getSecret()));
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
			return new ArrayList<>();
		}
	}

	public Category getCategory(int ID) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/category/single?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&id=" + ID);
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");

		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			return gson.fromJson(result, Category.class);
		}
		else
		{
			return null;
		}
	}

	public void addCategory(Category category) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/category?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&name=" + Helpers.getURLEncodedString(category.getName()) + "&color=" + category.getColor().replace("#", ""));
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

	public void updateCategory(Category category) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/category?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&id=" + category.getID() + "&name=" + Helpers.getURLEncodedString(category.getName()) + "&color=" + category.getColor().replace("#", ""));
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setRequestMethod("PUT");
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

	public void deleteCategory(int ID) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/category?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&id=" + ID);
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
	 * Payment
	 */
	public ArrayList<NormalPayment> getPayments(int year, int month) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/payment?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&year=" + year + "&month=" + month);
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");
			
		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			// required by GSON
			Type listType = new TypeToken<ArrayList<NormalPayment>>()
			{
			}.getType();
			return gson.fromJson(result, listType);
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}
	}

	public ArrayList<RepeatingPaymentEntry> getRepeatingPayments(int year, int month) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/repeatingpayment?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&year=" + year + "&month=" + month);
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");

		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			// required by GSON
			Type listType = new TypeToken<ArrayList<RepeatingPaymentEntry>>()
			{
			}.getType();
			return gson.fromJson(result, listType);
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}
	}

	public RepeatingPayment getRepeatingPayment(int ID) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/repeatingpayment/single?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&id=" + ID);
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");

		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			return gson.fromJson(result, RepeatingPayment.class);
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}
	}

	public Integer addNormalPayment(NormalPayment payment) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/payment?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&amount=" + payment.getAmount() + "&date=" + payment.getDate() + "&categoryID=" + payment.getCategoryID() + "&name=" + Helpers.getURLEncodedString(payment.getName())
				+ "&description=" + Helpers.getURLEncodedString(payment.getDescription()));
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setRequestMethod("POST");
		httpsCon.setDoInput(true);
		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			return gson.fromJson(result, Integer.class);
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}
	}

	public void updateNormalPayment(NormalPayment payment) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/payment?secret=" + settings.getSecret() + "&id=" + payment.getID() + "&amount=" + payment.getAmount() + "&date=" + payment.getDate() + "&categoryID=" + payment.getCategoryID() + "&name=" + Helpers.getURLEncodedString(payment.getName())
				+ "&description=" + Helpers.getURLEncodedString(payment.getDescription()));
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setRequestMethod("PUT");
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

	public Integer addRepeatingPayment(RepeatingPayment payment) throws Exception
	{
		String repeatEndDate = payment.getRepeatEndDate();
		if(repeatEndDate == null || repeatEndDate.equals(""))
		{
			// A is placeholder for empty repeatEndDate
			repeatEndDate = "A";
		}

		URL url = new URL(settings.getUrl() + "/repeatingpayment?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&amount=" + payment.getAmount() + "&date=" + payment.getDate() + "&categoryID=" + payment.getCategoryID() + "&name=" + Helpers.getURLEncodedString(payment.getName())
				+ "&repeatInterval=" + payment.getRepeatInterval() + "&repeatEndDate=" + repeatEndDate + "&repeatMonthDay=" + payment.getRepeatMonthDay() + "&description=" + Helpers.getURLEncodedString(payment.getDescription()));
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setRequestMethod("POST");
		httpsCon.setDoInput(true);
		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			return gson.fromJson(result, Integer.class);
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}
	}

	public void deleteNormalPayment(NormalPayment payment) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/payment?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&id=" + payment.getID());
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

	public void deleteRepeatingPayment(RepeatingPaymentEntry payment) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/repeatingpayment?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&id=" + payment.getRepeatingPaymentID());
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
	
	public ArrayList<Payment> getPaymentsForSearch(String query, boolean searchName, boolean searchDescription, boolean searchCategoryName, boolean searchTags, boolean searchAmount, int minAmount, int maxAmount) throws Exception
	{
		String urlString = settings.getUrl() + "/payment/search?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&query=" + Helpers.getURLEncodedString(query);
		if(searchName)
		{
			urlString += "&name=" + 1;
		}
		
		if(searchDescription)
		{
			urlString += "&description=" + 1;
		}
		
		if(searchCategoryName)
		{
			urlString += "&categoryName=" + 1;
		}
		
		if(searchTags)
		{
			urlString += "&tags=" + 1;
		}
		
		if(searchAmount)
		{
			urlString += "&minAmount=" + minAmount;
			urlString += "&maxAmount=" + maxAmount;
		}
		
		URL url = new URL(urlString);
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");
			
		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			JsonParser parser = new JsonParser();
			JsonElement resultJSON = parser.parse(result);
			
	        return PaymentJSONDeserializer.deserializePaymentList(resultJSON.getAsJsonObject().get("payments").getAsJsonArray());
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}
	}
	
	public int getMaxAmount() throws Exception
	{
		URL url = new URL(settings.getUrl() + "/payment/search/maxAmount?secret=" + Helpers.getURLEncodedString(settings.getSecret()));
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");
			
		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			return gson.fromJson(result, Integer.class);
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}
	}

	/*
	 * CATEGORYBUDGET
	 */
	public ArrayList<CategoryBudget> getCategoryBudgets(int year, int month) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/categorybudget?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&year=" + year + "&month=" + month);
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
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}
	}

	/*
	 * REST
	 */
	public int getRestForAllPreviousMonths(int year, int month) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/rest?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + "&year=" + year + "&month=" + month);
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");

		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			return gson.fromJson(result, Integer.class);
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}
	}

	/*
	 * DATABASE
	 */
	public void deleteDatabase() throws Exception
	{
		URL url = new URL(settings.getUrl() + "/database?secret=" + Helpers.getURLEncodedString(settings.getSecret()));
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

	public String exportDatabase() throws Exception
	{
		URL url = new URL(settings.getUrl() + "/database?secret=" + Helpers.getURLEncodedString(settings.getSecret()));
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");

		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			return Read.getStringFromInputStream(httpsCon.getInputStream());
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}
	}

	public void importDatabase(Database database) throws Exception
	{
		String databaseJSON = new Gson().toJson(database);
		
		URL url = new URL(settings.getUrl() + "/database?secret=" + Helpers.getURLEncodedString(settings.getSecret()));
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setRequestMethod("POST");
		httpsCon.setRequestProperty("Content-Type", "application/json");
		httpsCon.setRequestProperty("Accept", "application/json");		
		httpsCon.setDoInput(true);
		httpsCon.setDoOutput(true);
		PrintWriter writer = new PrintWriter(httpsCon.getOutputStream());
		writer.write(databaseJSON);
		writer.flush();
		writer.close();
		
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
	 * CHARTS
	 */
	public ArrayList<CategoryInOutSum> getCategoryInOutSumForMonth(DateTime startDate, DateTime endDate) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/charts/categoryInOutSum?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + 
				"&startDate=" + startDate.toString("yyyy-MM-dd") + 
				"&endDate=" + endDate.toString("yyyy-MM-dd"));
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");

		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			// required by GSON
			Type listType = new TypeToken<ArrayList<CategoryInOutSum>>()
			{
			}.getType();
			return gson.fromJson(result, listType);
		}
		else
		{
			return null;
		}
	}
	
	public ArrayList<MonthInOutSum> getMonthInOutSum(DateTime startDate, DateTime endDate) throws Exception
	{
		URL url = new URL(settings.getUrl() + "/charts/monthInOutSum?secret=" + Helpers.getURLEncodedString(settings.getSecret()) + 
				"&startDate=" + startDate.toString("yyyy-MM-dd") + 
				"&endDate=" + endDate.toString("yyyy-MM-dd"));	
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");

		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());
			// required by GSON
			Type listType = new TypeToken<ArrayList<MonthInOutSum>>()
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
	 * VERSION
	 */
	public VersionInformation getServerVersion() throws Exception
	{
		URL url = new URL(settings.getUrl() + "/version?secret=" + Helpers.getURLEncodedString(settings.getSecret()));
		HttpsURLConnection httpsCon = (HttpsURLConnection)url.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setRequestMethod("GET");

		if(httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			String result = Read.getStringFromInputStream(httpsCon.getInputStream());			
			return gson.fromJson(result, VersionInformation.class);
		}
		else
		{
			throw new ServerConnectionException(String.valueOf(httpsCon.getResponseCode()));
		}
	}
}