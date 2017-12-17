package de.deadlocker8.budgetmaster.logic;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.utils.LanguageType;
import de.deadlocker8.budgetmaster.logic.utils.SaveFileType;

public class Settings
{
	/*
	 * VERSIONS 
	 * 
	 * --> 1
	 * initial
	 * 
	 * --> 2
	 * added field for "serverType"
	 * 
	 */	
	
	private final String TYPE = SaveFileType.BUDGETMASTER_SETTINGS.toString();
	private final int VERSION = 2;
	private ServerType serverType;
	private String clientSecret;
	private String url;
	private String secret;
	private String currency;
	private boolean restActivated = true;
	private ArrayList<String> trustedHosts;
	private LanguageType language = LanguageType.ENGLISH;
	private boolean autoUpdateCheckEnabled = true;
	
	public Settings()
	{
		
	}

	public ServerType getServerType()
	{
		return serverType;
	}

	public void setServerType(ServerType serverType)
	{
		this.serverType = serverType;
	}

	public String getClientSecret()
	{
		return clientSecret;
	}

	public void setClientSecret(String clientPassword)
	{
		this.clientSecret = clientPassword;
	}
	
	public String getUrl()
	{
		return url;
	}
	
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public String getSecret()
	{
		return secret;
	}

	public void setSecret(String secret)
	{
		this.secret = secret;
	}

	public String getCurrency()
	{
		return currency;
	}

	public void setCurrency(String currency)
	{
		this.currency = currency;
	}

	public boolean isRestActivated()
	{
		return restActivated;
	}

	public void setRestActivated(boolean restActivated)
	{
		this.restActivated = restActivated;
	}
	
	public ArrayList<String> getTrustedHosts()
	{
		return trustedHosts;
	}

	public void setTrustedHosts(ArrayList<String> trustedHosts)
	{
		this.trustedHosts = trustedHosts;
	}	
	
	public LanguageType getLanguage()
	{
		return language;
	}

	public void setLanguage(LanguageType language)
	{
		this.language = language;
	}	

	public boolean isAutoUpdateCheckEnabled()
	{
		return autoUpdateCheckEnabled;
	}
	
	public void setAutoUpdateCheckEnabled(boolean autoUpdateCheckEnabled)
	{
		this.autoUpdateCheckEnabled = autoUpdateCheckEnabled;
	}

	public boolean isComplete()
	{
		if(url == null)
			return false;
		if(secret == null)
			return false;
		if(currency == null)
			return false;
		
		return true;
	}

	public int getVERSION()
	{
		return VERSION;
	}

	@Override
	public String toString()
	{
		return "Settings [TYPE=" + TYPE + ", VERSION=" + VERSION + ", serverType=" + serverType + ", clientSecret=" + clientSecret + ", url=" + url + ", secret=" + secret + ", currency=" + currency + ", restActivated=" + restActivated + ", trustedHosts=" + trustedHosts + ", language=" + language
				+ ", autoUpdateCheckEnabled=" + autoUpdateCheckEnabled + "]";
	}
}