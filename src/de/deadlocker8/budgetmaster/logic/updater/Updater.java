package de.deadlocker8.budgetmaster.logic.updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.deadlocker8.budgetmaster.logic.utils.Strings;
import logger.Logger;
import tools.Localization;
import tools.PathUtils;

public class Updater
{
	private VersionInformation latestVersion;
	private static final String LATEST_VERSION_INFO_URL = "https://raw.githubusercontent.com/deadlocker8/BudgetMaster/master/src/de/deadlocker8/budgetmaster/resources/languages/_de.properties";
	private static final String CHANGELOG_URL = "https://raw.githubusercontent.com/deadlocker8/BudgetMaster/master/src/de/deadlocker8/budgetmaster/resources/changelog.json";
	private static final String BUILD_FOLDER = "https://github.com/deadlocker8/BudgetMaster/raw/master/build/";
	
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
	
	private void downloadLatestUpdater() throws IOException
	{
		//download into temp directory and file					
		Path target = Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/Updater.jar");			
		download(BUILD_FOLDER + "Updater.jar", target);		
	}
	
	private File getCurrentExecutableName()
	{
		return new File(Updater.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	}
	
	
	public void downloadLatestVersion() throws IOException
	{
		File currentExecutable = getCurrentExecutableName();
		File currentFolder = currentExecutable.getParentFile();
		String currentFileName = currentExecutable.getName();
		String fileEnding;
		
		//check if BudgetMaster is running from executable
		//no updating procedure if running from source
		if(currentFileName.contains("."))
		{
			fileEnding = currentExecutable.getAbsolutePath().substring(currentExecutable.getAbsolutePath().indexOf("."), currentExecutable.getAbsolutePath().length());			
		}
		else
		{
			Logger.debug("Update procedure will be skipped because BudgetMaster is running from source");
			return;
		}
		
		PathUtils.checkFolder(new File(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER)));
		
		//download latest updater.jar
		downloadLatestUpdater();
		
		//download into temp directory and file
		if(fileEnding.equalsIgnoreCase("exe"))
		{			
			Path target = Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/update_BudgetMaster.exe");			
			download(BUILD_FOLDER + "BudgetMaster.exe", target);
		}
		else
		{
			Path target = Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/update_BudgetMasterClient.jar");			
			download(BUILD_FOLDER + "BudgetMasterClient.jar", target);
		}
		
		//TODO start upater with params
		//--> move temp jar/exe to currentFolder with currentFileName
	}
	
	public void download(String url, Path target) throws IOException
	{
		URL website = new URL(url);
		InputStream in = website.openStream();
		Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
	}
}