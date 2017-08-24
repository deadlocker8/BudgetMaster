package de.deadlocker8.budgetmaster.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.deadlocker8.budgetmaster.logic.updater.VersionInformation;

public class Updater
{
	private VersionInformation latestVersion;
	private static final String LATEST_VERSION_INFO_URL = "https://raw.githubusercontent.com/deadlocker8/BudgetMaster/master/src/de/deadlocker8/budgetmaster/resources/languages/_de.properties";
	private static final String CHANGELOG_URL = "https://raw.githubusercontent.com/deadlocker8/BudgetMaster/master/src/de/deadlocker8/budgetmaster/resources/changelog.json";
	
	public Updater()
	{	
		latestVersion = new VersionInformation(0, "-", "-");
	}		

	private VersionInformation getLatestVersionInformationFromServer() throws IOException
	{
		URL webseite = new URL(LATEST_VERSION_INFO_URL);
		URLConnection connection = webseite.openConnection();

		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
        
        ArrayList<String> lines = new ArrayList<String>();        

        while ((line = br.readLine()) != null)
        {
        	lines.add(line);            		
        }
        
        VersionInformation versionInfo = new VersionInformation();
        
        for(String currentLine : lines)
        {
        	if(currentLine.contains("version.code"))
        	{
        		versionInfo.setVersionCode(Integer.parseInt(currentLine.substring(currentLine.indexOf("=") + 1, currentLine.length())));
        	}
        	
        	if(currentLine.contains("version.name"))
        	{
        		versionInfo.setVersionName(currentLine.substring(currentLine.indexOf("=") + 1, currentLine.length()));
        	}
        	
        	if(currentLine.contains("version.date"))
        	{
        		versionInfo.setDate(currentLine.substring(currentLine.indexOf("=") + 1, currentLine.length()));
        	}
        }
        
        if(!versionInfo.isComplete())
        	throw new IllegalArgumentException("VersionInformation not complete");        	
		
		return versionInfo;
	}	
	
	public boolean isUpdateAvailable(int currentVersionCode) throws IOException
	{
		latestVersion = getLatestVersionInformationFromServer();
		return currentVersionCode < latestVersion.getVersionCode();	
	}	
	
	public VersionInformation getLatestVersion()
	{
		return latestVersion;
	}

	public JsonObject getChangelogFromURL() throws IOException
	{
		URL webseite = new URL(CHANGELOG_URL);
		URLConnection connection = webseite.openConnection();

		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;        
        StringBuilder data = new StringBuilder();
        while ((line = br.readLine()) != null)
        {
        	data.append(line);            		
        }
        
        JsonParser parser = new JsonParser();
        return parser.parse(data.toString()).getAsJsonObject();
	}
	
	public String getChangelog(int versionCode) throws IOException
	{		
		JsonObject changelogJSON = getChangelogFromURL();
		
		if(changelogJSON != null)
		{
			return changelogJSON.get(String.valueOf(versionCode)).getAsString();
		}
		return null;
	}
}