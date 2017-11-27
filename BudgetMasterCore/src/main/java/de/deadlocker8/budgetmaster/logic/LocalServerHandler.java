package de.deadlocker8.budgetmaster.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.serverconnection.ServerConnection;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import logger.Logger;
import tools.HashUtils;
import tools.Localization;
import tools.PathUtils;

public class LocalServerHandler
{
	private static final String BUILD_FOLDER = "https://github.com/deadlocker8/BudgetMaster/raw/{}/build/";
	
	public LocalServerHandler()
	{
		
	}
	
	public boolean isServerPresent()
	{
		File file = new File(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/localServer/BudgetMasterServer.jar");
		return file.exists();
	}
	
	public boolean isServerRunning()
	{
		try
		{
			Settings settings = new Settings();
			settings.setUrl("https://localhost:9000");
			settings.setSecret(HashUtils.hash("BudgetMaster", Helpers.SALT));
			ArrayList<String> trustedHosts = new ArrayList<>();
			trustedHosts.add("localhost");
			settings.setTrustedHosts(trustedHosts);		
			ServerConnection connection = new ServerConnection(settings);
			connection.getServerInfo();
			
			return true;
		}
		catch(Exception e)
		{
			Logger.debug(e);
			return false;
		}
	}
	
	public void createServerSettingsIfNotExists() throws FileNotFoundException
	{
		File settingsFile = new File(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/localServer/settings.json");
		if(settingsFile.exists())
		{
			return;
		}
		Logger.debug("Creating settings file for local server...");
		
		String databasePath = PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/localServer/BudgetMaster.db";
		String settings = "{\"databaseType\": \"sqlite\"," + 
							"\"databaseUrl\": \"" + databasePath.replace("\\", "/") + "\"," + 
							"\"databaseName\": \"budgetmaster\"," + 
							"\"databaseUsername\": \"root\"," + 
							"\"databasePassword\": \"\"," + 
							"\"serverPort\": 9000," + 
							"\"serverSecret\": \"BudgetMaster\"," + 
							"\"keystorePath\": \"default\"," + 
							"\"keystorePassword\": \"BudgetMaster\"}";
		PrintWriter writer = new PrintWriter(settingsFile);
		writer.println(settings);
		writer.close();
	}
	
	public void downloadServer(String versionName) throws Exception
	{
		Logger.debug("Downloading BudgetMasterServer from " + BUILD_FOLDER.replace("{}", "v" + versionName));
		PathUtils.checkFolder(new File(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/localServer"));
		
		//download into temp directory and file
		Path target = Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/localServer/BudgetMasterServer.jar");
		download(BUILD_FOLDER.replace("{}", "v" + versionName) + "BudgetMasterServer.jar", target);			
		Logger.debug("Successfully downloaded BudgetMasterServer " + versionName);
	}
	
	private void download(String url, Path target) throws IOException
	{
		URL website = new URL(url);
		InputStream in = website.openStream();
		Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
	}
	
	public Process startServer() throws IOException
	{
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/localServer/BudgetMasterServer.jar").toString()); 				
		return pb.start();		
	}
	
	public LocalServerStatus getServerStatus()
	{
		LocalServerHandler serverHandler = new LocalServerHandler();
		if(serverHandler.isServerPresent())
		{
			Logger.debug("Local server found");
			if(serverHandler.isServerRunning())
			{
				Logger.debug("Local server is running");
				return LocalServerStatus.ACTIVE;
			}
			else
			{
				Logger.debug("Local server is NOT running");
				return LocalServerStatus.INACTIVE;
			}
		}
		else
		{
			Logger.debug("Local server NOT found");
			return LocalServerStatus.MISSING;
		}		
	}
}