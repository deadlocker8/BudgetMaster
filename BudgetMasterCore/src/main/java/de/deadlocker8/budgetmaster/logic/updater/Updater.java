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
import java.util.Properties;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.deadlocker8.budgetmaster.logic.utils.Strings;
import logger.Logger;
import nativeWindows.NativeLauncher;
import tools.Localization;
import tools.OS;
import tools.OS.OSType;
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

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        
        VersionInformation versionInfo = new VersionInformation();
        Properties properties = new Properties();
        properties.load(bufferedReader);           
        versionInfo.setVersionCode(Integer.parseInt(properties.getProperty("version.code", "-1")));
        versionInfo.setVersionName(properties.getProperty("version.name"));
        versionInfo.setDate(properties.getProperty("version.date"));
        
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
	
	public String getChangelog(int versionCode) throws Exception
	{		
		JsonObject changelogJSON = getChangelogFromURL();
		
		if(changelogJSON != null)
		{
			return changelogJSON.get(String.valueOf(versionCode)).getAsString();			
		}
		return null;
	}
	
	private void downloadLatestUpdater(OSType osType) throws IOException
	{
		//download into temp directory and file				
		String ending = "jar";
		if(osType == OSType.Windows)
		{
			ending = "exe";
		}		
		
		Path target = Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/Updater." + ending);			
		download(BUILD_FOLDER + "Updater." + ending, target);
		Logger.debug("Successfully downloaded latest updater");
	}
	
	private File getCurrentExecutableName()
	{
		return new File(Updater.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	}
	
	
	public void downloadLatestVersion() throws Exception
	{
		File currentExecutable = getCurrentExecutableName();
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
		
		downloadLatestUpdater(OS.getType());		
		
		//download into temp directory and file
		Path target;
		if(fileEnding.equalsIgnoreCase("exe"))
		{			
			target = Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/update_BudgetMaster.exe");			
			download(BUILD_FOLDER + "BudgetMaster.exe", target);			
			Logger.debug("Successfully downloaded latest EXE");
		}
		else
		{
			target = Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/update_BudgetMasterClient.jar");			
			download(BUILD_FOLDER + "BudgetMasterClient.jar", target);			
			Logger.debug("Successfully downloaded latest JAR");
		}
		
		String params = target.toString().replace(" ", "%20") + " " + currentExecutable.getAbsolutePath().replace(" ", "%20") + " "  + Localization.getString(Strings.APP_NAME);
		Logger.debug(params);		
	
		if(OS.getType() == OSType.Windows)
		{
			NativeLauncher.executeAsAdministrator(Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/Updater.exe").toString(), params);
		}
		else
		{
			ProcessBuilder pb = new ProcessBuilder("java", "-jar", Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/Updater.jar").toString(), target.toString().replace(" ", "%20"), currentExecutable.toString().replace(" ", "%20"), Localization.getString(Strings.APP_NAME)); 				
			pb.start();	
		}
		System.exit(0);
	}
	
	public void download(String url, Path target) throws IOException
	{
		URL website = new URL(url);
		InputStream in = website.openStream();
		Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
	}
}