package de.deadlocker8.budgetmaster.logic;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tools.Read;

public class ServerConnection
{
	private Settings settings;
	private Gson gson;

	public ServerConnection(Settings settings)
	{
		this.settings = settings;
		this.gson = new Gson();
	}
	
	public ArrayList<Category> getCategories() throws Exception
	{
		URL url = new URL(settings.getUrl() + "/category?secret=" + settings.getSecret());
		HttpURLConnection httpCon = (HttpURLConnection)url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("GET");		
		
		String result = Read.getStringFromInputStream(httpCon.getInputStream());
		//required by GSON
		Type listType = new TypeToken<ArrayList<Category>>(){}.getType();		
		return gson.fromJson(result, listType);		
	}	
}