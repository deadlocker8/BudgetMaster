package de.deadlocker8.budgetmaster.logic;

import java.util.ArrayList;

public class Settings
{
	private String clientSecret;
	private String url;
	private String secret;
	private String currency;
	private boolean restActivated;
	private ArrayList<String> trustedHosts;
	
	public Settings()
	{
		
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

	@Override
	public String toString()
	{
		return "Settings [clientSecret=" + clientSecret + ", url=" + url + ", secret=" + secret + ", currency=" + currency + ", restActivated=" + restActivated + ", trustedHosts=" + trustedHosts + "]";
	}
}